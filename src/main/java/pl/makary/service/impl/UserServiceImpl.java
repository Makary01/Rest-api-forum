package pl.makary.service.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.makary.entity.Role;
import pl.makary.entity.User;
import pl.makary.exception.EmailTakenException;
import pl.makary.exception.IncorrectPasswordException;
import pl.makary.exception.UsernameTakenException;
import pl.makary.exception.ValidationException;
import pl.makary.model.user.CreateUserRequest;
import pl.makary.model.user.DeleteUserRequest;
import pl.makary.repository.RoleRepository;
import pl.makary.repository.UserRepository;
import pl.makary.service.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

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

    //
//    @Override
//    public void editUser(User user, EditUserRequest editUserRequest) throws UniqueValueException, IncorrectPasswordException {
//        if (!passwordEncoder.matches(editUserRequest.getPassword(), user.getPassword())) {
//            throw new IncorrectPasswordException();
//        }
//        if (userRepository.existsByUsername(editUserRequest.getUsername())) {
//            if (userRepository.findByUsername(editUserRequest.getUsername()).getId() != user.getId())
//                throw new UniqueValueException("username", "username already taken");
//        }
//        if (userRepository.existsByEmail(editUserRequest.getEmail())) {
//            if (userRepository.findByEmail(editUserRequest.getEmail()).getId() != user.getId())
//                throw new UniqueValueException("email", "email already taken");
//        }
//
//        user.setUsername(editUserRequest.getUsername());
//        user.setPassword(passwordEncoder.encode(editUserRequest.getPassword()));
//        user.setEmail(editUserRequest.getEmail());
//
//        userRepository.save(user);
//
//
//    }
//
    @Override
    public void delete(User user, DeleteUserRequest deleteUserRequest) throws ValidationException {
        if (!passwordEncoder.matches(deleteUserRequest.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        } else {
            userRepository.delete(user);
        }
    }





    private void setDefaultUserFields(User user){
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