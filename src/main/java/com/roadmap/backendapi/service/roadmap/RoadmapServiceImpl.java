package com.roadmap.backendapi.service.roadmap;

import com.roadmap.backendapi.dto.RoadmapDTO;
import com.roadmap.backendapi.entity.Milestone;
import com.roadmap.backendapi.entity.Resource;
import com.roadmap.backendapi.entity.user.User;
import com.roadmap.backendapi.exception.ConnectionErrorException;
import com.roadmap.backendapi.exception.roadmap.*;
import com.roadmap.backendapi.exception.user.UserDataRequiredException;
import com.roadmap.backendapi.exception.user.UserNotFoundException;
import com.roadmap.backendapi.mapper.RoadmapMapper;
import com.roadmap.backendapi.entity.Roadmap;
import com.roadmap.backendapi.repository.RoadmapRepository;
import com.roadmap.backendapi.repository.user.UserRepository;
import com.roadmap.backendapi.request.roadmap.UpdateRoadmapRequest;
import com.roadmap.backendapi.service.ai.AIProviderService;
import com.roadmap.backendapi.service.prompt.PromptService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

/**
 * RoadmapServiceImpl is a service class that implements the RoadmapService interface.
 * It provides methods for managing and searching roadmaps.
 */
@Service
@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
public class RoadmapServiceImpl implements RoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final RoadmapMapper roadMapMapper;
    private final UserRepository userRepository;
    private final AIProviderService aiService;
    private final PromptService promptService;


    public RoadmapServiceImpl(RoadmapRepository roadmapRepository, RoadmapMapper roadMapMapper, UserRepository userRepository, AIProviderService aiService, PromptService promptService) {
        this.roadmapRepository = roadmapRepository;
        this.roadMapMapper = roadMapMapper;
        this.userRepository = userRepository;
        this.aiService = aiService;
        this.promptService = promptService;
    }

    /**
     * Generates a roadmap for a user based on their profile.
     * It fetches the user, generates the roadmap using the AI service, and saves it to the database.
     *
     * @param userId the ID of the user for whom to generate the roadmap
     * @return the generated roadmap as a RoadmapDTO
     * @throws UserNotFoundException if the user with the given ID is not found
     * @throws UserDataRequiredException if the user's profile is incomplete
     * @throws ConnectionErrorException if there's an error connecting to the AI service
     * @throws RoadmapNullException if the generated roadmap is null
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRES_NEW
            ,label = {"roadmap Service", "roadmap generation"},
            rollbackFor = {UserNotFoundException.class, UserDataRequiredException.class, ConnectionErrorException.class, RoadmapNullException.class})
    @Override
    @CacheEvict(value = "roadmaps", key = "#userId")
    public RoadmapDTO generateRoadmap(Long userId) {
        // fetch the user
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // generate the roadmap prompt
        String roadmapPrompt = getCompleteRoadmapPrompt(user);

        // Generate the roadmap using the AI service
        Roadmap generatedRoadmap = aiService.generate(roadmapPrompt, Roadmap.class);

        // ensure the generated roadmap is not null
        if (generatedRoadmap == null) {
            throw new RoadmapNullException();
        }

        generatedRoadmap.setUser(user);

        // generated roadmap with associated milestones
      //  generatedRoadmap = roadmapRepository.save(generatedRoadmap);

        return roadMapMapper.toDTO(generatedRoadmap);
    }

    /**
     * Generates a prompt for the AI service to create a detailed roadmap based on the user's profile.
     * Uses external prompt templates loaded from resources/prompts/roadmap/
     *
     * @param user the user for whom to generate the roadmap
     * @return the generated prompt
     * @throws UserDataRequiredException if the user's profile is incomplete
     */
    String getCompleteRoadmapPrompt(User user) {
        if (user == null || user.getGoal() == null || user.getInterests() == null || user.getSkills() == null) {
                throw new UserDataRequiredException();
        }

        java.util.Map<String, Object> variables = java.util.Map.of(
                "user", java.util.Map.of(
                        "goal", user.getGoal(),
                        "interests", user.getInterests(),
                        "skills", user.getSkills()
                )
        );

        return promptService.renderPrompt("roadmap", "complete_roadmap", variables);
    }


    /**
     * Updates a roadmap for a user.
     * @param request the request containing the updated roadmap details
     * @return the updated roadmap as a RoadmapDTO
     * @throws RoadMapNotFoundException if the roadmap with the given ID is not found
     */
    @Override
    @CachePut(value = "roadmaps", key = "#result.id", unless = "#result == null")
    @Transactional(isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRES_NEW,
            label = {"roadmap Service", "roadmap update"},
            rollbackFor = {RoadMapNotFoundException.class, RoadmapNullException.class})
    public RoadmapDTO updateRoadmap(UpdateRoadmapRequest request) {
        // check if the roadmap exists
        Long roadmapId = request.getRoadmapId();
        if (roadmapId == null) {
            // If no roadmap ID is provided, generate a new roadmap
            return generateRoadmap(request.getUserId());
        }
        
        // Find the existing roadmap
        Roadmap existingRoadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(RoadMapNotFoundException::new);
        
        // Update the roadmap fields if provided in the request
        if (request.getTitle() != null) {
            existingRoadmap.setTitle(request.getTitle());
        }
        
        if (request.getDescription() != null) {
            existingRoadmap.setDescription(request.getDescription());
        }
        
        // Save the updated roadmap
        existingRoadmap = roadmapRepository.save(existingRoadmap);
        
        return roadMapMapper.toDTO(existingRoadmap);
    }


    /**
     * Deletes a roadmap with the given ID.
     *
     * @param roadmapId the ID of the roadmap to delete
     * @throws RoadMapNotFoundException if the roadmap with the given ID is not found
     */
    @Override
    @CacheEvict(value = {"roadmaps", "roadmapsByUser", "roadmapsByTitle"}, allEntries = true)
    @Transactional(isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRES_NEW,
            label = {"roadmap Service", "roadmap delete"},
            rollbackFor = {RoadMapNotFoundException.class})
    public void deleteRoadmap(Long roadmapId) {
        if(roadmapRepository.existsById(roadmapId))
            roadmapRepository.deleteById(roadmapId);
        else
            throw new RoadMapNotFoundException();
    }

    /**
     * Retrieves a roadmap by its ID.
     *
     * @param roadmapId the ID of the roadmap to retrieve
     * @return the retrieved roadmap as a RoadmapDTO
     * @throws RoadMapNotFoundException if the roadmap with the given ID is not found
     */

    @Override
    @Cacheable(value = "roadmaps", key = "#roadmapId", unless = "#result == null")
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public RoadmapDTO getRoadmapById(Long roadmapId) {
        return roadmapRepository.findById(roadmapId)
                .map(roadMapMapper::toDTO)
                .orElseThrow(RoadMapNotFoundException::new);
    }


    /**
     * Retrieves a roadmap by its user ID.
     *
     * @param userId the ID of the user whose roadmap to retrieve
     * @return a list of roadmaps associated with the user
     */
    @Override
    @Cacheable(value = "roadmapsByUser", key = "#userId", unless = "#result == null or #result.isEmpty()")
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<RoadmapDTO> getRoadmapByUserId(Long userId) {
        //TODO : Check if its okay to return a list of roadmaps as DTOs
        return roadmapRepository.findByUserId(userId, RoadmapDTO.class);
    }

    /**
     * Retrieves a roadmap by its title.
     *
     * @param title the title of the roadmap to retrieve
     * @return a list of roadmaps matching the title
     */
    @Override
    @Cacheable(value = "roadmapsByTitle", key = "#title", unless = "#result == null or #result.isEmpty()")
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<RoadmapDTO> getRoadmapByTitle(String title) {
        return roadmapRepository.findByTitleContaining(title).stream()
                .map(roadMapMapper::toDTO).toList();
    }

    /**
     * Retrieves a paginated list of roadmaps by user ID.
     *
     * @param userId the ID of the user whose roadmaps to retrieve
     * @param pageable pagination information (page, size, sort)
     * @return a page of roadmaps associated with the user
     */
    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Page<RoadmapDTO> getRoadmapByUserId(Long userId, Pageable pageable) {
        return roadmapRepository.findByUserId(userId, pageable)
                .map(roadMapMapper::toDTO);
    }

    /**
     * Retrieves a paginated list of roadmaps by title.
     *
     * @param title the title to search for (partial match)
     * @param pageable pagination information (page, size, sort)
     * @return a page of roadmaps matching the title
     */
    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Page<RoadmapDTO> getRoadmapByTitle(String title, Pageable pageable) {
        return roadmapRepository.findByTitleContaining(title, pageable)
                .map(roadMapMapper::toDTO);
    }

    /**
     * Retrieves all roadmaps with pagination.
     *
     * @param pageable pagination information (page, size, sort)
     * @return a page of all roadmaps
     */
    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Page<RoadmapDTO> getAllRoadmaps(Pageable pageable) {
        return roadmapRepository.findAll(pageable)
                .map(roadMapMapper::toDTO);
    }
}