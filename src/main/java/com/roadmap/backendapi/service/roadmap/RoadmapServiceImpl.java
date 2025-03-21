package com.roadmap.backendapi.service.roadmap;

import com.roadmap.backendapi.dto.RoadmapDTO;
import com.roadmap.backendapi.entity.Milestone;
import com.roadmap.backendapi.entity.Resource;
import com.roadmap.backendapi.exception.ConnectionErrorException;
import com.roadmap.backendapi.exception.roadmap.*;
import com.roadmap.backendapi.exception.user.UserDataRequiredException;
import com.roadmap.backendapi.exception.user.UserNotFoundException;
import com.roadmap.backendapi.handler.RoadmapWebSocketHandler;
import com.roadmap.backendapi.mapper.RoadmapMapper;
import com.roadmap.backendapi.entity.Roadmap;
import com.roadmap.backendapi.entity.User;
import com.roadmap.backendapi.repository.MilestoneRepository;
import com.roadmap.backendapi.repository.ResourceRepository;
import com.roadmap.backendapi.repository.RoadmapRepository;
import com.roadmap.backendapi.repository.UserRepository;
import com.roadmap.backendapi.request.roadmap.UpdateRoadmapRequest;
import com.roadmap.backendapi.service.milestone.MilestoneService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;

import java.net.SocketException;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoadmapServiceImpl implements RoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final RoadmapMapper roadMapMapper;
    private final UserRepository userRepository;
    private final ChatClient chatClient;
    private final MilestoneService milestoneService;
    private final RoadmapWebSocketHandler roadmapWebSocketHandler;
    private final MilestoneRepository milestoneRepository;
    private final ResourceRepository resourceRepository;


    public RoadmapServiceImpl(RoadmapRepository roadmapRepository, RoadmapMapper roadMapMapper, UserRepository userRepository, ChatClient chatClient, MilestoneService milestoneService, RoadmapWebSocketHandler roadmapWebSocketHandler, MilestoneRepository milestoneRepository, ResourceRepository resourceRepository) {
        this.roadmapRepository = roadmapRepository;
        this.roadMapMapper = roadMapMapper;
        this.userRepository = userRepository;
        this.chatClient = chatClient;
        this.milestoneService = milestoneService;
        this.roadmapWebSocketHandler = roadmapWebSocketHandler;
        this.milestoneRepository = milestoneRepository;
        this.resourceRepository = resourceRepository;
    }
public RoadmapDTO generateRoadmap(Long userId) {
    // Step 1: Fetch the user
    User user = userRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);

    // Step 2: Generate the roadmap prompt
    String roadmapPrompt = getCompleteRoadmapPrompt(user);

    // Generate the roadmap using the chat client
    Roadmap generatedRoadmap = chatClient.prompt(roadmapPrompt).call().entity(Roadmap.class);

    // Ensure the generated roadmap is not null
    assert generatedRoadmap != null;

    for(Milestone milestone: generatedRoadmap.getMilestones()) {
        milestone.setRoadmap(generatedRoadmap);
        for(Resource resource : milestone.getResources())
        {
            resource.setMilestone(milestone);
        }
    };
    generatedRoadmap.setUser(user);

    // Save the generated roadmap with associated milestones
    generatedRoadmap = roadmapRepository.save(generatedRoadmap);

    return roadMapMapper.toDTO(generatedRoadmap);
}
    private String getCompleteRoadmapPrompt(User user) {
        if (user == null || user.getGoal() == null || user.getInterests() == null || user.getSkills() == null) {
                throw  new  UserDataRequiredException();
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
    
                - **Directly relevant** to the milestone’s goal and tasks.
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
    public List<RoadmapDTO> getRoadmapByUserId(Long userId) {
        return roadmapRepository.findByUserId(userId)
                .stream().map(roadMapMapper::toDTO)
                .toList();
    }

    @Override
    public List<RoadmapDTO> getRoadmapByTitle(String title) {
        return roadmapRepository.findByTitleContaining(title).stream()
                .map(roadMapMapper::toDTO).toList();
    }
}
