package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.ProgressDTO;
import com.roadmap.backendapi.entity.Progress;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


/**
 * Mapper interface for converting Progress entities to ProgressDTOs.
 * This interface uses MapStruct to generate the implementation at compile time.
 * The generated implementation will be a Spring component.
 *
 * @see com.roadmap.backendapi.entity.Progress
 * @see com.roadmap.backendapi.dto.ProgressDTO
 */
@Mapper(componentModel = "spring")
public interface ProgressMapper {

    ProgressMapper INSTANCE = Mappers.getMapper(ProgressMapper.class);

    ProgressDTO toDTO(Progress progress);

}
