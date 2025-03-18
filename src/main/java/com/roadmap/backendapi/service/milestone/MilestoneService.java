package com.roadmap.backendapi.service.milestone;

import com.roadmap.backendapi.dto.MilestoneDTO;
import com.roadmap.backendapi.exception.milestone.MilestoneNotFoundException;
import com.roadmap.backendapi.mapper.MilestoneMapper;
import com.roadmap.backendapi.model.Milestone;
import com.roadmap.backendapi.model.Resource;
import com.roadmap.backendapi.model.Roadmap;
import com.roadmap.backendapi.model.enums.MilestoneStatus;
import com.roadmap.backendapi.repository.MilestoneRepository;
import com.roadmap.backendapi.request.milestone.CreateMilestoneRequest;
import com.roadmap.backendapi.request.milestone.UpdateMilestoneRequest;
import com.roadmap.backendapi.service.resource.ResourceService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MilestoneService implements IMilestoneService {

    private final MilestoneRepository milestoneRepository;
    private final MilestoneMapper milestoneMapper;
    private final ChatClient chatClient;
    private final ResourceService resourceService;

    public MilestoneService(MilestoneRepository milestoneRepository, MilestoneMapper milestoneMapper, ChatClient chatClient, ResourceService resourceService) {
        this.milestoneRepository = milestoneRepository;
        this.milestoneMapper = milestoneMapper;
        this.chatClient = chatClient;
        this.resourceService = resourceService;
    }

    @Override
    public MilestoneDTO updateMilestone(UpdateMilestoneRequest request) {
        return null;
    }

    @Override
    public List<Milestone> createMilestone(Roadmap roadmap) {
        String prompt = getMilestonePrompt(roadmap);
        String milestone = chatClient.prompt(prompt).call().content();
        System.out.println("Milestone JSON: " + milestone);

        assert milestone != null;
        return extractMilestones(milestone,roadmap);
    }

    private List<Milestone> extractMilestones(String milestone,Roadmap roadmap) {

        int start = milestone.indexOf("{");
        JSONObject milestoneJson = new JSONObject(milestone.substring(start));
        System.out.println("Milestone JSON: " + milestoneJson);
        JSONArray milestonesJson = milestoneJson.getJSONArray("milestones");
        List<Milestone> milestones=new ArrayList<>();
        for (int i = 0; i < milestonesJson.length(); i++) {
            Milestone milestone1= createMilestoneObjectFromJson(milestonesJson.getJSONObject(i),roadmap);
            milestones.add(milestone1);
        }
        return milestones;
    }

    private Milestone createMilestoneObjectFromJson(JSONObject milestoneJson, Roadmap roadmap) {
       Milestone milestone= Milestone.builder()
                .description(milestoneJson.getString("description"))
                .title(milestoneJson.getString("title"))
               .actionableSteps(milestoneJson.getJSONArray("actionableSteps").toString())
               .prerequisites(milestoneJson.getJSONArray("prerequisites").toString())
               // .dueDate(Timestamp.valueOf(milestoneJson.getString("duration")))
                .status(MilestoneStatus.NOT_STARTED)
                .roadmap(roadmap)
                .build();
       List<Resource> resources = resourceService.createResource(milestone,roadmap);
       milestone.setResources(resources);
       return milestone;
    }

    private String getMilestonePrompt(Roadmap roadmap) {
        return String.format(
                "You are a roadmap milestone generator. Your task is to create a detailed, personalized, and actionable roadmap with milestones based on the user's goal, interests, skills, and the roadmap's title and description. Each milestone should be specific, realistic, and logically ordered to help the user achieve their goal.\n\n" +
                        "Here is the user's data:\n" +
                        "- **Goal**: %s\n" +
                        "- **Interests**: %s\n" +
                        "- **Skills**: %s\n\n" +
                        "Here is the roadmap's data:\n" +
                        "- **Title**: %s\n" +
                        "- **Description**: %s\n\n" +
                        "Generate a list of milestones with the following details for each milestone:\n" +
                        "1. **Id**: A unique identifier starts from 1 .\n" +
                        "2. **Title**: A concise and specific title for the milestone.\n" +
                        "3. **Description**: A detailed description of what the user needs to accomplish in this milestone.\n" +
                        "4. **Actionable Steps**: A step-by-step breakdown of what the user should do to complete this milestone. These steps should be specific and include tasks such as studying, practicing, building projects, or seeking mentorship.\n" +
                        "5. **Prerequisites**: Any knowledge, skills, or prior milestones that the user should complete before starting this milestone.\n" +
                        "6. **Duration**: The estimated time required to complete this milestone (in weeks or months). Ensure the duration is realistic and achievable.\n\n" +
                        "The milestones should:\n" +
                        "- Be logically ordered and build upon each other.\n" +
                        "- Be tailored to the user's goal, interests, and skills.\n" +
                        "- Include a mix of learning, practice, and application tasks.\n" +
                        "- Provide clear progression from beginner to advanced levels (if applicable).\n\n" +
                        "Return the output in JSON format with the following structure:\n" +
                        "{\n" +
                        "  \"roadmapTitle\": \"%s\",\n" +
                        "  \"roadmapDescription\": \"%s\",\n" +
                        "  \"milestones\": [\n" +
                        "    {\n" +
                        "      \"id\": \"unique identifier\",\n" +
                        "      \"title\": \"Milestone Title\",\n" +
                        "      \"description\": \"Detailed milestone description.\",\n" +
                        "      \"actionableSteps\": [\n" +
                        "        \"Step 1: Description of the first actionable step.\",\n" +
                        "        \"Step 2: Description of the second actionable step.\",\n" +
                        "        \"Step 3: Description of the third actionable step.\"\n" +
                        "      ],\n" +
                        "      \"prerequisites\": [\n" +
                        "        \"Prerequisite 1: Description of prerequisite knowledge or skills.\",\n" +
                        "        \"Prerequisite 2: Description of another prerequisite.\"\n" +
                        "      ],\n" +
                        "      \"duration\": \"X weeks/months\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"title\": \"Milestone Title 2\",\n" +
                        "      \"description\": \"Detailed milestone description.\",\n" +
                        "      \"actionableSteps\": [\n" +
                        "        \"Step 1: Description of the first actionable step.\",\n" +
                        "        \"Step 2: Description of the second actionable step.\"\n" +
                        "      ],\n" +
                        "      \"prerequisites\": [\n" +
                        "        \"Prerequisite 1: Description of prerequisite knowledge or skills.\"\n" +
                        "      ],\n" +
                        "      \"duration\": \"X weeks/months\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}\n\n" +
                        "Ensure the milestones are:\n" +
                        "- Specific and actionable.\n" +
                        "- Realistic in terms of time and effort.\n" +
                        "- Tailored to the user's current skills and interests.\n" +
                        "- Logically ordered to ensure a clear progression toward the goal.\n\n",
                roadmap.getUser().getGoal(), roadmap.getUser().getInterests(), roadmap.getUser().getSkills(),
                roadmap.getTitle(), roadmap.getDescription(),
                roadmap.getTitle(), roadmap.getDescription()
        );
    }

    @Override
    public void deleteMilestone(Long milestoneId) {
        if (milestoneRepository.existsById(milestoneId))
            milestoneRepository.deleteById(milestoneId);
        else
            throw new MilestoneNotFoundException();

    }

    @Override
    public MilestoneDTO getMilestoneById(Long milestoneId) {
        return milestoneRepository.findById(milestoneId)
                .map(milestoneMapper::toDTO)
                .orElseThrow(MilestoneNotFoundException::new);
    }

    @Override
    public MilestoneDTO getMilestoneByTitle(String title) {
        return Optional.ofNullable(milestoneRepository.findByTitle(title))
                .map(milestoneMapper::toDTO)
                .orElseThrow(MilestoneNotFoundException::new);
    }

    @Override
    public List<MilestoneDTO> getMilestoneByRoadmapId(Long roadmapId) {
        return milestoneRepository.findByRoadmapId(roadmapId).stream()
                .map(milestoneMapper::toDTO)
                .toList();
    }

    @Override
    public MilestoneDTO getMilestoneByStatus(String status) {
        return getMilestoneByStatus(MilestoneStatus.valueOf(status));
    }
    @Override
    public MilestoneDTO getMilestoneByStatus(MilestoneStatus status) {
        return Optional.ofNullable(milestoneRepository.findByStatus((status)))
                .map(milestoneMapper::toDTO)
                .orElseThrow(MilestoneNotFoundException::new);
    }
}
