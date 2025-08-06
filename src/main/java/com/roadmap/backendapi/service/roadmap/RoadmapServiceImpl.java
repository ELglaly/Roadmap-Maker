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
import com.roadmap.backendapi.repository.UserRepository;
import com.roadmap.backendapi.request.roadmap.UpdateRoadmapRequest;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Optional;

/**
 * RoadmapServiceImpl is a service class that implements the RoadmapService interface.
 * It provides methods for managing and searching roadmaps.
 */
@Service
public class RoadmapServiceImpl implements RoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final RoadmapMapper roadMapMapper;
    private final UserRepository userRepository;
    private final ChatClient chatClient;


    public RoadmapServiceImpl(RoadmapRepository roadmapRepository, RoadmapMapper roadMapMapper, UserRepository userRepository, ChatClient chatClient) {
        this.roadmapRepository = roadmapRepository;
        this.roadMapMapper = roadMapMapper;
        this.userRepository = userRepository;
        this.chatClient = chatClient;
    }

    /**
     * Generates a roadmap for a user based on their profile.
     * It fetches the user, generates the roadmap using the ChatClient, and saves it to the database.
     *
     * @param userId the ID of the user for whom to generate the roadmap
     * @return the generated roadmap as a RoadmapDTO
     * @throws UserNotFoundException if the user with the given ID is not found
     * @throws UserDataRequiredException if the user's profile is incomplete
     * @throws ConnectionErrorException if there's an error connecting to the AI service
     * @throws RoadmapNullException if the generated roadmap is null
     */
    public RoadmapDTO generateRoadmap(Long userId) {
        // Step 1: Fetch the user
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // Step 2: Generate the roadmap prompt
        String roadmapPrompt = getCompleteRoadmapPrompt(user);

        // Generate the roadmap using the chat client
        Roadmap generatedRoadmap = null;
        try {
            generatedRoadmap = chatClient.prompt(roadmapPrompt).call().entity(Roadmap.class);
        } catch (ResourceAccessException e) {
            throw new ConnectionErrorException();
        }

        // Ensure the generated roadmap is not null
        if (generatedRoadmap == null) {
            throw new RoadmapNullException();
        }
        
        if (generatedRoadmap.getMilestones() != null) {
            for (Milestone milestone : generatedRoadmap.getMilestones()) {
                milestone.setRoadmap(generatedRoadmap);
                if (milestone.getResources() != null) {
                    for (Resource resource : milestone.getResources()) {
                        resource.setMilestone(milestone);
                    }
                }
            }
        }
        generatedRoadmap.setUser(user);

        // Save the generated roadmap with associated milestones
        generatedRoadmap = roadmapRepository.save(generatedRoadmap);

        return roadMapMapper.toDTO(generatedRoadmap);
    }

    /**
     * Generates a prompt for the ChatClient to create a detailed roadmap based on the user's profile.
     *
     * @param user the user for whom to generate the roadmap
     * @return the generated prompt
     * @throws UserDataRequiredException if the user's profile is incomplete
     */
    String getCompleteRoadmapPrompt(User user) {
        if (user == null || user.getGoal() == null || user.getInterests() == null || user.getSkills() == null) {
                throw new UserDataRequiredException();
        }
        return String.format(
                """
                You are an expert career and learning roadmap generator. Your task is to create a detailed and comprehensive roadmap to help the user
                 achieve their goal based on their skills, interests, and aspirations.
                ---
                ### **User Profile**
                - **Goal**: %s
                - **Interests**: %s
                - **Skills**: %s
                
                ---
                ## **Step 1: Generate a Detailed Roadmap**
                Create a **step-by-step roadmap** that provides a **clear pathway** toward achieving the user's goal. The roadmap must include:
                1. **Title**: A clear and relevant title.
                2. **Description**: A detailed explanation of what the roadmap covers.
                3. **Created Date**: Set this to today's date.
                
                The roadmap must be **realistic, structured, and actionable** while ensuring a logical progression from beginner to advanced levels (if applicable).
    
                ---
                ## **Step 2: Generate Maximum Number of Roadmap Milestones**
                The roadmap should be divided into **as many meaningful milestones as possible** to provide a **thorough and structured** journey. Each milestone must include:
    
                1. **Title**: A precise name that clearly defines the milestone.
                2. **Description**: A detailed explanation of what the user must accomplish in this milestone.
                3. **Actionable Steps**: A step-by-step list of what the user should do (e.g., studying, practicing, working on projects, networking, mentorship).
                4. **Prerequisites**: Any knowledge, skills, or previous milestones the user must complete before starting this milestone.
                5. **Duration**: The estimated time required to complete this milestone (in weeks or months).
                
                Ensure the milestones are:
                - **Numerous, covering all necessary steps** to achieve the goal.
                - **Logically ordered** to provide structured learning and progression.
                - **Specific and actionable**, with clear tasks the user must complete.
                - **Realistic and achievable** while challenging the user to grow.
    
                ---
                ## **Step 3: Generate Maximum Number of Recommended Resources**
                For each milestone, generate **as many relevant and high-quality resources as possible** to **support learning and skill development**. The resources must be:
    
                - **Directly relevant** to the milestone's goal and tasks.
                - **From reputable sources** to ensure high quality.
                - **A mix of different types** to cater to different learning styles.
    
                Each resource should include:
                1. **Title**: A clear and descriptive name.
                2. **Type**: One of the following: **VIDEO, ARTICLE, BOOK, COURSE, PODCAST, TOOL, TUTORIAL, PRACTICE, WEBSITE**.
                3. **URL**: A valid and accessible link to the resource.
                4. **Description**: A brief explanation of why this resource is useful and how it helps the user complete the milestone.
    
                ---
                ## **Final Requirements**
                - **Maximize the number of milestones and resources** to create a detailed roadmap.
                - **Ensure clear structure and logical progression** from beginner to advanced.
                - **Include diverse resource types** for an effective learning experience.
                - **Focus on high-quality, reputable sources** for all recommendations.
                - **The roadmap should be as detailed and actionable as possible** to provide the user with a **clear path to success**.
    
                """,
                user.getGoal(), user.getInterests(), user.getSkills()
        );
    }


    /**
     * Updates a roadmap for a user.
     * @param request the request containing the updated roadmap details
     * @return the updated roadmap as a RoadmapDTO
     * @throws RoadMapNotFoundException if the roadmap with the given ID is not found
     */
    @Override
    public RoadmapDTO updateRoadmap(UpdateRoadmapRequest request) {
        // First check if the roadmap exists
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
    public List<RoadmapDTO> getRoadmapByUserId(Long userId) {
        return roadmapRepository.findByUserId(userId)
                .stream().map(roadMapMapper::toDTO)
                .toList();
    }

    /**
     * Retrieves a roadmap by its title.
     *
     * @param title the title of the roadmap to retrieve
     * @return a list of roadmaps matching the title
     */
    @Override
    public List<RoadmapDTO> getRoadmapByTitle(String title) {
        return roadmapRepository.findByTitleContaining(title).stream()
                .map(roadMapMapper::toDTO).toList();
    }
}