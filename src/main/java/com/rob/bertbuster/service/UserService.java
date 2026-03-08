package com.rob.bertbuster.service;

import com.rob.bertbuster.domain.entity.dto.RegisterUserDto;
import com.rob.bertbuster.domain.entity.dto.UserRegistrationResponseDto;

public interface UserService {

    UserRegistrationResponseDto registerUser(RegisterUserDto registerUserDto);


}
