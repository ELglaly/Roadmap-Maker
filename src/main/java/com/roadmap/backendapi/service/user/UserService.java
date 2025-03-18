package com.roadmap.backendapi.service.user;

import com.roadmap.backendapi.dto.UserDTO;
import com.roadmap.backendapi.exception.user.UserNotFoundException;
import com.roadmap.backendapi.mapper.IUserMapper;
import com.roadmap.backendapi.mapper.UserMapper;
import com.roadmap.backendapi.model.User;
import com.roadmap.backendapi.repository.UserRepository;
import com.roadmap.backendapi.request.user.CreateUserRequest;
import com.roadmap.backendapi.request.user.UpdateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUseService {

    private final IUserMapper userMapper;
    private final   UserRepository userRepository;

    public UserService(IUserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }


    public UserDTO getUserById(Long userId) {
        User  user =userRepository.findById(userId)
                    .orElseThrow(UserNotFoundException::new);

        UserDTO userDTO = userMapper.toDTO(user);
        return userDTO;
    }

    public UserDTO getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email))
                .map(userMapper::toDTO)
                .orElseThrow(UserNotFoundException::new);
    }

    public UserDTO getUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username))
                .map(userMapper::toDTO)
                .orElseThrow(UserNotFoundException::new);
    }

    public UserDTO createUser(CreateUserRequest createUserRequest) {
        return null;
    }

    public UserDTO updateUser(UpdateUserRequest updateUserRequest) {
        return null;
    }

    public void deleteUser(Long userId) {
       Optional.of(userRepository.existsById(userId))
                .filter(Boolean::booleanValue)
                .orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(userId);
    }
}
