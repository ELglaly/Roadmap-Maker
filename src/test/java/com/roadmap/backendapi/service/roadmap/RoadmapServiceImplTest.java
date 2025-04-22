package com.roadmap.backendapi.service.roadmap;
import com.roadmap.backendapi.dto.RoadmapDTO;
import com.roadmap.backendapi.entity.Milestone;
import com.roadmap.backendapi.entity.Roadmap;
import com.roadmap.backendapi.entity.User;
import com.roadmap.backendapi.exception.roadmap.RoadMapNotFoundException;
import com.roadmap.backendapi.exception.roadmap.RoadmapNullException;
import com.roadmap.backendapi.exception.user.UserDataRequiredException;
import com.roadmap.backendapi.exception.user.UserNotFoundException;
import com.roadmap.backendapi.mapper.RoadmapMapper;
import com.roadmap.backendapi.repository.RoadmapRepository;
import com.roadmap.backendapi.repository.UserRepository;
import com.roadmap.backendapi.request.roadmap.UpdateRoadmapRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;



/**
 * Unit tests for the RoadmapServiceImpl class.
 * This class contains test cases for various methods in the RoadmapServiceImpl class,
 * including generating roadmaps, deleting roadmaps, and retrieving roadmaps by ID or title.
 */

@SpringBootTest
@ActiveProfiles("test")
public class RoadmapServiceImplTest {

    @Mock
    private ChatClient chatClient;

    @Mock
    private RoadmapMapper roadMapMapper;

    @Mock
    private RoadmapRepository roadmapRepository;

    @InjectMocks
    private RoadmapServiceImpl roadmapService;

    @Mock
    private UserRepository userRepository;


    User incompleteUser;
    Roadmap roadmap1;
    Roadmap roadmap2;
    RoadmapDTO dto1 ;
    RoadmapDTO dto2 ;

    @BeforeEach
    public void setUp()
    {
        incompleteUser = new User();
        incompleteUser.setId(1L);

         roadmap1 = new Roadmap();
         roadmap2 = new Roadmap();
         dto1 = new RoadmapDTO();
         dto2 = new RoadmapDTO();
    }

    /**
     * Test case for generating a roadmap with incomplete user data.
     * This test verifies that the method throws a UserDataRequiredException
     * when the user object is missing required fields (goal, interests, or skills).
     */
    @Test
    public void testGenerateRoadmapWithIncompleteUserData() {
        RoadmapServiceImpl roadmapService = new RoadmapServiceImpl(null, null, null, null);

        assertThrows(UserDataRequiredException.class, () -> {
            roadmapService.getCompleteRoadmapPrompt(incompleteUser);
        });
    }

    /**
     * Tests the generateRoadmap method when the user data is incomplete.
     * This test verifies that a UserDataRequiredException is thrown when trying to generate a roadmap for a user with missing required data.
     */
    @Test
    public void testGenerateRoadmap_IncompleteUserData() {

        when(userRepository.findById(incompleteUser.getId())).thenReturn(Optional.of(incompleteUser));

        assertThrows(UserDataRequiredException.class, () -> roadmapService.generateRoadmap(incompleteUser.getId()));
    }

    /**
     * Tests the generateRoadmap method when the user is not found.
     * This test verifies that a UserNotFoundException is thrown when trying to generate a roadmap for a non-existent user.
     */
    @Test
    public void testGenerateRoadmap_UserNotFound() {
        Long nonExistentUserId = 999L;
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> roadmapService.generateRoadmap(nonExistentUserId));
    }


    /**
     * Tests the constructor of RoadmapServiceImpl to ensure it initializes the object correctly with all dependencies.
     * This test verifies that the RoadmapServiceImpl can be instantiated without throwing any exceptions
     * and that the resulting object is not null.
     */
    @Test
    public void testRoadmapServiceImplConstructor() {
        RoadmapServiceImpl roadmapService = new RoadmapServiceImpl(
            roadmapRepository, 
            roadMapMapper, 
            userRepository, 
            chatClient
        );

        assertNotNull(roadmapService, "RoadmapServiceImpl should be successfully instantiated");
    }

    /**
     * Test case for deleteRoadmap method when the roadmap exists.
     * It verifies that the deleteById method is called on the repository
     * and no exception is thrown when the roadmap exists.
     */
    @Test
    public void test_deleteExistingRoadmap() {
        Long roadmapId = 1L;
        when(roadmapRepository.existsById(roadmapId)).thenReturn(true);

        roadmapService.deleteRoadmap(roadmapId);

        verify(roadmapRepository).deleteById(roadmapId);
    }

    /**
     * Tests the deleteRoadmap method when the roadmap with the given ID does not exist.
     * This test verifies that a RoadMapNotFoundException is thrown when attempting to delete a non-existent roadmap.
     */
    @Test
    public void test_deleteRoadmap_nonExistentRoadmap() {
        Long nonExistentRoadmapId = 999L;
        when(roadmapRepository.existsById(nonExistentRoadmapId)).thenReturn(false);

        assertThrows(RoadMapNotFoundException.class, () -> roadmapService.deleteRoadmap(nonExistentRoadmapId));
    }

    /**
     * Test case for deleteRoadmap method when the roadmap does not exist.
     * This test verifies that a RoadMapNotFoundException is thrown when attempting to delete a non-existent roadmap.
     */
    @Test
    public void test_deleteRoadmap_throwsExceptionWhenRoadmapNotFound() {
        Long nonExistentRoadmapId = 1L;
        when(roadmapRepository.existsById(nonExistentRoadmapId)).thenReturn(false);

        assertThrows(RoadMapNotFoundException.class, () -> roadmapService.deleteRoadmap(nonExistentRoadmapId));
    }

    /**
     * Test case for generating a roadmap successfully.
     * This test verifies that the generateRoadmap method correctly fetches the user,
     * generates a roadmap using the ChatClient, saves it to the repository,
     * and returns the mapped DTO.
     */
    @Test
    public void test_generateRoadmap_SuccessfulGeneration() {
        // Arrange
        incompleteUser.setGoal("Learn Java");
        incompleteUser.setInterests("Programming");
        incompleteUser.setSkills("Basic coding");

        Roadmap generatedRoadmap = new Roadmap();
        RoadmapDTO expectedDto = new RoadmapDTO();

        ChatClient.ChatClientRequestSpec mockPromptSpec = mock(ChatClient.ChatClientRequestSpec.class);
        ChatClient.CallResponseSpec  mockCallSpec = mock(ChatClient.CallResponseSpec.class);


        when(userRepository.findById(incompleteUser.getId())).thenReturn(Optional.of(incompleteUser));
        when(chatClient.prompt(anyString())).thenReturn(mockPromptSpec);
        when(mockPromptSpec.call()).thenReturn(mockCallSpec);
        when(mockCallSpec.entity(Roadmap.class)).thenReturn(generatedRoadmap);
        when(roadmapRepository.save(any(Roadmap.class))).thenReturn(generatedRoadmap);
        when(roadMapMapper.toDTO(generatedRoadmap)).thenReturn(expectedDto);


        // Act
        RoadmapDTO result = roadmapService.generateRoadmap(incompleteUser.getId());

        // Assert
        assertNotNull(result);
        verify(userRepository).findById(incompleteUser.getId());
        verify(chatClient).prompt(anyString());
        verify(roadmapRepository).save(any(Roadmap.class));
        verify(roadMapMapper).toDTO(generatedRoadmap);
    }

    /**
     * Tests the getRoadmapById method when the roadmap with the given ID does not exist.
     * This test verifies that a RoadMapNotFoundException is thrown when the roadmap is not found.
     */
    @Test
    public void test_getRoadmapById_nonExistentRoadmap() {
        // Arrange
        Long nonExistentRoadmapId = 999L;
        when(roadmapRepository.findById(nonExistentRoadmapId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RoadMapNotFoundException.class, () -> roadmapService.getRoadmapById(nonExistentRoadmapId));
    }

    /**
     * Test case for getRoadmapById method when the roadmap exists.
     * It should return the roadmap DTO when the roadmap is found in the repository.
     */
    @Test
    public void test_getRoadmapById_whenRoadmapExists() {
        // Arrange
        Long roadmapId = 1L;
        Roadmap roadmap = new Roadmap();
        RoadmapDTO roadmapDTO = new RoadmapDTO();

        when(roadmapRepository.findById(roadmapId)).thenReturn(Optional.of(roadmap));
        when(roadMapMapper.toDTO(roadmap)).thenReturn(roadmapDTO);

        // Act
        RoadmapDTO result = roadmapService.getRoadmapById(roadmapId);

        // Assert
        assertNotNull(result);
        assertEquals(roadmapDTO, result);
        verify(roadmapRepository).findById(roadmapId);
        verify(roadMapMapper).toDTO(roadmap);
    }

    /**
     * Test case for getRoadmapByTitle method.
     * This test verifies that the method correctly retrieves roadmaps by title,
     * maps them to DTOs, and returns the list of RoadmapDTOs.
     */
    @Test
    public void test_getRoadmapByTitle_ReturnsMatchingRoadmaps() {
        // Arrange
        String title = "Java";
        List<Roadmap> roadmaps = Arrays.asList(roadmap1, roadmap2);

        when(roadmapRepository.findByTitleContaining(title)).thenReturn(roadmaps);
        when(roadMapMapper.toDTO(roadmap1)).thenReturn(dto1);
        when(roadMapMapper.toDTO(roadmap2)).thenReturn(dto2);

        // Act
        List<RoadmapDTO> result = roadmapService.getRoadmapByTitle(title);

        // Assert
        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));

        Mockito.verify(roadmapRepository).findByTitleContaining(title);
        Mockito.verify(roadMapMapper).toDTO(roadmap1);
        Mockito.verify(roadMapMapper).toDTO(roadmap2);
    }

    /**
     * Test case for getRoadmapByUserId method.
     * This test verifies that the method correctly retrieves roadmaps for a given user ID,
     * maps them to DTOs, and returns the list of RoadmapDTOs.
     */
    @Test
    public void test_getRoadmapByUserId_returnsListOfRoadmapDTOs() {
        // Arrange
        List<Roadmap> roadmaps = Arrays.asList(roadmap1, roadmap2);

        when(roadmapRepository.findByUserId(incompleteUser.getId())).thenReturn(roadmaps);
        when(roadMapMapper.toDTO(roadmap1)).thenReturn(dto1);
        when(roadMapMapper.toDTO(roadmap2)).thenReturn(dto2);

        // Act
        List<RoadmapDTO> result = roadmapService.getRoadmapByUserId(incompleteUser.getId());

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
        verify(roadmapRepository).findByUserId(incompleteUser.getId());
        verify(roadMapMapper, times(2)).toDTO(any(Roadmap.class));
    }

    /**
     * Test case for updateRoadmap method when it returns null.
     * This test verifies that the updateRoadmap method returns null
     * when called with any UpdateRoadmapRequest.
     */
    @Test
    public void test_updateRoadmap_withNoRoadmapId_callsGenerateRoadmap() {
        // Arrange
        UpdateRoadmapRequest request = new UpdateRoadmapRequest();
        request.setUserId(1L);
        
        // Create a spy of the service to verify method calls
        RoadmapServiceImpl serviceSpy = Mockito.spy(roadmapService);
        RoadmapDTO expectedDto = new RoadmapDTO();
        
        // Mock the generateRoadmap method to return a DTO
        Mockito.doReturn(expectedDto).when(serviceSpy).generateRoadmap(request.getUserId());
        
        // Act
        RoadmapDTO result = serviceSpy.updateRoadmap(request);
        
        // Assert
        assertNotNull(result);
        assertEquals(expectedDto, result);
        Mockito.verify(serviceSpy).generateRoadmap(request.getUserId());
    }
    
    /**
     * Test case for updateRoadmap method when a roadmapId is provided.
     * This test verifies that the updateRoadmap method updates an existing roadmap
     * when a roadmapId is provided in the request.
     */
    @Test
    public void test_updateRoadmap_withRoadmapId_updatesExistingRoadmap() {
        // Arrange
        Long roadmapId = 1L;
        UpdateRoadmapRequest request = new UpdateRoadmapRequest();
        request.setRoadmapId(roadmapId);
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");
        
        Roadmap existingRoadmap = new Roadmap();
        existingRoadmap.setId(roadmapId);
        existingRoadmap.setTitle("Original Title");
        existingRoadmap.setDescription("Original Description");
        
        Roadmap updatedRoadmap = new Roadmap();
        updatedRoadmap.setId(roadmapId);
        updatedRoadmap.setTitle(request.getTitle());
        updatedRoadmap.setDescription(request.getDescription());
        
        RoadmapDTO expectedDto = new RoadmapDTO();
        
        when(roadmapRepository.findById(roadmapId)).thenReturn(Optional.of(existingRoadmap));
        when(roadmapRepository.save(any(Roadmap.class))).thenReturn(updatedRoadmap);
        when(roadMapMapper.toDTO(updatedRoadmap)).thenReturn(expectedDto);
        
        // Act
        RoadmapDTO result = roadmapService.updateRoadmap(request);
        
        // Assert
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(roadmapRepository).findById(roadmapId);
        verify(roadmapRepository).save(any(Roadmap.class));
        verify(roadMapMapper).toDTO(updatedRoadmap);
    }

    /**
     * Test case for generateRoadmap method when the AI service returns null.
     * This test verifies that a RoadmapNullException is thrown when the AI service returns null.
     */
    @Test
    public void test_generateRoadmap_throwsExceptionWhenAIReturnsNull() {
        // Arrange
        incompleteUser.setGoal("Learn Java");
        incompleteUser.setInterests("Programming");
        incompleteUser.setSkills("Basic coding");

        ChatClient.ChatClientRequestSpec mockPromptSpec = mock(ChatClient.ChatClientRequestSpec.class);
        ChatClient.CallResponseSpec mockCallSpec = mock(ChatClient.CallResponseSpec.class);

        when(userRepository.findById(incompleteUser.getId())).thenReturn(Optional.of(incompleteUser));
        when(chatClient.prompt(anyString())).thenReturn(mockPromptSpec);
        when(mockPromptSpec.call()).thenReturn(mockCallSpec);
        when(mockCallSpec.entity(Roadmap.class)).thenReturn(null);

        // Act & Assert
        assertThrows(RoadmapNullException.class, () -> roadmapService.generateRoadmap(incompleteUser.getId()));
    }

    /**
     * Test case for generateRoadmap method when the roadmap has null resources.
     * This test verifies that the method handles null resources properly without throwing NullPointerException.
     */
    @Test
    public void test_generateRoadmap_handlesNullResources() {
        // Arrange
        incompleteUser.setGoal("Learn Java");
        incompleteUser.setInterests("Programming");
        incompleteUser.setSkills("Basic coding");

        Roadmap generatedRoadmap = new Roadmap();
        Milestone milestone = new Milestone();
        milestone.setResources(null); // Set resources to null
        generatedRoadmap.setMilestones(List.of(milestone));
        
        RoadmapDTO expectedDto = new RoadmapDTO();

        ChatClient.ChatClientRequestSpec mockPromptSpec = mock(ChatClient.ChatClientRequestSpec.class);
        ChatClient.CallResponseSpec mockCallSpec = mock(ChatClient.CallResponseSpec.class);

        when(userRepository.findById(incompleteUser.getId())).thenReturn(Optional.of(incompleteUser));
        when(chatClient.prompt(anyString())).thenReturn(mockPromptSpec);
        when(mockPromptSpec.call()).thenReturn(mockCallSpec);
        when(mockCallSpec.entity(Roadmap.class)).thenReturn(generatedRoadmap);
        when(roadmapRepository.save(any(Roadmap.class))).thenReturn(generatedRoadmap);
        when(roadMapMapper.toDTO(generatedRoadmap)).thenReturn(expectedDto);

        // Act
        RoadmapDTO result = roadmapService.generateRoadmap(incompleteUser.getId());

        // Assert
        assertNotNull(result);
        verify(roadmapRepository).save(any(Roadmap.class));
    }
    }





