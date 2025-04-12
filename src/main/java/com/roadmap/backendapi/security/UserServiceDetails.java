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


/** * UserServiceDetails is a service class that implements Spring Security's
 * UserDetailsService interface. It is responsible for loading user-specific data
 * during authentication and authorization processes.
 *
 * @see org.springframework.security.core.userdetails.UserDetailsService
 * @see com.roadmap.backendapi.entity.User
 */
import java.util.Optional;
@Service
public class UserServiceDetails implements UserDetailsService {
    private final UserRepository userRepository;

    public UserServiceDetails(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * Loads user details by username.
     *
     * @param username the username of the user
     * @return UserDetails object containing user information
     * @throws UsernameNotFoundException if the user is not found
     */
    @Cacheable(value = "userCache", key = "#username")
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= getUser(username);
        return UserDetails.userBuilder(user);

    }
    /**
     * Retrieves a user by username.
     *
     * @param username the username of the user
     * @return User object containing user information
     * @throws UserNotFoundException if the user is not found
     */
    public User getUser(String username) {
        return  Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(UserNotFoundException::new);
    }
}
