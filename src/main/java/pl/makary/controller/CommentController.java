package pl.makary.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.makary.entity.Answer;
import pl.makary.entity.Comment;
import pl.makary.model.Comment.AddCommentRequest;
import pl.makary.service.AnswerService;
import pl.makary.service.CommentService;
import pl.makary.util.CurrentUser;

import javax.validation.Valid;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/comment")
public class CommentController extends Controller{

    private final CommentService commentService;
    private final AnswerService answerService;


    public CommentController(CommentService commentService, AnswerService answerService) {
        this.commentService = commentService;
        this.answerService = answerService;
    }

    @PostMapping("/{answerId:\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b}")
    public ResponseEntity<?> addComment(@AuthenticationPrincipal CurrentUser currentUser,
                                        @PathVariable UUID answerId,
                                        @Valid @RequestBody AddCommentRequest addCommentRequest, BindingResult result){
        if(result.hasErrors()) generateResponseFromBindingResult(result);

        Optional<Answer> answerOptional = answerService.findById(answerId);
        if (answerOptional.isPresent()) {
            commentService.saveNewComment(currentUser.getUser(),addCommentRequest,answerOptional.get());
            return generateOkResponse("Added comment");
        }else {
            return generateNotFoundResponse("Answer not found");
        }
    }

    @DeleteMapping("/{commentId:\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b}")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal CurrentUser currentUser,
                                           @PathVariable UUID commentId){
        Optional<Comment> commentOptional = commentService.findById(commentId);
        if (commentOptional.isPresent()) {
            if(commentOptional.get().getAuthor().getId()==currentUser.getUser().getId()){
                commentService.deleteComment(commentOptional.get());
                return generateOkResponse("Deleted comment");
            }else {
                return generateForbiddenResponse("Unauthorized");
            }
        }else {
            return generateNotFoundResponse("Comment not found");
        }
    }
}
