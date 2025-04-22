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
import org.springframework.web.client.ResourceAccessException;
import java.util.List;
import java.util.Optional;

@Service
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceMapper resourceMapper;
    private final ChatClient chatClient;

    public ResourceServiceImpl(ResourceRepository resourceRepository, ResourceMapper resourceMapper, ChatClient chatClient ) {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
        this.chatClient = chatClient;
    }


    @Override
    public void addResourcesToMilestone(Milestone milestone , Roadmap roadmap) {
        String prompt = getResourcePrompt(milestone,roadmap);

        List<Resource> resource = null;
        try {
            resource = chatClient.prompt(prompt).call().entity(new ParameterizedTypeReference<List<Resource>>() {
            });
        } catch (ResourceAccessException e) {
            throw new ConnectionErrorException();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
        milestone.setResources(resource);
    }

    private String getResourcePrompt(Milestone milestone, Roadmap roadmap) {

        validateRoadmapData(roadmap);
        validateMilestoneData(milestone);

        return String.format(
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
        );

    }

    private  boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    private void validateRoadmapData(Roadmap roadmap) {
        if (isNullOrEmpty(roadmap.getUser().getGoal()) ||
                isNullOrEmpty(roadmap.getUser().getInterests()) ||
                isNullOrEmpty(roadmap.getUser().getSkills()) ||
                isNullOrEmpty(roadmap.getTitle()) ||
                isNullOrEmpty(roadmap.getDescription())){
            throw new UserDataRequiredException();
        }
    }

    private void validateMilestoneData(Milestone milestone) {
        if ( isNullOrEmpty(milestone.getTitle()) ||
                isNullOrEmpty(milestone.getDescription()) ||
                isNullOrEmpty(milestone.getActionableSteps()) ||
                isNullOrEmpty(milestone.getPrerequisites())){
            throw new MilestoneUnexpectedException("Milestone Fields are Empty ");
        }
    }

    @Override
    public ResourceDTO updateResource(UpdateResourceRequest request) {
        return null;
    }

    @Override
    public void deleteResource(Long resourceId) {
        if(resourceRepository.existsById(resourceId))
            resourceRepository.deleteById(resourceId);
        else
            throw new ResourceNotFoundException();
    }

    @Override
    public ResourceDTO getResourceById(Long resourceId) {
        return resourceRepository.findById(resourceId)
                .map(resourceMapper::toDTO)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public ResourceDTO getResourceByTitle(String title) {
        return Optional.ofNullable(resourceRepository.findByTitle(title))
                .map(resourceMapper::toDTO)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public ResourceDTO getResourceByType(String type) {
        return Optional.ofNullable(resourceRepository.findByType(ResourceType.valueOf(type)))
                .map(resourceMapper::toDTO)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public ResourceDTO getResourceByUrl(String url) {
        return Optional.ofNullable(resourceRepository.findByUrl(url))
                .map(resourceMapper::toDTO)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public List<ResourceDTO> getResourceByMilestoneId(Long milestoneId) {
        return resourceRepository.findByMilestoneId(milestoneId).stream()
                .map(resourceMapper::toDTO)
                .toList();

    }
}
