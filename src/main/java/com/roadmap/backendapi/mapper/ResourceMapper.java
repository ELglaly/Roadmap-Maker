package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.ResourceDTO;
import com.roadmap.backendapi.model.Resource;
import lombok.RequiredArgsConstructor;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResourceMapper {
    ResourceMapper INSTANCE = Mappers.getMapper(ResourceMapper.class);

    ResourceDTO toDTO(Resource resource);
}