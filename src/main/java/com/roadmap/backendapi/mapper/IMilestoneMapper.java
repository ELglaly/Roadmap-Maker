package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.MilestoneDTO;
import com.roadmap.backendapi.model.Milestone;
public interface IMilestoneMapper {

    MilestoneDTO toDTO(Milestone milestone);
    Milestone toEntity(MilestoneDTO milestoneDTO);
}
