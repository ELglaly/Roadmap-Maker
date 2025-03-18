package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.ResourceDTO;
import com.roadmap.backendapi.model.Resource;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component

public class ResourceMapper  implements IResourceMapper{
    private final ModelMapper modelMapper;

    public ResourceMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ResourceDTO toDTO(Resource resource) {
        return modelMapper.map(resource, ResourceDTO.class);
    }

    @Override
    public Resource toEntity(ResourceDTO resourceDTO) {
        return modelMapper.map(resourceDTO, Resource.class);
    }
}
