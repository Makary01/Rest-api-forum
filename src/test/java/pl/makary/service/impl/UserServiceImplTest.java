package pl.makary.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.makary.entity.Role;
import pl.makary.entity.User;
import pl.makary.exception.EmailTakenException;
import pl.makary.exception.IncorrectPasswordException;
import pl.makary.exception.UsernameTakenException;
import pl.makary.exception.ValidationException;
import pl.makary.model.user.*;
import pl.makary.repository.RoleRepository;
import pl.makary.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private UserServiceImpl userService;

    private User validUser;

    @BeforeEach
    void init() {
        this.userService = new UserServiceImpl(userRepository, roleRepository, passwordEncoder);
        validUser = new User();
        validUser.setId(1L);
        validUser.setStatus(1);
        validUser.setUsername("testUser");
        validUser.setEmail("test@email.com");
        validUser.setPassword("testPassword");
        validUser.setCreated(LocalDateTime.now());
        validUser.setLastOnline(LocalDateTime.now());
        Role role = new Role();
        role.setName("ROLE_USER");
        role.setId(1L);
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        validUser.setRoles(roleSet);
    }

    @Test
    @DisplayName("Should save new user")
    public void shouldSaveNewUser() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testUsername");
        createUserRequest.setEmail("test@email.com");
        createUserRequest.setPassword("testPassword");

        Role role = new Role();
        role.setName("ROLE_USER");
        role.setId(1L);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
        when(userRepository.existsByUsername("testUsername")).thenReturn(false);
        when(userRepository.existsByEmail("test@email.com")).thenReturn(false);

        try {
            userService.saveNewUser(createUserRequest);
            verify(userRepository).save(any());
            verify(passwordEncoder).encode("testPassword");
        } catch (ValidationException e) {
            fail();
        }
    }
    @Test
    @DisplayName("Should fail saving new user")
    public void shouldFailSaveNewUserUsernameTaken() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testUsername");
        createUserRequest.setEmail("test@email.com");
        createUserRequest.setPassword("testPassword");

        Role role = new Role();
        role.setName("ROLE_USER");
        role.setId(1L);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
        when(userRepository.existsByUsername("testUsername")).thenReturn(true);


        assertThrows(UsernameTakenException.class, () -> userService.saveNewUser(createUserRequest));

    }
    @Test
    @DisplayName("Should fail saving new user")
    public void shouldFailSaveNewUserEmailTaken() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testUsername");
        createUserRequest.setEmail("test@email.com");
        createUserRequest.setPassword("testPassword");

        Role role = new Role();
        role.setName("ROLE_USER");
        role.setId(1L);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
        when(userRepository.existsByUsername("testUsername")).thenReturn(false);
        when(userRepository.existsByEmail("test@email.com")).thenReturn(true);

        assertThrows(EmailTakenException.class, () -> userService.saveNewUser(createUserRequest));

    }


    @Test
    @DisplayName("Should delete user")
    void shouldDeleteUser() {
        DeleteUserRequest deleteUserRequest = new DeleteUserRequest();
        deleteUserRequest.setPassword("testPassword");
        when(passwordEncoder.matches(validUser.getPassword(),deleteUserRequest.getPassword()))
                .thenReturn(true);
        try{
            userService.delete(validUser,deleteUserRequest);
            verify(userRepository).delete(any());
            verify(passwordEncoder).matches(validUser.getPassword(),deleteUserRequest.getPassword());
        } catch (ValidationException e) {
            fail();
        }
    }
    @Test
    @DisplayName("Should fail deleting user, wrong password")
    void shouldFailDeletingUserWrongPassword() {
        DeleteUserRequest deleteUserRequest = new DeleteUserRequest();
        deleteUserRequest.setPassword("testPassword");
        when(passwordEncoder.matches(validUser.getPassword(),deleteUserRequest.getPassword()))
                .thenReturn(false);
        assertThrows(IncorrectPasswordException.class, () -> userService.delete(validUser,deleteUserRequest));
    }


    @Test
    @DisplayName("Should change username")
    void shouldChangeUsername() {
        ChangeUsernameRequest changeUsernameRequest = new ChangeUsernameRequest();
        changeUsernameRequest.setUsername("testUsername2");
        changeUsernameRequest.setPassword("testPassword");
        when(passwordEncoder.matches(changeUsernameRequest.getPassword(), validUser.getPassword()))
                .thenReturn(true);
        when(userRepository.existsByUsername(changeUsernameRequest.getUsername()))
                .thenReturn(false);
        try {
            userService.changeUsername(validUser, changeUsernameRequest);
            verify(userRepository).save(any());
            verify(userRepository).existsByUsername(changeUsernameRequest.getUsername());
            verify(passwordEncoder).matches(changeUsernameRequest.getPassword(), validUser.getPassword());
        } catch (ValidationException e) {
            fail();
        }
    }
    @Test
    @DisplayName("Should fail changing username, wrong password")
    void shouldFailChangingUsernameWrongPassword() {
        ChangeUsernameRequest changeUsernameRequest = new ChangeUsernameRequest();
        changeUsernameRequest.setUsername("testUsername2");
        changeUsernameRequest.setPassword("testPassword");
        when(passwordEncoder.matches(changeUsernameRequest.getPassword(), validUser.getPassword()))
                .thenReturn(false);
        assertThrows(IncorrectPasswordException.class, () -> userService.changeUsername(validUser, changeUsernameRequest));
    }
    @Test
    @DisplayName("Should fail changing username, username taken")
    void shouldFailChangingUsernameUsernameTaken() {
        ChangeUsernameRequest changeUsernameRequest = new ChangeUsernameRequest();
        changeUsernameRequest.setUsername("testUsername2");
        changeUsernameRequest.setPassword("testPassword");
        when(passwordEncoder.matches(changeUsernameRequest.getPassword(), validUser.getPassword()))
                .thenReturn(true);
        when(userRepository.existsByUsername(changeUsernameRequest.getUsername()))
                .thenReturn(true);
        assertThrows(UsernameTakenException.class, () -> userService.changeUsername(validUser, changeUsernameRequest));
    }


    @Test
    @DisplayName("Should change email")
    void shouldChangeEmail() {
        ChangeEmailRequest changeEmailRequest = new ChangeEmailRequest();
        changeEmailRequest.setEmail("test2@email.com");
        changeEmailRequest.setPassword("testPassword");
        when(passwordEncoder.matches(changeEmailRequest.getPassword(), validUser.getPassword()))
                .thenReturn(true);
        when(userRepository.existsByEmail(changeEmailRequest.getEmail()))
                .thenReturn(false);
        try {
            userService.changeEmail(validUser, changeEmailRequest);
            verify(userRepository).save(any());
            verify(userRepository).existsByEmail(changeEmailRequest.getEmail());
            verify(passwordEncoder).matches(changeEmailRequest.getPassword(), validUser.getPassword());
        } catch (ValidationException e) {
            fail();
        }

    }
    @Test
    @DisplayName("Should fail saving email, wrong password")
    void shouldFailSavingEmailWrongPassword() {
        ChangeEmailRequest changeEmailRequest = new ChangeEmailRequest();
        changeEmailRequest.setEmail("test2@email.com");
        changeEmailRequest.setPassword("testPassword");
        when(passwordEncoder.matches(changeEmailRequest.getPassword(), validUser.getPassword()))
                .thenReturn(false);
        assertThrows(IncorrectPasswordException.class,()->userService.changeEmail(validUser,changeEmailRequest));

    }

    @Test
    @DisplayName("Should fail saving email, email taken")
    void shouldFailSavingEmailEmailTaken() {
        ChangeEmailRequest changeEmailRequest = new ChangeEmailRequest();
        changeEmailRequest.setEmail("test2@email.com");
        changeEmailRequest.setPassword("testPassword");
        when(passwordEncoder.matches(changeEmailRequest.getPassword(), validUser.getPassword()))
                .thenReturn(true);
        when(userRepository.existsByEmail(changeEmailRequest.getEmail()))
                .thenReturn(true);
        assertThrows(EmailTakenException.class, () -> userService.changeEmail(validUser, changeEmailRequest));

    }


    @Test
    @DisplayName("Should change password")
    void shouldChangePassword() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setNewPassword("testNewPassword");
        changePasswordRequest.setOldPassword("testPassword");
        when(passwordEncoder.matches(changePasswordRequest.getOldPassword(), validUser.getPassword()))
                .thenReturn(true);
        try {
            userService.changePassword(validUser, changePasswordRequest);
            verify(passwordEncoder).matches(changePasswordRequest.getOldPassword(),"testPassword");
            verify(passwordEncoder).encode(changePasswordRequest.getNewPassword());
            verify(userRepository).save(any());
        } catch (ValidationException e) {
            fail();
        }
    }
    @Test
    @DisplayName("Should fail changing password, incorrect password")
    void shouldFailChangingPasswordIncorrectPassword() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setNewPassword("testNewPassword");
        changePasswordRequest.setOldPassword("testPassword");
        when(passwordEncoder.matches(changePasswordRequest.getOldPassword(), validUser.getPassword()))
                .thenReturn(false);
        assertThrows(IncorrectPasswordException.class, ()->userService.changePassword(validUser,changePasswordRequest));

    }
}