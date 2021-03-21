package pl.makary.service;

import pl.makary.entity.User;
import pl.makary.exception.ValidationException;
import pl.makary.model.user.*;


public interface UserService {

    User findByUserName(String name);

    void saveNewUser(CreateUserRequest createUserRequest) throws ValidationException;

    void delete(User user, DeleteUserRequest deleteUserRequest) throws ValidationException;

    void changeUsername(User user, ChangeUsernameRequest changeUsernameRequest) throws ValidationException;

    void changeEmail(User user, ChangeEmailRequest changeEmailRequest) throws ValidationException;

    void changePassword(User user, ChangePasswordRequest changePasswordRequest) throws ValidationException;
}
