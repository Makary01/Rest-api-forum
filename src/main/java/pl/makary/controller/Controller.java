package pl.makary.controller;

import com.sun.istack.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import pl.makary.entity.Comment;
import pl.makary.model.Comment.CommentModel;
import pl.makary.model.ForbiddenResponse;
import pl.makary.model.NotFoundResponse;
import pl.makary.model.OkResponse;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class Controller {

    public ResponseEntity<?> generateResponseFromBindingResult(BindingResult result) {
        Map<String, String> errors = result.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    public ResponseEntity<?> generateOkResponse(String message){
        return ResponseEntity.ok(new OkResponse(message));
    }

    public ResponseEntity<?> generateNotFoundResponse(String message){

        NotFoundResponse notFoundResponse = new NotFoundResponse(message);
        return new ResponseEntity<>(
                notFoundResponse.getBody(),
                HttpStatus.NOT_FOUND
        );
    }

    public ResponseEntity<?> generateForbiddenResponse(String message){
        ForbiddenResponse forbiddenResponse = new ForbiddenResponse(message);
        return new ResponseEntity<>(
                forbiddenResponse.getBody(),
                HttpStatus.FORBIDDEN
        );
    }

    public CommentModel generateCommentModelFromComment(Comment comment) {
        CommentModel commentModel = new CommentModel();
        commentModel.setId(comment.getId());
        commentModel.setContent(comment.getContent());
        commentModel.setAuthor(comment.getAuthor().getUsername());
        commentModel.setRating(comment.getRating());
        commentModel.setCreated(comment.getCreated());
        return commentModel;
    }
}
