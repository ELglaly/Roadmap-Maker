package com.roadmap.backendapi.security;

import com.roadmap.backendapi.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * UserDetails class implements Spring Security's UserDetails interface.
 * It represents the authenticated user's details, including username, password,
 * email, and authorities (roles).
 *
 * @see org.springframework.security.core.userdetails.UserDetails
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Configuration
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {
    private String email;
    private Long id;
    private String username;
    private String password;
    private boolean enabled;
    private Collection<GrantedAuthority> authorities;


    /**
     * Converts a User entity to UserDetails.
     *
     * @param user the User entity
     * @return UserDetails object
     */

    public static UserDetails userBuilder(User user) {

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));

        return UserDetails.builder()
                .username(user.getUsername())
                .password(user.getUserSecurity().getPasswordHash())
                .email(user.getUserContact().getEmail())
                .id(user.getId())
                .enabled(user.getUserSecurity().isEnabled())
                .authorities(authorities)
                .build();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
