package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.UserDTO;
import com.roadmap.backendapi.entity.User;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toDTO(User user);


}