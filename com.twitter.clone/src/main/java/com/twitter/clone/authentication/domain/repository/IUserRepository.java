package com.twitter.clone.authentication.domain.repository;

import com.twitter.clone.authentication.domain.entity.User;

import java.util.List;

public interface IUserRepository {

    User findUser(int userId);

    User findUserLogin(String email, String password);

    User findUser(String email, String userName);

    void createNewUser(String email, String userName, String password);

    List<User> search(String searchInput);
}
