package com.twitter.clone.authentication.api.servcie;

import com.twitter.clone.authentication.api.dto.AuthenticationDto;
import com.twitter.clone.authentication.api.dto.UserDto;
import com.twitter.clone.authentication.domain.exception.UserAlreadyExistException;

public interface IUserService {
    UserDto validateLogin(AuthenticationDto.LoginDto loginDto);

    UserDto createNewUser(AuthenticationDto.SignupDto signupDto) throws UserAlreadyExistException;
}
