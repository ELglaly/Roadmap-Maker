package com.roadmap.backendapi.dto;
import com.roadmap.backendapi.entity.enums.MilestoneStatus;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.util.List;

/**
 * Data Transfer Object (DTO) for Milestone.
 * This class is used to transfer data between the application and the client.
 * It contains fields that represent the properties of a Milestone.
 *
 * @see MilestoneStatus
 * @see com.roadmap.backendapi.entity.Milestone
 */


@Data
@Builder
@EqualsAndHashCode
public class MilestoneDTO {

    private Long id;

    private String title;

    private String description;

    private Timestamp dueDate;

    private String actionableSteps;

    private String prerequisites;

    private MilestoneStatus status;


    private List<ResourceDTO> resourcesDTO;

    private List<ProgressDTO> progressDTOS;

}


