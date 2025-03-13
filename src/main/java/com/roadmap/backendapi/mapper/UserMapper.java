package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.UserDTO;
import com.roadmap.backendapi.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    @Mapping(target = "roadmapsDTO", source = "roadmaps")
    @Mapping(target = "progressDTO", source = "progress")
    UserDTO toDTO(User user);
}