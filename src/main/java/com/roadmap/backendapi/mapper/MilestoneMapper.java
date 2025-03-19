package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.MilestoneDTO;
import com.roadmap.backendapi.model.Milestone;
import lombok.RequiredArgsConstructor;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MilestoneMapper {
  MilestoneMapper INSTANCE = Mappers.getMapper(MilestoneMapper.class);

  @Mapping(target = "resourcesDTO", source = "resources")
  MilestoneDTO toDTO(Milestone milestone);

}
