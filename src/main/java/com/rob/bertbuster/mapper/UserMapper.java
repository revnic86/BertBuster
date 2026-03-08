package com.rob.bertbuster.mapper;

import com.rob.bertbuster.domain.entity.User;
import com.rob.bertbuster.domain.entity.dto.RegisterUserDto;
import com.rob.bertbuster.domain.entity.dto.UserRegistrationResponseDto;

public interface UserMapper {

    UserRegistrationResponseDto userToDto(User user);

    User userFromDto(RegisterUserDto registerUserDto);
}
