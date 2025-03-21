package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.ProgressDTO;
import com.roadmap.backendapi.entity.Progress;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface ProgressMapper {

    ProgressMapper INSTANCE = Mappers.getMapper(ProgressMapper.class);

    ProgressDTO toDTO(Progress progress);

}
