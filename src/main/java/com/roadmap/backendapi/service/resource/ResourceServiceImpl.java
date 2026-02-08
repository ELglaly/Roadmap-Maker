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
import com.roadmap.backendapi.service.ai.AIProviderService;
import com.roadmap.backendapi.service.prompt.PromptService;
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
    private final AIProviderService aiService;
    private final PromptService promptService;

    public ResourceServiceImpl(ResourceRepository resourceRepository, ResourceMapper resourceMapper, AIProviderService aiService, PromptService promptService) {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
        this.aiService = aiService;
        this.promptService = promptService;
    }


    /**
     * Adds resources to a milestone based on the user's roadmap.
     * Uses the AI service to generate relevant resources.
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

        List<Resource> resources = aiService.generate(prompt, new ParameterizedTypeReference<>() {});

        if (resources == null || resources.isEmpty()) {
            throw new MilestoneUnexpectedException("No resources generated for milestone");
        }

        // Validate each resource before saving
        resources.forEach(this::validateResource);
        milestone.setResources(resources);
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
     * Uses external prompt templates loaded from resources/prompts/resource/
     *
     * @param milestone the milestone for which resources are to be recommended
     * @param roadmap   the roadmap containing user data and details
     * @return a formatted prompt string for resource recommendation
     */
    private Optional<String> getResourcePrompt(Milestone milestone, Roadmap roadmap) {

        validateRoadmapData(roadmap);

        java.util.Map<String, Object> variables = java.util.Map.of(
                "user", java.util.Map.of(
                        "goal", roadmap.getUser().getGoal(),
                        "interests", roadmap.getUser().getInterests(),
                        "skills", roadmap.getUser().getSkills()
                ),
                "roadmap", java.util.Map.of(
                        "title", roadmap.getTitle(),
                        "description", roadmap.getDescription()
                ),
                "milestone", java.util.Map.of(
                        "title", milestone.getTitle(),
                        "description", milestone.getDescription(),
                        "actionableSteps", milestone.getActionableSteps(),
                        "prerequisites", milestone.getPrerequisites()
                )
        );

        return Optional.of(promptService.renderPrompt("resource", "resource_recommendation", variables));
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
