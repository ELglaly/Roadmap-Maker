package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.ProgressDTO;
import com.roadmap.backendapi.model.Progress;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component

public class ProgressMapper {

    private final ModelMapper modelMapper;

    public ProgressMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProgressDTO toDTO(Progress progress) {
        return modelMapper.map(progress, ProgressDTO.class);
    }
    public Progress toEntity(ProgressDTO progressDTO) {
        return modelMapper.map(progressDTO, Progress.class);
    }
}
