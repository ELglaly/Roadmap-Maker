package com.roadmap.backendapi.service.milestone;

import com.roadmap.backendapi.dto.MilestoneDTO;
import com.roadmap.backendapi.entity.Milestone;
import com.roadmap.backendapi.entity.Roadmap;
import com.roadmap.backendapi.entity.User;
import com.roadmap.backendapi.entity.enums.MilestoneStatus;
import com.roadmap.backendapi.exception.ConnectionErrorException;
import com.roadmap.backendapi.exception.milestone.MilestoneNotFoundException;
import com.roadmap.backendapi.mapper.MilestoneMapper;
import com.roadmap.backendapi.repository.MilestoneRepository;
import com.roadmap.backendapi.service.resource.ResourceService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.ResourceAccessException;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class MilestoneServiceImplTest {

    @Mock
    private ChatClient chatClient;

    @Mock
    private MilestoneMapper milestoneMapper;

    @Mock
    private MilestoneRepository milestoneRepository;

    @InjectMocks
    private MilestoneServiceImpl milestoneService;

    @Mock
    private ResourceService resourceService;

    /**
     * Test case for deleteMilestone method when the milestone exists.
     * It verifies that the method calls deleteById on the repository when the milestone exists.
     */
    @Test
    public void testDeleteMilestone_WhenMilestoneExists() {
        Long milestoneId = 1L;
        when(milestoneRepository.existsById(milestoneId)).thenReturn(true);

        milestoneService.deleteMilestone(milestoneId);

        verify(milestoneRepository).deleteById(milestoneId);
        verify(milestoneRepository).existsById(milestoneId);
        verifyNoMoreInteractions(milestoneRepository);

    }

    /**
     * Tests the getMilestoneById method when the milestone is not found.
     * This test verifies that a MilestoneNotFoundException is thrown when
     * the repository returns an empty Optional for the given milestone ID.
     */
    @Test
    public void testGetMilestoneByIdWhenMilestoneNotFound() {
        //Arrange
        Long nonExistentMilestoneId = 999L;
        when(milestoneRepository.findById(nonExistentMilestoneId)).thenReturn(Optional.empty());

        //Act
        MilestoneNotFoundException  exception = assertThrows(MilestoneNotFoundException.class,
                () -> milestoneService.getMilestoneById(nonExistentMilestoneId));

        //Assert
        assertEquals("MILESTONE_NOT_FOUND", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(milestoneRepository).findById(nonExistentMilestoneId);
        verifyNoMoreInteractions(milestoneRepository);
    }

    /**
     * Test case for getMilestoneByStatus method when a milestone with the given status exists.
     * It verifies that the method returns the correct MilestoneDTO when the milestone is found.
     */
    @Test
    public void testGetMilestoneByStatusWhenMilestoneExists() {
        // Arrange
        MilestoneStatus status = MilestoneStatus.IN_PROGRESS;
        Milestone milestone = new Milestone();
        MilestoneDTO expectedDto = new MilestoneDTO();

        when(milestoneRepository.findByStatus(status)).thenReturn(milestone);
        when(milestoneMapper.toDTO(milestone)).thenReturn(expectedDto);

        // Act
        MilestoneDTO result = milestoneService.getMilestoneByStatus(status);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(milestoneRepository).findByStatus(status);
        verify(milestoneMapper).toDTO(milestone);
    }

    /**
     * Test case for MilestoneServiceImpl constructor.
     * Verifies that the constructor initializes the service with the provided dependencies.
     */
    @Test
    public void test_MilestoneServiceImpl_Constructor() {
        MilestoneRepository mockRepository = mock(MilestoneRepository.class);
        MilestoneMapper mockMapper = mock(MilestoneMapper.class);
        ChatClient mockChatClient = mock(ChatClient.class);
        ResourceService mockResourceService = mock(ResourceService.class);

        MilestoneServiceImpl milestoneService = new MilestoneServiceImpl(
            mockRepository, mockMapper, mockChatClient, mockResourceService);

        // No assertions needed as we're just testing the constructor doesn't throw exceptions
    }

    /**
     * Test case for deleteMilestone method when the milestone does not exist.
     * This test verifies that a MilestoneNotFoundException is thrown when
     * attempting to delete a non-existent milestone.
     */
    @Test
    public void test_deleteMilestone_nonExistentMilestone() {
        Long nonExistentMilestoneId = 999L;
        when(milestoneRepository.existsById(nonExistentMilestoneId)).thenReturn(false);

        assertThrows(MilestoneNotFoundException.class, () -> {
            milestoneService.deleteMilestone(nonExistentMilestoneId);
        });
    }

    /**
     * Test case for deleting a non-existent milestone.
     * This test verifies that the deleteMilestone method throws a MilestoneNotFoundException
     * when attempting to delete a milestone with an ID that does not exist in the repository.
     */
    @Test
    public void test_deleteMilestone_nonExistentMilestone_2() {
        Long nonExistentMilestoneId = 999L;
        when(milestoneRepository.existsById(nonExistentMilestoneId)).thenReturn(false);

        assertThrows(MilestoneNotFoundException.class, () -> {
            milestoneService.deleteMilestone(nonExistentMilestoneId);
        });
    }

    /**
     * Test case for getMilestoneById method when the milestone exists.
     * It verifies that the method correctly retrieves and maps a milestone to DTO
     * when given a valid milestone ID.
     */
    @Test
    public void test_getMilestoneById_ExistingMilestone() {
        // Arrange
        Long milestoneId = 1L;
        Milestone milestone = new Milestone();
        MilestoneDTO expectedDto = new MilestoneDTO();

        when(milestoneRepository.findById(milestoneId)).thenReturn(Optional.of(milestone));
        when(milestoneMapper.toDTO(milestone)).thenReturn(expectedDto);

        // Act
        MilestoneDTO result = milestoneService.getMilestoneById(milestoneId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(milestoneRepository).findById(milestoneId);
        verify(milestoneMapper).toDTO(milestone);
    }

    /**
     * Test case for getMilestoneByRoadmapId method.
     * This test verifies that the method correctly retrieves and maps milestones for a given roadmap ID.
     */
    @Test
    public void test_getMilestoneByRoadmapId_returnsMappedMilestones() {
        // Arrange
        Long roadmapId = 1L;
        Milestone milestone1 = new Milestone();
        Milestone milestone2 = new Milestone();
        List<Milestone> milestones = Arrays.asList(milestone1, milestone2);

        MilestoneDTO milestoneDTO1 = new MilestoneDTO();
        MilestoneDTO milestoneDTO2 = new MilestoneDTO();

        when(milestoneRepository.findByRoadmapId(roadmapId)).thenReturn(milestones);
        when(milestoneMapper.toDTO(milestone1)).thenReturn(milestoneDTO1);
        when(milestoneMapper.toDTO(milestone2)).thenReturn(milestoneDTO2);

        // Act
        List<MilestoneDTO> result = milestoneService.getMilestoneByRoadmapId(roadmapId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(milestoneDTO1, result.get(0));
        assertEquals(milestoneDTO2, result.get(1));

        verify(milestoneRepository).findByRoadmapId(roadmapId);
        verify(milestoneMapper).toDTO(milestone1);
        verify(milestoneMapper).toDTO(milestone2);
        verifyNoMoreInteractions(milestoneRepository, milestoneMapper);
    }

    /**
     * Test case for getMilestoneByStatus method when a valid status string is provided.
     * It verifies that the method correctly converts the string to MilestoneStatus enum
     * and delegates the call to the overloaded method with MilestoneStatus parameter.
     */
    @Test
    public void test_getMilestoneByStatus_WithValidStatusString() {
        // Arrange
        String statusString = "IN_PROGRESS";
        MilestoneStatus status = MilestoneStatus.IN_PROGRESS;
        MilestoneDTO expectedDTO = new MilestoneDTO();

        when(milestoneRepository.findByStatus(status)).thenReturn(new com.roadmap.backendapi.entity.Milestone());
        when(milestoneMapper.toDTO(any())).thenReturn(expectedDTO);

        // Act
        MilestoneDTO result = milestoneService.getMilestoneByStatus(statusString);

        // Assert
        assertEquals(expectedDTO, result);
        verify(milestoneRepository).findByStatus(status);
        verify(milestoneMapper).toDTO(any());
        verifyNoMoreInteractions(milestoneRepository, milestoneMapper);
    }

    /**
     * Test that getMilestoneByStatus throws an IllegalArgumentException when given an invalid status string.
     * This tests the edge case where the input status string is not a valid MilestoneStatus enum value.
     */
    @Test
    public void test_getMilestoneByStatus_invalidStatusString() {
        MilestoneServiceImpl milestoneService = new MilestoneServiceImpl(null, null, null, null);
        MilestoneNotFoundException exception = assertThrows(MilestoneNotFoundException.class, () -> {
            milestoneService.getMilestoneByStatus("INVALID_STATUS");
        });
        assertEquals("MILESTONE_NOT_FOUND", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verifyNoInteractions(milestoneRepository, milestoneMapper);
    }

    /**
     * Tests the behavior of getMilestoneByStatus when no milestone is found for the given status.
     * This test verifies that a MilestoneNotFoundException is thrown when the repository returns null.
     */
    @Test
    public void test_getMilestoneByStatus_whenMilestoneNotFound_throwsException() {
        MilestoneStatus status = MilestoneStatus.IN_PROGRESS;
        when(milestoneRepository.findByStatus(status)).thenReturn(null);
        MilestoneNotFoundException exception = assertThrows(MilestoneNotFoundException.class, () -> {
            milestoneService.getMilestoneByStatus(status);
        });
        assertEquals("MILESTONE_NOT_FOUND", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(milestoneRepository).findByStatus(status);
        verifyNoMoreInteractions(milestoneRepository);
    }

    /**
     * Test case for getMilestoneByTitle when the milestone is not found.
     * This test verifies that a MilestoneNotFoundException is thrown when
     * attempting to retrieve a milestone with a title that doesn't exist in the repository.
     */
    @Test
    public void test_getMilestoneByTitle_nonExistentMilestone() {
        String nonExistentTitle = "Non-existent Milestone";
        when(milestoneRepository.findByTitle(nonExistentTitle)).thenReturn(null);

         MilestoneNotFoundException exception = assertThrows(MilestoneNotFoundException.class, () -> {
            milestoneService.getMilestoneByTitle(nonExistentTitle);
        });

         assertEquals("MILESTONE_NOT_FOUND", exception.getMessage());
         assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        verify(milestoneRepository).findByTitle(nonExistentTitle);
        verifyNoMoreInteractions(milestoneRepository);
    }

    /**
     * Test case for getMilestoneByTitle method when the milestone exists.
     * It verifies that the method returns the correct MilestoneDTO when a milestone with the given title is found.
     */
    @Test
    public void test_getMilestoneByTitle_whenMilestoneExists() {
        // Arrange
        String title = "Test Milestone";
        Milestone milestone = new Milestone();
        milestone.setTitle(title);
        MilestoneDTO milestoneDTO = new MilestoneDTO();
        milestoneDTO.setTitle(title);

        when(milestoneRepository.findByTitle(title)).thenReturn(milestone);
        when(milestoneMapper.toDTO(milestone)).thenReturn(milestoneDTO);

        // Act
        MilestoneDTO result = milestoneService.getMilestoneByTitle(title);

        // Assert
        assertNotNull(result);
        assertEquals(title, result.getTitle());
        verify(milestoneRepository).findByTitle(title);
        verify(milestoneMapper).toDTO(milestone);
        verifyNoMoreInteractions(milestoneRepository, milestoneMapper);
    }

    /**
     * Tests the updateMilestones method when a ResourceAccessException is thrown by the ChatClient.
     * This test verifies that a ConnectionErrorException is thrown when there's a connection issue with the ChatClient.
     */
    @Test
    public void test_updateMilestones_throwsConnectionErrorException() {
        // Arrange
        Roadmap roadmap = new Roadmap();
        roadmap.setId(1L);
        roadmap.setTitle("Test Roadmap");
        roadmap.setDescription("Test Description");
        User user = new User();
        user.setGoal("Test Goal");
        user.setInterests("Test Interests");
        user.setSkills("Test Skills");
        roadmap.setUser(user);

        when(chatClient.prompt((String) any())).thenThrow(ResourceAccessException.class);

        // Act & Assert
        ConnectionErrorException exception =assertThrows(ConnectionErrorException.class, ()
                -> milestoneService.updateMilestones(roadmap));

        assertEquals("Your Connection Is Unstable, Try Again Later", exception.getMessage());
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, exception.getStatus());

        // Verify
        verify(chatClient).prompt((String) any());
        verify(milestoneRepository, never()).deleteAllByRoadmapId(any());
        verify(milestoneRepository, never()).saveAll(any());
    }

    /**
     * Test that updateMilestones throws ConnectionErrorException when ChatClient throws ResourceAccessException.
     * This test verifies that the method properly handles network connectivity issues
     * when trying to generate milestones using the ChatClient.
     */
    @Test
    public void test_updateMilestones_throwsConnectionErrorException_whenChatClientFails() {
        // Arrange
        MilestoneServiceImpl milestoneService = new MilestoneServiceImpl(milestoneRepository, null, chatClient, resourceService);

        Roadmap roadmap = new Roadmap();
        User user= User.builder().goal("Software").skills("Java").skills("c++").build();
        roadmap.setUser(user);
        when(chatClient.prompt(anyString())).thenThrow(ResourceAccessException.class);

        // Act & Assert
        ConnectionErrorException exception = assertThrows(ConnectionErrorException.class, () -> {
            milestoneService.updateMilestones(roadmap);
        });
        assertEquals("Your Connection Is Unstable, Try Again Later", exception.getMessage());
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, exception.getStatus());
        verify(chatClient).prompt(anyString());
        verifyNoInteractions(resourceService,milestoneRepository);
    }

}
