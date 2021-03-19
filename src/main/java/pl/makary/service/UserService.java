package pl.makary.service;

import pl.makary.entity.User;
import pl.makary.exception.ValidationException;
import pl.makary.model.user.CreateUserRequest;
import pl.makary.model.user.DeleteUserRequest;


public interface UserService {

    User findByUserName(String name);

    void saveNewUser(CreateUserRequest createUserRequest) throws ValidationException;

    void delete(User user, DeleteUserRequest deleteUserRequest) throws ValidationException;
//
//    void editUser(User user, EditUserRequest editUserRequest) throws UniqueValueException, IncorrectPasswordException;
//
}
