package com.roadmap.backendapi.service.milestone;

import com.roadmap.backendapi.dto.MilestoneDTO;
import com.roadmap.backendapi.exception.ConnectionErrorException;
import com.roadmap.backendapi.exception.milestone.MilestoneNotFoundException;
import com.roadmap.backendapi.mapper.MilestoneMapper;
import com.roadmap.backendapi.entity.Milestone;
import com.roadmap.backendapi.entity.Roadmap;
import com.roadmap.backendapi.entity.enums.MilestoneStatus;
import com.roadmap.backendapi.repository.MilestoneRepository;
import com.roadmap.backendapi.service.resource.ResourceService;
import org.springframework.ai.chat.client.ChatClient;
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
    private final ChatClient chatClient;
    private final ResourceService resourceService;

    public MilestoneServiceImpl(MilestoneRepository milestoneRepository, MilestoneMapper milestoneMapper, ChatClient chatClient, ResourceService resourceService) {
        this.milestoneRepository = milestoneRepository;
        this.milestoneMapper = milestoneMapper;
        this.chatClient = chatClient;
        this.resourceService = resourceService;
    }

    /**
     * Updates the milestones for a given roadmap.
     * It generates new milestones using the ChatClient and saves them to the database.
     *
     * @param roadmap the roadmap for which to update milestones
     */
    @Override
    public void updateMilestones(Roadmap roadmap) {
        // prompt the user to generate milestones
        String prompt = getMilestonePrompt(roadmap);
        List<Milestone> milestones = null;
        // Call the chat client to generate milestones based on the prompt
        try {
            milestones = chatClient.prompt(prompt).call().entity(new ParameterizedTypeReference<List<Milestone>>() {
            });
        }  catch (ResourceAccessException e) {
            throw new ConnectionErrorException();
        }
        milestoneRepository.deleteAllByRoadmapId(roadmap.getId());
        assert milestones != null;
        milestones.forEach(milestone ->
                {
                    milestone.setRoadmap(roadmap);
                    resourceService.addResourcesToMilestone(milestone, roadmap);
                }
        );
        milestoneRepository.saveAll(milestones);
    }

    /**
     * Generates a prompt for the ChatClient to create milestones based on the user's data and roadmap details.
     *
     * @param roadmap the roadmap containing user data and roadmap details
     * @return the generated prompt
     */
    private String getMilestonePrompt(Roadmap roadmap) {
        try {
            return String.format(
                    """
                            You are a roadmap milestone generator. Your task is to create a detailed, personalized, and actionable roadmap with milestones based on the user's goal, interests, skills, and the roadmap's title and description. Each milestone should be specific, realistic, and logically ordered to help the user achieve their goal.
                            
                            Here is the user's data:
                            - **Goal**: %s
                            - **Interests**: %s
                            - **Skills**: %s
                            
                            Here is the roadmap's data:
                            - **Title**: %s
                            - **Description**: %s
                            
                            Generate a list of milestones with the following details for each milestone:
                            1. **Title**: A concise and specific title for the milestone.
                            2. **Description**: A detailed description of what the user needs to accomplish in this milestone.
                            3. **Actionable Steps**: A step-by-step breakdown of what the user should do to complete this milestone. These steps should be specific and include tasks such as studying, practicing, building projects, or seeking mentorship.
                            4. **Prerequisites**: Any knowledge, skills, or prior milestones that the user should complete before starting this milestone.
                            5. **Duration**: The estimated time required to complete this milestone (in weeks or months). Ensure the duration is realistic and achievable.
                            
                            The milestones should:
                            - Be logically ordered and build upon each other.
                            - Be tailored to the user's goal, interests, and skills.
                            - Include a mix of learning, practice, and application tasks.
                            - Provide clear progression from beginner to advanced levels (if applicable).
                            
                            Return the output in JSON format with the following structure:
                            Ensure the milestones are:
                            - Specific and actionable.
                            - Realistic in terms of time and effort.
                            - Tailored to the user's current skills and interests.
                            - Logically ordered to ensure a clear progression toward the goal.
                            
                            """,
                    roadmap.getUser().getGoal(), roadmap.getUser().getInterests(), roadmap.getUser().getSkills(),
                    roadmap.getTitle(), roadmap.getDescription()
            );
        }
        catch (NullPointerException e)
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
