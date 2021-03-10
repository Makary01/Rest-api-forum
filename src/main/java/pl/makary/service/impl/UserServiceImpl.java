package pl.makary.service.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.makary.entity.Role;
import pl.makary.entity.User;
import pl.makary.exception.UniqueValueException;
import pl.makary.model.CreateUserRequest;
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
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(1);
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    @Override
    public void saveNewUser(CreateUserRequest cur) throws UniqueValueException {
        User userToSave = new User();
        userToSave.setId(null);
        userToSave.setUsername(cur.getUsername());
        userToSave.setPassword(passwordEncoder.encode(cur.getPassword()));
        userToSave.setEmail(cur.getEmail());
        userToSave.setEnabled(1);
        Role userRole = roleRepository.findByName("ROLE_USER");
        userToSave.setRoles(new HashSet<>(Arrays.asList(userRole)));

        if(userRepository.existsByUsername(userToSave.getUsername())){
            throw new UniqueValueException("username","username already taken");
        }
        if(userRepository.existsByEmail(userToSave.getEmail())){
            throw new UniqueValueException("email","email already taken");
        }

        userRepository.save(userToSave);
    }
}