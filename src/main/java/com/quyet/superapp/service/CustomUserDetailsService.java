package com.quyet.superapp.service;

import com.quyet.superapp.config.jwt.UserPrincipal;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameWithRole(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));

        String rawRole = user.getRole().getName();           // e.g., "Staff"
        String roleName = rawRole.toUpperCase().trim();      // => "STAFF"

        System.out.println("🧠 Role granted: ROLE_" + roleName); // debug

        return new UserPrincipal(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + roleName)),
                user.isEnable()
        );
    }


}
