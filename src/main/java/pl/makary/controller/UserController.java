package pl.makary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import pl.makary.entity.User;
import pl.makary.exception.IncorrectPasswordException;
import pl.makary.exception.UniqueValueException;
import pl.makary.model.CreateUserRequest;
import pl.makary.model.EditUserRequest;
import pl.makary.service.UserService;
import pl.makary.util.CurrentUser;

import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("")
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserRequest createUserRequest, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors= result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity
                    .badRequest()
                    .body(errors);
        }

        try {
            userService.saveNewUser(createUserRequest);
        } catch (UniqueValueException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getError());
        }

        return ResponseEntity.ok("created new user");
    }

    @PutMapping("")
    public ResponseEntity<?> editUser(@AuthenticationPrincipal CurrentUser currentUser,
                                      @RequestBody @Valid EditUserRequest editUserRequest, BindingResult result){
        if (result.hasErrors()) {
            Map<String, String> errors= result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity
                    .badRequest()
                    .body(errors);
        }

        try {
            User user = currentUser.getUser();
            userService.editUser(user,editUserRequest);
        } catch (UniqueValueException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getError());
        } catch (IncorrectPasswordException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getError());
        }
        return ResponseEntity.ok("edited current user");
    }

}
