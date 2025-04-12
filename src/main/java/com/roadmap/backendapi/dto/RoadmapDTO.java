package com.roadmap.backendapi.dto;

import lombok.*;

import java.util.List;

/**
 * Data Transfer Object (DTO) for Roadmap.
 * This class is used to transfer data between the application and the client.
 * It contains fields that represent the properties of a Roadmap.
 *
 * @see com.roadmap.backendapi.entity.Roadmap
 */

@Builder
@EqualsAndHashCode
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoadmapDTO {

    private Long id;
    private String title;
    private String description;
    private List<MilestoneDTO> milestonesDTO;

}
