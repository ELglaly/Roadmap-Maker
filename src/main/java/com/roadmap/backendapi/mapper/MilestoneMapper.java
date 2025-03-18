package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.MilestoneDTO;
import com.roadmap.backendapi.model.Milestone;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component

public class MilestoneMapper {
  private final ModelMapper modelMapper;

    public MilestoneMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public MilestoneDTO toDTO(Milestone milestone) {
    return modelMapper.map(milestone, MilestoneDTO.class);
  }
  public Milestone toEntity(MilestoneDTO milestoneDTO) {
    return modelMapper.map(milestoneDTO, Milestone.class);
  }
}
