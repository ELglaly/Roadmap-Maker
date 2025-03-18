package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.ProgressDTO;
import com.roadmap.backendapi.model.Progress;

public interface IProgressMapper {

    ProgressDTO toDTO(Progress progress);
    Progress toEntity(ProgressDTO progressDTO);
}
