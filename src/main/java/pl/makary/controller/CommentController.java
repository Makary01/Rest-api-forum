package pl.makary.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.makary.entity.Answer;
import pl.makary.entity.Comment;
import pl.makary.model.Comment.AddCommentRequest;
import pl.makary.model.Comment.CommentList;
import pl.makary.model.Comment.CommentModel;
import pl.makary.service.AnswerService;
import pl.makary.service.CommentService;
import pl.makary.util.CurrentUser;

import javax.validation.Valid;
import java.util.*;

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

    @PostMapping("/{commentId:\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b}/upvote")
    public ResponseEntity<?> upvoteComment(@AuthenticationPrincipal CurrentUser currentUser,
                                           @PathVariable UUID commentId){
        Optional<Comment> commentOptional = commentService.findById(commentId);

        if (commentOptional.isPresent()) {
            commentService.upvoteComment(currentUser.getUser(), commentOptional.get());
            return generateOkResponse("Upvoted comment");
        }else {
            return generateNotFoundResponse("Comment not found");
        }
    }

    @PostMapping("/{commentId:\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b}/downvote")
    public ResponseEntity<?> downvoteComment(@AuthenticationPrincipal CurrentUser currentUser,
                                           @PathVariable UUID commentId){
        Optional<Comment> commentOptional = commentService.findById(commentId);

        if (commentOptional.isPresent()) {
            commentService.downvoteComment(currentUser.getUser(), commentOptional.get());
            return generateOkResponse("Downvoted comment");
        }else {
            return generateNotFoundResponse("Comment not found");
        }
    }

    @GetMapping("/{answerId:\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b}")
    public ResponseEntity<?> readMoreComments(@PathVariable UUID answerId,
                                              @RequestParam(name = "page", defaultValue = "2") String page,
                                              @RequestParam(name = "sortBy", defaultValue = "rating") String sortBy){
        Integer pageNumber;
        try{
            pageNumber = Integer.parseInt(page);
            if(pageNumber<=1) pageNumber = 2;
        }catch (NumberFormatException e){
            pageNumber = 2;
        }

        if(!sortBy.equals("rating")&&!sortBy.equals("created")){
            sortBy="rating";
        }

        Optional<Answer> answerOptional = answerService.findById(answerId);
        if(answerOptional.isPresent()){
            Pageable pageRequest = PageRequest.of(pageNumber-1,50, Sort.by(sortBy));
            Page<Comment> commentPage = commentService.findAllByAnswer(answerOptional.get(), pageRequest);

            CommentList commentList = new CommentList();
            List<CommentModel> comments = new ArrayList<>();
            for (Comment comment : commentPage.getContent()) {
                comments.add(generateCommentModelFromComment(comment));
            }
            commentList.setComments(comments);
            commentList.setTotalPages(commentPage.getTotalPages());
            commentList.setPageNumber(commentPage.getNumber()+1);
            return ResponseEntity.ok(commentList);
        }else {
            return generateNotFoundResponse("Answer not found");
        }

    }
}
