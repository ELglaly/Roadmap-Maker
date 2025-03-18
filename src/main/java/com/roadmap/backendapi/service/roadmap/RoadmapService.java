package com.roadmap.backendapi.service.roadmap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.json.Json;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.roadmap.backendapi.dto.RoadmapDTO;
import com.roadmap.backendapi.exception.roadmap.RoadMapNotFoundException;
import com.roadmap.backendapi.exception.user.UserNotFoundException;
import com.roadmap.backendapi.mapper.RoadmapMapper;
import com.roadmap.backendapi.model.Milestone;
import com.roadmap.backendapi.model.Roadmap;
import com.roadmap.backendapi.model.User;
import com.roadmap.backendapi.repository.RoadmapRepository;
import com.roadmap.backendapi.repository.UserRepository;
import com.roadmap.backendapi.request.roadmap.UpdateRoadmapRequest;
import com.roadmap.backendapi.service.milestone.IMilestoneService;
import org.json.JSONObject;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoadmapService implements IRoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final RoadmapMapper roadMapMapper;
    private final UserRepository userRepository;
    private final ChatClient chatClient;
    private final IMilestoneService milestoneService;


    public RoadmapService(RoadmapRepository roadmapRepository, RoadmapMapper roadMapMapper, UserRepository userRepository, ChatClient chatClient, IMilestoneService milestoneService) {
        this.roadmapRepository = roadmapRepository;
        this.roadMapMapper = roadMapMapper;
        this.userRepository = userRepository;
        this.chatClient = chatClient;
        this.milestoneService = milestoneService;
    }

    @Override
    public RoadmapDTO createRoadmap(Long userId) {
        User user =userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        String roadmapPrompt = getRoadMapPrompt(user);
        String roadmap = chatClient.prompt(roadmapPrompt).call().content();

        assert roadmap != null;
        int start = roadmap.indexOf("{") ;
        JSONObject roadmapJson = new JSONObject(roadmap.substring(start));

        Roadmap roadmap1= new Roadmap();
        roadmap1.setTitle(roadmapJson.getString("title"));
        roadmap1.setDescription(roadmapJson.getString("description"));
        roadmap1.setUser(user);

        roadmap1 =roadmapRepository.save(roadmap1);
        List<Milestone> milestones= milestoneService.createMilestone(roadmap1);
        roadmap1.setMilestones(milestones);
        roadmap1=  roadmapRepository.save(roadmap1);
        return roadMapMapper.toDTO(roadmap1);
    }

    private String getRoadMapPrompt(User user) {
        return String.format(
                "You are a career and learning roadmap generator. Based on the user's goal, interests, and skills, create a detailed and personalized roadmap to help them achieve their goal. The roadmap should include the following:\n\n" +
                        "1. Goal: %s\n" +
                        "2. Interests: %s\n" +
                        "3. Skills: %s\n\n" +
                        "Generate a step-by-step roadmap with the following structure:\n" +
                        "- Title: A concise title for the roadmap.\n" +
                        "- Description: A brief description of the roadmap.\n" +
                        "Return the output in JSON format with the following structure:\n" +
                        "{\n" +
                        "  \"title\": \"Roadmap Title\",\n" +
                        "  \"description\": \"Brief roadmap description\",\n" +
                        "}\n\n" +
                        "Make the roadmap realistic, actionable, and tailored to the user's interests and skills.",
                user.getGoal(), user.getInterests(), user.getSkills()
        );
    }

    @Override
    public RoadmapDTO updateRoadmap(UpdateRoadmapRequest request) {
        return null;
    }

    @Override
    public void deleteRoadmap(Long roadmapId) {
       if(roadmapRepository.existsById(roadmapId))
              roadmapRepository.deleteById(roadmapId);
       else
               throw new RoadMapNotFoundException();
    }

    @Override
    public RoadmapDTO getRoadmapById(Long roadmapId) {
        return roadmapRepository.findById(roadmapId)
                .map(roadMapMapper::toDTO)
                .orElseThrow(RoadMapNotFoundException::new);
    }

    @Override
    public RoadmapDTO getRoadmapByUserId(Long userId) {
        return Optional.ofNullable(roadmapRepository.findByUserId(userId))
                .map(roadMapMapper::toDTO)
                .orElseThrow(RoadMapNotFoundException::new);
    }

    @Override
    public RoadmapDTO getRoadmapByTitle(String title) {
        return Optional.ofNullable(roadmapRepository.findByTitle(title))
                .map(roadMapMapper::toDTO)
                .orElseThrow(RoadMapNotFoundException::new);
    }
}
