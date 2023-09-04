package com.twitter.clone.authentication.domain.service;

import com.google.inject.Inject;
import com.twitter.clone.authentication.api.dto.AuthenticationDto;
import com.twitter.clone.authentication.api.dto.UserDto;
import com.twitter.clone.authentication.api.mapper.UserMapper;
import com.twitter.clone.authentication.api.servcie.IUserService;
import com.twitter.clone.authentication.domain.exception.UserAlreadyExistException;
import com.twitter.clone.authentication.domain.repository.IUserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final UserMapper mapper;

    @Override
    public UserDto validateLogin(AuthenticationDto.LoginDto loginDto) {
        var user = userRepository.findUserLogin(loginDto.email(), loginDto.password());
        return mapper.userToUserDto(user);
    }

    @Override
    public UserDto createNewUser(AuthenticationDto.SignupDto signupDto) throws UserAlreadyExistException {
        var user = userRepository.findUser(signupDto.email(), signupDto.UserName());
        if (user != null) {
            throw new UserAlreadyExistException("User already exists");
        }
        userRepository.createNewUser(signupDto.email(), signupDto.UserName(), signupDto.password());
        var newuser = userRepository.findUser(signupDto.email(), signupDto.UserName());
        return mapper.userToUserDto(user);
    }
}
