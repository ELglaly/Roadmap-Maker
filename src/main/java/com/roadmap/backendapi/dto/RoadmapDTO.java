package com.roadmap.backendapi.dto;

import lombok.*;

import java.util.List;

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
