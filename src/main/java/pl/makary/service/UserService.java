package pl.makary.service;

import pl.makary.entity.User;

public interface UserService {

    User findByUserName(String name);

    void saveUser(User user);
}
