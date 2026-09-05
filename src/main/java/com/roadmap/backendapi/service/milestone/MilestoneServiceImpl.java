package com.roadmap.backendapi.service.milestone;

import com.roadmap.backendapi.dto.MilestoneDTO;
import com.roadmap.backendapi.exception.ConnectionErrorException;
import com.roadmap.backendapi.exception.milestone.MilestoneNotFoundException;
import com.roadmap.backendapi.mapper.MilestoneMapper;
import com.roadmap.backendapi.entity.Milestone;
import com.roadmap.backendapi.entity.Roadmap;
import com.roadmap.backendapi.entity.enums.MilestoneStatus;
import com.roadmap.backendapi.repository.MilestoneRepository;
import com.roadmap.backendapi.service.ai.AIProviderService;
import com.roadmap.backendapi.service.prompt.PromptService;
import com.roadmap.backendapi.service.resource.ResourceService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Optional;

/**
 * MilestoneServiceImpl is a service class that implements the MilestoneService interface.
 * It provides methods for managing and searching milestones in a roadmap.
 */
@Service
public class MilestoneServiceImpl implements MilestoneService {

    private final MilestoneRepository milestoneRepository;
    private final MilestoneMapper milestoneMapper;
    private final AIProviderService aiService;
    private final PromptService promptService;
    private final ResourceService resourceService;

    public MilestoneServiceImpl(MilestoneRepository milestoneRepository, MilestoneMapper milestoneMapper, AIProviderService aiService, PromptService promptService, ResourceService resourceService) {
        this.milestoneRepository = milestoneRepository;
        this.milestoneMapper = milestoneMapper;
        this.aiService = aiService;
        this.promptService = promptService;
        this.resourceService = resourceService;
    }

    /**
     * Updates the milestones for a given roadmap.
     * It generates new milestones using the AI service and saves them to the database.
     *
     * @param roadmap the roadmap for which to update milestones
     */
    @Override
    public void updateMilestones(Roadmap roadmap) {
        // prompt the user to generate milestones
        String prompt = getMilestonePrompt(roadmap);

        // Call the AI service to generate milestones based on the prompt
        List<Milestone> milestones = aiService.generate(prompt, new ParameterizedTypeReference<List<Milestone>>() {});

        milestoneRepository.deleteAllByRoadmapId(roadmap.getId());
        assert milestones != null;
        milestones.forEach(milestone ->
                {
                    resourceService.addResourcesToMilestone(milestone, roadmap);
                }
        );
        milestoneRepository.saveAll(milestones);
    }

    /**
     * Generates a prompt for the AI service to create milestones based on the user's data and roadmap details.
     * Uses external prompt templates loaded from resources/prompts/milestone/
     *
     * @param roadmap the roadmap containing user data and roadmap details
     * @return the generated prompt
     */
    private String getMilestonePrompt(Roadmap roadmap) {
        try {
            java.util.Map<String, Object> variables = java.util.Map.of(
                    "user", java.util.Map.of(
                            "goal", roadmap.getUser().getGoal(),
                            "interests", roadmap.getUser().getInterests(),
                            "skills", roadmap.getUser().getSkills()
                    ),
                    "roadmap", java.util.Map.of(
                            "title", roadmap.getTitle(),
                            "description", roadmap.getDescription()
                    )
            );

            return promptService.renderPrompt("milestone", "milestone_generation", variables);
        }
        catch ( IllegalArgumentException e)
        {
                throw new NullPointerException("RoadMap details is Empty");
        }

    }


    /**
     * delete a milestone with the given ID .
     *
     * @param milestoneId the ID of the milestone to delete
     */
    @Override
    public void deleteMilestone(Long milestoneId) {
        if (milestoneRepository.existsById(milestoneId))
            milestoneRepository.deleteById(milestoneId);
        else
            throw new MilestoneNotFoundException();
    }

    /**
     * Retrieves a milestone by its ID.
     *
     * @param milestoneId the ID of the milestone to retrieve
     * @return the MilestoneDTO object representing the milestone
     */
    @Override
    public MilestoneDTO getMilestoneById(Long milestoneId) {
        return milestoneRepository.findById(milestoneId)
                .map(milestoneMapper::toDTO)
                .orElseThrow(MilestoneNotFoundException::new);
    }

    /**
     * Retrieves a milestone by its title.
     *
     * @param title the title of the milestone to retrieve
     * @return the MilestoneDTO object representing the milestone
     */
    @Override
    public MilestoneDTO getMilestoneByTitle(String title) {
        return Optional.ofNullable(milestoneRepository.findByTitle(title))
                .map(milestoneMapper::toDTO)
                .orElseThrow(MilestoneNotFoundException::new);
    }

    /**
     * Retrieves a list of milestones by the roadmap ID.
     *
     * @param roadmapId the ID of the roadmap to retrieve milestones for
     * @return a list of MilestoneDTO objects representing the milestones
     */
    @Override
    public List<MilestoneDTO> getMilestoneByRoadmapId(Long roadmapId) {
        return milestoneRepository.findByRoadmapId(roadmapId).stream()
                .map(milestoneMapper::toDTO)
                .toList();
    }

    /**
     * Retrieves a milestone by its status.
     *
     * @param status the status of the milestone to retrieve
     * @return the MilestoneDTO object representing the milestone
     */
    @Override
    public MilestoneDTO getMilestoneByStatus(String status) {
        MilestoneStatus  milestoneStatus = null;
        try {
            milestoneStatus= MilestoneStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new MilestoneNotFoundException();
        }
        return Optional.of(milestoneStatus)
                .map(this::getMilestoneByStatus)
                .orElseThrow(MilestoneNotFoundException::new);
    }

    /**
     * Retrieves a milestone by its status.
     *
     * @param status the status of the milestone to retrieve
     * @return the MilestoneDTO object representing the milestone
     */
    @Override
    public MilestoneDTO getMilestoneByStatus(MilestoneStatus status) {
        return Optional.ofNullable(milestoneRepository.findByStatus((status)))
                .map(milestoneMapper::toDTO)
                .orElseThrow(MilestoneNotFoundException::new);
    }
}
