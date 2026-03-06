package com.rob.bertbuster.mapper.impl;

import com.rob.bertbuster.domain.entity.User;
import com.rob.bertbuster.domain.entity.dto.RegisterUserDto;
import com.rob.bertbuster.domain.entity.dto.UserRegistrationResponseDto;
import com.rob.bertbuster.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapperImpl(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserRegistrationResponseDto userToDto(User user) {
        UserRegistrationResponseDto userRegistrationResponseDto = new UserRegistrationResponseDto(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );

        return userRegistrationResponseDto;
    }


    @Override
    public User userFromDto(RegisterUserDto registerUserDto) {
        User user = new User();
        user.setUsername(registerUserDto.username());
        user.setPassword(passwordEncoder.encode(registerUserDto.password()));
        user.setRole(registerUserDto.role() != null && !registerUserDto.role().isBlank() ? registerUserDto.role() : "USER"); //use role provided in DTO otherwise use "USER" default. All new users will be role "USER" for now.);

        return user;
    }
}
