package com.twitter.clone.authentication.data.repository;

import com.google.inject.Inject;
import com.twitter.clone.authentication.data.dau.IUserDAO;
import com.twitter.clone.authentication.domain.entity.User;
import com.twitter.clone.authentication.domain.repository.IUserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class UserRepository implements IUserRepository {

    private final IUserDAO userDau;

    @Override
    public User findUserLogin(String email, String password) {
        return userDau.findUserLogin(email, password);
    }

    @Override
    public User findUser(String email, String userName) {
        return userDau.findUser(email, userName);
    }

    @Override
    public void createNewUser(String email, String userName, String password) {
        userDau.insert(email,userName,password);
    }
}
