package com.roadmap.backendapi.mapper;

import com.roadmap.backendapi.dto.UserDTO;
import com.roadmap.backendapi.model.User;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component

public class UserMapper implements IUserMapper {


    private ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDTO toDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public User toEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}