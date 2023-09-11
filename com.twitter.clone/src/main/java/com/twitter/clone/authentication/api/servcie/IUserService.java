package com.twitter.clone.authentication.api.servcie;

import com.twitter.clone.authentication.api.dto.AuthenticationDto;
import com.twitter.clone.authentication.api.dto.UserDto;
import com.twitter.clone.authentication.domain.exception.InvalidCredentialException;
import com.twitter.clone.authentication.domain.exception.UserAlreadyExistException;

import java.util.List;

public interface IUserService {
    UserDto validateLogin(AuthenticationDto.LoginDto loginDto) throws InvalidCredentialException;

    UserDto createNewUser(AuthenticationDto.SignupDto signupDto) throws UserAlreadyExistException;

    UserDto findUser( int userId);

    List<UserDto> search(String searchInput);
}
