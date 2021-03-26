package pl.makary.service.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.makary.entity.Role;
import pl.makary.entity.User;
import pl.makary.exception.EmailTakenException;
import pl.makary.exception.IncorrectPasswordException;
import pl.makary.exception.UsernameTakenException;
import pl.makary.exception.ValidationException;
import pl.makary.model.user.*;
import pl.makary.repository.RoleRepository;
import pl.makary.repository.UserRepository;
import pl.makary.service.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User findByUserName(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) updateLastOnline(user);
        return user;
    }

    @Override
    public void saveNewUser(CreateUserRequest createUserRequest) throws ValidationException {
        User userToSave = new User();

        setDefaultUserFields(userToSave);

        userToSave.setUsername(createUserRequest.getUsername());
        if (userRepository.existsByUsername(userToSave.getUsername())) {
            throw new UsernameTakenException();
        }

        userToSave.setEmail(createUserRequest.getEmail());
        if (userRepository.existsByEmail(userToSave.getEmail())) {
            throw new EmailTakenException();
        }

        userToSave.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));

        userRepository.save(userToSave);
    }

    @Override
    public void delete(User user, DeleteUserRequest deleteUserRequest) throws ValidationException {
        if (!passwordEncoder.matches(deleteUserRequest.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        } else {
            userRepository.delete(user);
        }
    }

    @Override
    public void changeUsername(User user, ChangeUsernameRequest changeUsernameRequest) throws ValidationException {
        if (!passwordEncoder.matches(changeUsernameRequest.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }
        if (userRepository.existsByUsername(changeUsernameRequest.getUsername())) {
            throw new UsernameTakenException();
        }
        user.setUsername(changeUsernameRequest.getUsername());
        userRepository.save(user);
    }

    @Override
    public void changeEmail(User user, ChangeEmailRequest changeEmailRequest) throws ValidationException {
        if (!passwordEncoder.matches(changeEmailRequest.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }
        if (userRepository.existsByEmail(changeEmailRequest.getEmail())) {
            throw new EmailTakenException();
        }
        user.setEmail(changeEmailRequest.getEmail());
        userRepository.save(user);
    }

    @Override
    public void changePassword(User user, ChangePasswordRequest changePasswordRequest) throws ValidationException {
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        } else {
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            userRepository.save(user);
        }
    }

    private void setDefaultUserFields(User user) {
        user.setId(null);
        user.setStatus(1);
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        user.setCreated(LocalDateTime.now());
        user.setLastOnline(LocalDateTime.now());
    }

    private void updateLastOnline(User user) {
        user.setLastOnline(LocalDateTime.now());
        userRepository.save(user);

    }

}