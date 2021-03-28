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
import pl.makary.exception.ValidationException;
import pl.makary.model.answer.AddAnswerRequest;
import pl.makary.model.answer.AnswerList;
import pl.makary.model.answer.AnswerModel;
import pl.makary.model.answer.EditAnswerRequest;
import pl.makary.model.comment.CommentList;
import pl.makary.model.comment.CommentModel;
import pl.makary.service.AnswerService;
import pl.makary.service.CommentService;
import pl.makary.service.PostService;
import pl.makary.util.CurrentUser;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/answer")
public class AnswerController extends Controller{

    private final AnswerService answerService;
    private final CommentService commentService;
    private final PostService postService;

    public AnswerController(AnswerService answerService, CommentService commentService, PostService postService) {
        this.answerService = answerService;
        this.commentService = commentService;
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<?> addAnswer(@AuthenticationPrincipal CurrentUser currentUser,
                                       @Valid @RequestBody AddAnswerRequest addAnswerRequest, BindingResult result){
        if(result.hasErrors()) return generateResponseFromBindingResult(result);

        try{
            answerService.saveNewAnswer(currentUser.getUser(),addAnswerRequest);
            return generateOkResponse("Added answer");
        }catch (ValidationException e){
            return e.generateErrorResponse();
        }
    }

    @GetMapping("/{postId:\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b}")
    public ResponseEntity<?> readPostAnswers(@PathVariable UUID postId,
                                             @RequestParam(name = "page", defaultValue = "1")String page,
                                             @RequestParam(name = "sortCommentsBy", defaultValue = "rating")String sortCommentsBy,
                                             @RequestParam(name = "sortAnswersBy", defaultValue = "rating")String sortAnswersBy){
        Integer answerPageNumber;
        try{
            answerPageNumber = Integer.parseInt(page);
            if(answerPageNumber<=0) answerPageNumber = 1;
        }catch (NumberFormatException e){
            answerPageNumber = 1;
        }
        if(!sortCommentsBy.equals("rating")&&!sortCommentsBy.equals("created")){
            sortCommentsBy ="rating";
        }
        if(!sortAnswersBy.equals("rating")&&!sortAnswersBy.equals("created")){
            sortAnswersBy ="rating";
        }



        Pageable answersPageRequest = PageRequest.of(answerPageNumber-1,20,Sort.by("isBest").descending().and(Sort.by(sortAnswersBy).descending()));

        try {
            Page<Answer> answerPage = answerService.readPageByPost(postId,answersPageRequest);
            return ResponseEntity.ok(generateAnswerListFromAnswerPage(answerPage, sortCommentsBy));
        } catch (ValidationException e) {
            return e.generateErrorResponse();
        }
    }

    @PutMapping("/{id:\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b}")
    public ResponseEntity<?> editAnswer(@PathVariable UUID id,
                                        @AuthenticationPrincipal CurrentUser currentUser,
                                        @Valid @RequestBody EditAnswerRequest editAnswerRequest,BindingResult result) {

        if(result.hasErrors()) return generateResponseFromBindingResult(result);

        Optional<Answer> answerOptional = answerService.findById(id);
        if(answerOptional.isPresent()){
            if(answerOptional.get().getAuthor().getId()!=currentUser.getUser().getId()){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }else {
            return ResponseEntity.notFound().build();
        }
        answerService.editAnswer(id, editAnswerRequest);
        return generateOkResponse("Edited answer");
    }

    @DeleteMapping("/{id:\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b}")
    public ResponseEntity<?> deleteAnswer(@PathVariable UUID id,
                                          @AuthenticationPrincipal CurrentUser currentUser){
        Optional<Answer> answerOptional = answerService.findById(id);
        if(answerOptional.isPresent()){
            if(answerOptional.get().getAuthor().getId()!=currentUser.getUser().getId()){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }else {
            return ResponseEntity.notFound().build();
        }
        answerService.deleteAnswer(id);
        return generateOkResponse("Edited answer");
    }

    @PostMapping("/{id:\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b}/upvote")
    public ResponseEntity<?> upvoteAnswer(@AuthenticationPrincipal CurrentUser currentUser,
                                          @PathVariable UUID id){
        Optional<Answer> answerOptional = answerService.findById(id);
        if(!answerOptional.isPresent()) return ResponseEntity.notFound().build();

        answerService.upvoteAnswer(currentUser.getUser(),answerOptional.get());
        return generateOkResponse("Upvoted post");
    }

    @PostMapping("/{id:\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b}/downvote")
    public ResponseEntity<?> downvoteAnswer(@AuthenticationPrincipal CurrentUser currentUser,
                                            @PathVariable UUID id){
        Optional<Answer> answerOptional = answerService.findById(id);
        if(!answerOptional.isPresent()) return ResponseEntity.notFound().build();

        answerService.downvoteAnswer(currentUser.getUser(),answerOptional.get());
        return generateOkResponse("Downvoted post");
    }


    private AnswerList generateAnswerListFromAnswerPage(Page<Answer> answerPage, String sortBy) {
        AnswerList answerList = new AnswerList();
        List<AnswerModel> answers = new ArrayList<>();
        for (Answer answer : answerPage.getContent()) {
            answers.add(generateAnswerModel(answer,sortBy));
        }
        answerList.setAnswers(answers);
        answerList.setPageNumber(answerPage.getNumber()+1);
        answerList.setTotalPages(answerPage.getTotalPages());
        return answerList;
    }

    private AnswerModel generateAnswerModel(Answer answer, String sortBy){
        AnswerModel answerModel = new AnswerModel();
        answerModel.setAuthor(answer.getAuthor().getUsername());
        answerModel.setContent(answer.getContent());
        answerModel.setCreated(answer.getCreated());
        answerModel.setId(answer.getId());
        answerModel.setRating(answer.getRating());
        answerModel.setBest(answer.isBest());

        CommentList commentList = new CommentList();
        Pageable pageRequest = PageRequest.of(0,50, Sort.by(sortBy).descending());
        Page<Comment> commentPage = commentService.findAllByAnswer(answer, pageRequest);
        List<CommentModel> comments = new ArrayList<>();
        for (Comment comment : commentPage.getContent()) {
            comments.add(generateCommentModelFromComment(comment));
        }
        commentList.setComments(comments);
        commentList.setTotalPages(commentPage.getTotalPages());
        commentList.setPageNumber(commentPage.getNumber()+1);
        answerModel.setComments(commentList);
        return answerModel;
    }
}
