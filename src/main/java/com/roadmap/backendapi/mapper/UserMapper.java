package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.UserDTO;

import com.roadmap.backendapi.entity.user.User;
import com.roadmap.backendapi.request.user.UserCreateDTO;
import com.roadmap.backendapi.request.user.UserUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting User entities to UserDTOs.
 * This interface uses MapStruct to generate the implementation at compile time.
 * The generated implementation will be a Spring component.
 *
 * @see com.roadmap.backendapi.entity.user.User
 * @see UserDTO
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "userContact.email", target = "email")
    @Mapping(source = "userContact.phoneNumber", target = "phone")
    @Mapping(source = "userContact.address", target = "address")
    @Mapping(target = "id", ignore = true) // ID is generated, not from DTO
    UserDTO toDTO(User user);

    @Mapping(source = "email", target = "userContact.email")
    @Mapping(source = "phoneNumberDto", target = "userContact.phoneNumber")
    @Mapping(source = "addressDto", target = "userContact.address")
    @Mapping(source = "passwordHash", target = "userSecurity.passwordHash")
    @Mapping(target = "id", ignore = true) // ID is generated, not from DTO
    @Mapping(target = "roadmaps", ignore = true) // Roadmaps are managed separately
    @Mapping(target = "role", constant = "USER") // Default role
    User toEntity(UserCreateDTO userCreateDto);

    @Mapping(target = "id", ignore = true) // ID is generated, not from DTO
    User toEntity(UserUpdateDTO userUpdateDto);

}