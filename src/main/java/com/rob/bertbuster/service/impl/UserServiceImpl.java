package com.rob.bertbuster.service.impl;

import com.rob.bertbuster.domain.entity.User;
import com.rob.bertbuster.domain.entity.dto.RegisterUserDto;
import com.rob.bertbuster.domain.entity.dto.UserRegistrationResponseDto;
import com.rob.bertbuster.mapper.UserMapper;
import com.rob.bertbuster.repository.UserRepository;
import com.rob.bertbuster.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserRegistrationResponseDto registerUser(RegisterUserDto registerUserDto) {


        if (userRepository.findByUsername(registerUserDto.username()).isPresent()){ //basic check to see if the user already exists.
            throw new RuntimeException("Username already exists");

        }

        User user = userMapper.userFromDto(registerUserDto);

        userRepository.save(user);

        UserRegistrationResponseDto userRegistrationResponseDto = userMapper.userToDto(user);


        return userRegistrationResponseDto;

    }



}
