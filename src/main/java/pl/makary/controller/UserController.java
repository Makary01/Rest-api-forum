package pl.makary.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import pl.makary.entity.User;
import pl.makary.exception.ValidationException;
import pl.makary.model.user.*;
import pl.makary.model.OkResponse;
import pl.makary.service.UserService;
import pl.makary.util.CurrentUser;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController extends Controller{

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    @ApiOperation(value = "Creates new user", response = OkResponse.class)
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserRequest createUserRequest, BindingResult result) {

        if (result.hasErrors()) return generateResponseFromBindingResult(result);

        try {
            userService.saveNewUser(createUserRequest);
            return generateOkResponse("Registered new user");
        } catch (ValidationException e) {
            return e.generateErrorResponse();
        }
    }


    @DeleteMapping
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal CurrentUser currentUser,
                                        @RequestBody @Valid DeleteUserRequest deleteUserRequest, BindingResult result) {

        if (result.hasErrors()) return generateResponseFromBindingResult(result);

        try {
            userService.delete(currentUser.getUser(), deleteUserRequest);
            return generateOkResponse("Deleted user");
        } catch (ValidationException e) {
            return e.generateErrorResponse();
        }
    }

    @GetMapping("/{username:\\S{4,14}}")
    public ResponseEntity<?> readUser(@PathVariable String username){
        Optional<User> userOptional = Optional.ofNullable(userService.findByUserName(username));
        return userOptional.isPresent() ? generateUserResponse(userOptional.get()) : generateNotFoundResponse("User not found");
    }

    @GetMapping
    @ApiOperation(value = "Reads currently logged user", response = OkResponse.class)
    public ResponseEntity<?> readYourself(@AuthenticationPrincipal CurrentUser currentUser){
        return generateUserResponse(currentUser.getUser());
    }

    @PutMapping("/change-username")
    public ResponseEntity<?> changeUsername(@AuthenticationPrincipal CurrentUser currentUser,
                                            @RequestBody @Valid ChangeUsernameRequest changeUsernameRequest, BindingResult result){
        if (result.hasErrors()) return generateResponseFromBindingResult(result);

        try {
            userService.changeUsername(currentUser.getUser(), changeUsernameRequest);
            return generateOkResponse("Changed username");
        } catch (ValidationException e) {
            return e.generateErrorResponse();
        }
    }

    @PutMapping("/change-email")
    public ResponseEntity<?> changeEmail(@AuthenticationPrincipal CurrentUser currentUser,
                                         @RequestBody @Valid ChangeEmailRequest changeEmailRequest, BindingResult result){
        if (result.hasErrors()) return generateResponseFromBindingResult(result);

        try {
            userService.changeEmail(currentUser.getUser(), changeEmailRequest);
            return generateOkResponse("Changed email");
        } catch (ValidationException e) {
            return e.generateErrorResponse();
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal CurrentUser currentUser,
                                            @RequestBody @Valid ChangePasswordRequest changePasswordRequest, BindingResult result){
        if (result.hasErrors()) return generateResponseFromBindingResult(result);

        try {
            userService.changePassword(currentUser.getUser(), changePasswordRequest);
            return generateOkResponse("Changed password");
        } catch (ValidationException e) {
            return e.generateErrorResponse();
        }
    }


    private ResponseEntity<ReadUserResponse> generateUserResponse(User user){
        ReadUserResponse userResponse = new ReadUserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setCreated(user.getCreated());
        userResponse.setLastOnline(user.getLastOnline());
        return ResponseEntity.ok(userResponse);
    }


}
