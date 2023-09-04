package com.twitter.clone.authentication.domain.repository;

import com.twitter.clone.authentication.domain.entity.User;

public interface IUserRepository {

    User findUserLogin(String email, String password);

    User findUser(String email, String userName);

    void createNewUser(String email, String userName, String password);
}
