package com.roadmap.backendapi.service.resource;

import com.roadmap.backendapi.dto.ResourceDTO;
import com.roadmap.backendapi.exception.resource.ResourceNotFoundException;
import com.roadmap.backendapi.mapper.ResourceMapper;
import com.roadmap.backendapi.model.Milestone;
import com.roadmap.backendapi.model.Resource;
import com.roadmap.backendapi.model.Roadmap;
import com.roadmap.backendapi.model.enums.ResourceType;
import com.roadmap.backendapi.repository.MilestoneRepository;
import com.roadmap.backendapi.repository.ResourceRepository;
import com.roadmap.backendapi.request.resource.CreateResourceRequest;
import com.roadmap.backendapi.request.resource.UpdateResourceRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ResourceService implements IResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceMapper resourceMapper;
    private final ChatClient chatClient;
    private final MilestoneRepository milestoneRepository;

    public ResourceService(ResourceRepository resourceRepository, ResourceMapper resourceMapper, ChatClient chatClient, MilestoneRepository milestoneRepository) {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
        this.chatClient = chatClient;
        this.milestoneRepository = milestoneRepository;
    }


    @Override
    public List<Resource> createResource(Milestone milestone , Roadmap roadmap) {
        String prompt = getResourcePrompt(milestone,roadmap);
        String resource = chatClient.prompt(prompt).call().content();
        System.out.println("Resource JSON: " + resource);
        assert resource != null;
        return extractResources(resource,milestone);
    }
//        milestone.setResources(resources);
//       // milestoneRepository.save(milestone);
//        return resources;
//    }

    private List<Resource> extractResources(String resource,Milestone milestone) {
        int start = resource.indexOf("{");
        JSONObject resourceJson = new JSONObject(resource.substring(start));
        System.out.println("Milestone JSON: " + resourceJson);
        JSONArray resourcesJson = resourceJson.getJSONArray("resources");
        List<Resource> resources=new ArrayList<>();
        for (int i = 0; i < resourcesJson.length(); i++) {
            Resource resource1= createResourceObjectFromJson(resourcesJson.getJSONObject(i),milestone);
            resources.add(resource1);
        }
        return resources;
    }

    private Resource createResourceObjectFromJson(JSONObject jsonObject,Milestone milestone) {
        return Resource.builder()
                .title(jsonObject.getString("title"))
                .type(ResourceType.valueOf(jsonObject.getString("type")))
                .url(jsonObject.getString("url"))
                .milestone(milestone)
                .build();
    }

    private String getResourcePrompt(Milestone milestone, Roadmap roadmap) {
        return String.format(
                "You are a resource recommendation engine. Your task is to suggest a list of high-quality, relevant, and actionable resources to help the user complete a specific milestone in their roadmap. The resources should be tailored to the user's goal, interests, skills, and the details of the roadmap and milestone.\n\n" +
                        "Here is the user's data:\n" +
                        "- **Goal**: %s\n" +
                        "- **Interests**: %s\n" +
                        "- **Skills**: %s\n\n" +
                        "Here is the roadmap's data:\n" +
                        "- **Title**: %s\n" +
                        "- **Description**: %s\n\n" +
                        "Here is the milestone's data:\n" +
                        "- **Title**: %s\n" +
                        "- **Description**: %s\n" +
                        "- **Actionable Steps**: %s\n" +
                        "- **Prerequisites**: %s\n\n" +
                        "Generate a list of resources with the following details for each resource:\n" +
                        "1. **Id**: A unique identifier starts from 1 .\n" +
                        "2. **Title**: A concise and specific title for the resource.\n" +
                        "3. **Type**: The type of resource must be one of the following types (VIDEO, ARTICLE, BOOK, COURSE, PODCAST, TOOL , TUTORIAL, PRACTICE,WEBSITE).\n" +
                        "4. **URL**: A link to the resource (if available). Ensure the URL is valid and accessible.\n" +
                        "5. **Description**: A brief description of the resource, explaining how it will help the user complete the milestone.\n\n" +
                        "The resources should:\n" +
                        "- Be directly relevant to the milestone's title and description.\n" +
                        "- Align with the user's goal, interests, and skills.\n" +
                        "- Include a mix of resource types (e.g., videos, articles, books, courses, podcasts).\n" +
                        "- Be high-quality, actionable, and from reputable sources.\n\n" +
                        "Return the output in JSON format with the following structure:\n" +
                        "{\n" +
                        "  \"resources\": [\n" +
                        "    {\n" +
                        "      \"id\": \"unique identifier\",\n" +
                        "      \"title\": \"Resource Title\",\n" +
                        "      \"type\": \"RESOURCE_TYPE\",\n" +
                        "      \"url\": \"https://example.com/resource\",\n" +
                        "      \"description\": \"Brief description of the resource and how it helps the user.\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"title\": \"Resource Title 2\",\n" +
                        "      \"type\": \"RESOURCE_TYPE\",\n" +
                        "      \"url\": \"https://example.com/resource2\",\n" +
                        "      \"description\": \"Brief description of the resource and how it helps the user.\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}\n\n" +
                        "Ensure the resources are:\n" +
                        "- Specific and actionable.\n" +
                        "- Tailored to the user's current skills and interests.\n" +
                        "- Logically ordered to ensure a clear progression toward completing the milestone.\n\n",
                roadmap.getUser().getGoal(), roadmap.getUser().getInterests(), roadmap.getUser().getSkills(),
                roadmap.getTitle(), roadmap.getDescription(),
                milestone.getTitle(), milestone.getDescription(), milestone.getActionableSteps(), milestone.getPrerequisites(),
                milestone.getTitle(), milestone.getDescription(), milestone.getActionableSteps(), milestone.getPrerequisites()
        );

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
