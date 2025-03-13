package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.ProgressDTO;
import com.roadmap.backendapi.model.Progress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProgressMapper {
    ProgressMapper INSTANCE = Mappers.getMapper(ProgressMapper.class);

    @Mapping(target = "milestoneDTO", source = "milestone")
    @Mapping(target = "userDTO", source = "user")
    ProgressDTO toDTO(Progress progress);
}
