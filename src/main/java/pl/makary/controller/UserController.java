package pl.makary.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import pl.makary.exception.ValidationException;
import pl.makary.model.user.CreateUserRequest;
import pl.makary.model.user.DeleteUserRequest;
import pl.makary.model.OkResponse;
import pl.makary.service.UserService;
import pl.makary.util.CurrentUser;

import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserRequest createUserRequest, BindingResult result) {

        if (result.hasErrors()) return generateResponseFromBindingResult(result);

        try {
            userService.saveNewUser(createUserRequest);
        } catch (ValidationException e) {
            return e.generateErrorResponse();
        }

        return generateOkResponse("Registered new user");
    }


    @DeleteMapping
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal CurrentUser currentUser,
                                        @RequestBody @Valid DeleteUserRequest deleteUserRequest, BindingResult result) {

        if (result.hasErrors()) return generateResponseFromBindingResult(result);

        try {
            userService.delete(currentUser.getUser(), deleteUserRequest);
        } catch (ValidationException e) {
            return e.generateErrorResponse();
        }

        return generateOkResponse("Deleted user");
    }

    private ResponseEntity<?> generateResponseFromBindingResult(BindingResult result) {
        Map<String, String> errors = result.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    private ResponseEntity<?> generateOkResponse(String message){
        return ResponseEntity.ok(new OkResponse(message));
    }



    //    @PutMapping("")
//    public ResponseEntity<?> editUser(@AuthenticationPrincipal CurrentUser currentUser,
//                                      @RequestBody @Valid EditUserRequest editUserRequest, BindingResult result){
//        if (result.hasErrors()) {
//            Map<String, String> errors= result.getFieldErrors().stream()
//                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
//            return ResponseEntity
//                    .badRequest()
//                    .body(errors);
//        }
//
//        try {
//            User user = currentUser.getUser();
//            userService.editUser(user,editUserRequest);
//        } catch (UniqueValueException e) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(e.getError());
//        } catch (IncorrectPasswordException e) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(e.getError());
//        }
//        return ResponseEntity.ok("edited current user");
//    }
//
//    @PutMapping("/change-password")
//    public ResponseEntity<?> changePassword(@AuthenticationPrincipal CurrentUser currentUser,
//                                        @RequestBody @Valid DeleteUserRequest deleteUserRequest, BindingResult result) {
//
//        if (result.hasErrors()) return generateResponseFromBindingResult(result);
//
//        try {
//            userService.delete(currentUser.getUser(), deleteUserRequest);
//        } catch (ValidationException e) {
//            return e.generateErrorResponse();
//        }
//
//        return generateOkResponse("Deleted user");
//    }



}
