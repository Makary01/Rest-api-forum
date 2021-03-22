package pl.makary.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import pl.makary.exception.ValidationException;
import pl.makary.model.OkResponse;
import pl.makary.model.post.CreatePostRequest;
import pl.makary.service.PostService;
import pl.makary.util.CurrentUser;

import javax.validation.Valid;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<?> createPost(@AuthenticationPrincipal CurrentUser currentUser,
                                        @RequestBody @Valid CreatePostRequest createPostRequest, BindingResult result){
        if(result.hasErrors()) return generateResponseFromBindingResult(result);

        try {
            postService.saveNewPost(currentUser.getUser(), createPostRequest);
        }catch (ValidationException e){
            e.generateErrorResponse();
        }

        return generateOkResponse("Saved new post");
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<?> readPost(@PathVariable Long id){
        return null;
    }

    public ResponseEntity<?> editPost(){
        return null;
    }

    public ResponseEntity<?> deletePost(){
        return null;
    }

    public ResponseEntity<?> upvotePost(){
        return null;
    }

    public ResponseEntity<?> downvotePost(){
        return null;
    }

    private ResponseEntity<?> generateResponseFromBindingResult(BindingResult result) {
        Map<String, String> errors = result.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return ResponseEntity
                .badRequest()
                .body(errors);
    }
    private ResponseEntity<OkResponse> generateOkResponse(String message){
        return ResponseEntity.ok(new OkResponse(message));
    }
}
