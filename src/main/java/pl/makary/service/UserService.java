package pl.makary.service;

import pl.makary.entity.User;
import pl.makary.exception.IncorrectPasswordException;
import pl.makary.exception.UniqueValueException;
import pl.makary.model.CreateUserRequest;
import pl.makary.model.EditUserRequest;

public interface UserService {

    User findByUserName(String name);

    void saveNewUser(CreateUserRequest createUserRequest) throws UniqueValueException;

    void editUser(User user, EditUserRequest editUserRequest) throws UniqueValueException, IncorrectPasswordException;
}
