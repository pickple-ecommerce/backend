package com.pickple.auth.application.security;

import com.pickple.auth.application.domain.model.User;
import com.pickple.auth.application.dto.UserDto;
import com.pickple.auth.infrastructure.feign.UserServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserServiceClient userServiceClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDto userDto = userServiceClient.getUserByUsername(username);
        User user = User.convertToUser(userDto);
        return new UserDetailsImpl(user);
    }
}
