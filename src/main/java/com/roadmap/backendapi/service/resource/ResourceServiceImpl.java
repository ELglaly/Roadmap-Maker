package com.roadmap.backendapi.service.resource;
import com.roadmap.backendapi.dto.ResourceDTO;
import com.roadmap.backendapi.exception.ConnectionErrorException;
import com.roadmap.backendapi.exception.milestone.MilestoneUnexpectedException;
import com.roadmap.backendapi.exception.resource.ResourceNotFoundException;
import com.roadmap.backendapi.exception.user.UserDataRequiredException;
import com.roadmap.backendapi.mapper.ResourceMapper;
import com.roadmap.backendapi.entity.Milestone;
import com.roadmap.backendapi.entity.Resource;
import com.roadmap.backendapi.entity.Roadmap;
import com.roadmap.backendapi.entity.enums.ResourceType;
import com.roadmap.backendapi.repository.ResourceRepository;
import com.roadmap.backendapi.request.resource.UpdateResourceRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceMapper resourceMapper;
    private final ChatClient chatClient;

    public ResourceServiceImpl(ResourceRepository resourceRepository, ResourceMapper resourceMapper, ChatClient chatClient ) {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
        this.chatClient = chatClient;
    }


    /**
     * Adds resources to a milestone based on the user's roadmap.
     *
     * @param milestone the milestone to which resources will be added
     * @param roadmap   the roadmap containing user data and details
     */
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            rollbackFor = {Exception.class})
    @Override
    public void addResourcesToMilestone(Milestone milestone , Roadmap roadmap) {
        String prompt = getResourcePrompt(milestone,roadmap)
                .orElseThrow(() -> new MilestoneUnexpectedException("Milestone Fields are Empty "));
        try {
            List<Resource> resources = chatClient.prompt(prompt).call().entity(new ParameterizedTypeReference<>() {});
            if (resources == null || resources.isEmpty()) {
                throw new MilestoneUnexpectedException("No resources generated for milestone");
            }
            // Validate each resource before saving
            resources.forEach(this::validateResource);
            milestone.setResources(resources);
        } catch (ResourceAccessException e) {
            throw new ConnectionErrorException();
        } catch (Exception e) {
            throw new MilestoneUnexpectedException("Failed to parse AI response: " + e.getMessage());
        }

    }
    /**
     * Validates a resource before saving it.
     * This method can be expanded to include more validation logic as needed.
     *
     * @param resource the resource to validate
     * @throws IllegalArgumentException if the resource is invalid
     */

    private void validateResource(Resource resource) {
        if (resource == null || isNullOrEmpty(resource.getTitle())
                || isNullOrEmpty(resource.getUrl())
                || resource.getType() == null) {
            throw new IllegalArgumentException("Invalid resource: " + resource);
        }
    }


    /**
     * Generates a prompt for the AI to recommend resources based on the user's roadmap and milestone.
     *
     * @param milestone the milestone for which resources are to be recommended
     * @param roadmap   the roadmap containing user data and details
     * @return a formatted prompt string for resource recommendation
     */
    private Optional<String> getResourcePrompt(Milestone milestone, Roadmap roadmap) {

        validateRoadmapData(roadmap);
        return Optional.of(String.format(
                """
                        You are a resource recommendation engine. Your task is to suggest a list of high-quality, relevant, and actionable resources to help the user complete a specific milestone in their roadmap. The resources should be tailored to the user's goal, interests, skills, and the details of the roadmap and milestone.
                        
                        Here is the user's data:
                        - **Goal**: %s
                        - **Interests**: %s
                        - **Skills**: %s
                        
                        Here is the roadmap's data:
                        - **Title**: %s
                        - **Description**: %s
                        
                        Here is the milestone's data:
                        - **Title**: %s
                        - **Description**: %s
                        - **Actionable Steps**: %s
                        - **Prerequisites**: %s
                        
                        Generate a list of resources with the following details for each resource:
                        1. **Id**: A unique identifier starts from 1 .
                        2. **Title**: A concise and specific title for the resource.
                        3. **Type**: The type of resource must be one of the following types (VIDEO, ARTICLE, BOOK, COURSE, PODCAST, TOOL , TUTORIAL, PRACTICE,WEBSITE).
                        4. **URL**: A link to the resource (if available). Ensure the URL is valid and accessible.
                        5. **Description**: A brief description of the resource, explaining how it will help the user complete the milestone.
                        
                        The resources should:
                        - Be directly relevant to the milestone's title and description.
                        - Align with the user's goal, interests, and skills.
                        - Include a mix of resource types (e.g., videos, articles, books, courses, podcasts).
                        - Be high-quality, actionable, and from reputable sources.
                      
                        Ensure the resources are:
                        - Specific and actionable.
                        - Tailored to the user's current skills and interests.
                        - Logically ordered to ensure a clear progression toward completing the milestone.
                        
                        """,
                roadmap.getUser().getGoal(), roadmap.getUser().getInterests(), roadmap.getUser().getSkills(),
                roadmap.getTitle(), roadmap.getDescription(),
                milestone.getTitle(), milestone.getDescription(), milestone.getActionableSteps(), milestone.getPrerequisites()
        )).orElseThrow(() -> new MilestoneUnexpectedException("Milestone Fields are Empty ")).describeConstable();

    }

    /**
     * Checks if a string is null or empty.
     *
     * @param str the string to check
     * @return true if the string is null or empty, false otherwise
     */
    private  boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Validates the data of a roadmap.
     *
     * @param roadmap the roadmap to validate
     * @throws UserDataRequiredException if any required field is empty
     */
    private void validateRoadmapData(Roadmap roadmap) {
        if (roadmap == null|| roadmap.getUser() == null||
                isNullOrEmpty(roadmap.getUser().getGoal())
                || roadmap.getUser().getInterests().isEmpty()
                || roadmap.getUser().getSkills().isEmpty()
                || isNullOrEmpty(roadmap.getTitle())
                || isNullOrEmpty(roadmap.getDescription())){
            throw new UserDataRequiredException();
        }
    }


    /**
     * Adds a new resource to the repository.
     *
     * @param request the resource to be added
     * @return the added resource as a DTO
     */
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            rollbackFor = {Exception.class})
    @Override
    public ResourceDTO updateResource(UpdateResourceRequest request) {
        //TODO: implement the logic to update a resource
        return null;
    }

    /**
     * Deletes a resource from the repository.
     *
     * @param resourceId the ID of the resource to be deleted
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteResource(Long resourceId) {
        if(resourceRepository.existsById(resourceId))
            resourceRepository.deleteById(resourceId);
        else
            throw new ResourceNotFoundException();
    }

    /**
     * Retrieves a resource by its ID.
     *
     * @param resourceId the ID of the resource to be retrieved
     * @return the resource as a DTO
     */
    @Override
    @Transactional(readOnly = true)
    public ResourceDTO getResourceById(Long resourceId) {
        return resourceRepository.findById(resourceId)
                .map(resourceMapper::toDTO)
                .orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Retrieves a resource by its title.
     *
     * @param title the title of the resource to be retrieved
     * @return the resource as a DTO
     */
    @Override
    public ResourceDTO getResourceByTitle(String title) {
        return Optional.ofNullable(resourceRepository.findByTitle(title))
                .map(resourceMapper::toDTO)
                .orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Retrieves a resource by its type.
     *
     */
    @Override
    public ResourceDTO getResourceByType(String type) {
        return Optional.ofNullable(resourceRepository.findByType(ResourceType.valueOf(type)))
                .map(resourceMapper::toDTO)
                .orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Retrieves all resources associated with a specific milestone ID.
     *
     * @param milestoneId the ID of the milestone
     * @return a list of resources as DTOs
     */
    @Override
    public List<ResourceDTO> getResourceByMilestoneId(Long milestoneId) {
        return resourceRepository.findByMilestoneId(milestoneId).stream()
                .map(resourceMapper::toDTO)
                .toList();

    }
}
