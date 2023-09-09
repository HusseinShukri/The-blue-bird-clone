package com.twitter.clone.authentication.data.dau;

import com.twitter.clone.authentication.domain.entity.User;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface IUserDAO {

    @SqlQuery("""
            SELECT
                u.id,
                u.username,
                u.email,
                u.password,
                u.created_at as 'createdAt',
                u.is_deleted as 'isDeleted'
            FROM
                User as u
            WHERE
                u.id = :userId
                    """)
    @RegisterBeanMapper(User.class)
    User findUser(@Bind("userId") int userId);

    @SqlQuery("""
        SELECT
            u.id,
            u.username,
            u.email,
            u.password,
            u.created_at as 'createdAt',
            u.is_deleted as 'isDeleted'
        FROM
            User as u
        WHERE
            u.email = :email
            AND u.password = :password
    """)
    @RegisterBeanMapper(User.class)
    User findUserLogin(@Bind("email") String email, @Bind("password") String password);

    @SqlQuery("""
            SELECT
            u.id,
            u.username,
            u.email,
            u.password,
            u.created_at as 'createdAt',
            u.is_deleted as 'isDeleted'
        FROM
            User as u
        WHERE
            u.email = :email
            AND u.username = :username
    """)
    @RegisterBeanMapper(User.class)
    User findUser(@Bind("email") String email, @Bind("username") String username);

    @SqlUpdate("""
               INSERT INTO `User` (username, password, email, created_at, is_deleted)
               VALUES (:userName, :password, :email, CURRENT_TIMESTAMP(), 0);
               """)
    void insert(@Bind("email") String email, @Bind("password") String password,  @Bind("userName")  String userName);

    @SqlQuery("""
            SELECT
            u.id,
            u.username,
            u.email,
            u.password,
            u.created_at as 'createdAt',
            u.is_deleted as 'isDeleted'
        FROM
            User as u
        WHERE
            u.id = :id
    """)
    @RegisterBeanMapper(User.class)
    User getUser(@Bind("id") int id);
}
