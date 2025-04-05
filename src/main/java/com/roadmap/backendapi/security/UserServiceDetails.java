package com.roadmap.backendapi.security;

import com.roadmap.backendapi.entity.User;
import com.roadmap.backendapi.exception.user.UserNotFoundException;
import com.roadmap.backendapi.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class UserServiceDetails implements UserDetailsService {
    private final UserRepository userRepository;

    public UserServiceDetails(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Cacheable(value = "userCache", key = "#username")
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= getUser(username);
        return UserDetails.userBuilder(user);

    }

    public User getUser(String username) {
        return  Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(UserNotFoundException::new);
    }
}
