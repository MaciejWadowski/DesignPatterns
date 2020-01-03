package agh.dp.database;

import agh.dp.models.User;

import java.util.List;

public interface UserService {
    User findByUserName(String lastName);
    List<User> findAllByUserNameLike(String lastName);
}
