package pl.makary.service;

import pl.makary.entity.User;
import pl.makary.exception.UniqueValueException;
import pl.makary.model.CreateUserRequest;

public interface UserService {

    User findByUserName(String name);

    void saveUser(User user);

    void saveNewUser(CreateUserRequest createUserRequest) throws UniqueValueException;
}
