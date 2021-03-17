package pl.makary.service.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.makary.entity.Role;
import pl.makary.entity.User;
import pl.makary.exception.EmailTakenException;
import pl.makary.exception.IncorrectPasswordException;
import pl.makary.exception.UsernameTakenException;
import pl.makary.exception.ValidationException;
import pl.makary.model.CreateUserRequest;
import pl.makary.model.DeleteUserRequest;
import pl.makary.repository.RoleRepository;
import pl.makary.repository.UserRepository;
import pl.makary.service.UserService;

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
        return userRepository.findByUsername(username);
    }

    @Override
    public void saveNewUser(CreateUserRequest createUserRequest) throws ValidationException {
        User userToSave = new User();
        userToSave.setId(null);
        userToSave.setUsername(createUserRequest.getUsername());
        userToSave.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        userToSave.setEmail(createUserRequest.getEmail());
        userToSave.setStatus(1);
        Role userRole = roleRepository.findByName("ROLE_USER");
        userToSave.setRoles(new HashSet<>(Arrays.asList(userRole)));

        if (userRepository.existsByUsername(userToSave.getUsername())) {
            throw new UsernameTakenException();
        }
        if (userRepository.existsByEmail(userToSave.getEmail())) {
            throw new EmailTakenException();
        }

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
        if(!passwordEncoder.matches(deleteUserRequest.getPassword(), user.getPassword())){
            throw new IncorrectPasswordException();
        }
        user.setStatus(0);
        userRepository.save(user);
    }


}