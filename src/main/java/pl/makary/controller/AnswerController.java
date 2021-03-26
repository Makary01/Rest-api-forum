package pl.makary.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.makary.entity.Answer;
import pl.makary.entity.Comment;
import pl.makary.exception.ValidationException;
import pl.makary.model.Answer.AnswerList;
import pl.makary.model.Answer.AnswerModel;
import pl.makary.model.Comment.CommentList;
import pl.makary.model.Comment.CommentModel;
import pl.makary.service.AnswerService;
import pl.makary.service.CommentService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/answer")
public class AnswerController {

    private final AnswerService answerService;
    private final CommentService commentService;

    public AnswerController(AnswerService answerService, CommentService commentService) {
        this.answerService = answerService;
        this.commentService = commentService;
    }


    @GetMapping("/{postId:\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b}")
    public ResponseEntity<?> readPostAnswers(@PathVariable UUID postId, @RequestParam(name = "page", defaultValue = "1")String page){
        Integer commentPageNumber;
        try{
            commentPageNumber = Integer.parseInt(page);
            if(commentPageNumber<=0) commentPageNumber = 1;
        }catch (NumberFormatException e){
            commentPageNumber = 1;
        }
        Pageable pageRequest = PageRequest.of(commentPageNumber-1,20);
        try {
            Page<Answer> answerPage = answerService.findAllByPost(postId,pageRequest);
            return ResponseEntity.ok(generateAnswerListFromAnswerPage(answerPage));
        } catch (ValidationException e) {
            return e.generateErrorResponse();
        }
    }

    private AnswerList generateAnswerListFromAnswerPage(Page<Answer> answerPage) {
        AnswerList answerList = new AnswerList();
        List<AnswerModel> answers = new ArrayList<>();
        for (Answer answer : answerPage.getContent()) {
            answers.add(generateAnswerModel(answer));
        }
        answerList.setAnswers(answers);
        answerList.setPageNumber(answerPage.getNumber()+1);
        answerList.getTotalPages();
        return answerList;
    }

    private AnswerModel generateAnswerModel(Answer answer){
        AnswerModel answerModel = new AnswerModel();
        answerModel.setAuthor(answer.getAuthor().getUsername());
        answerModel.setContent(answer.getContent());
        answerModel.setCreated(answer.getCreated());
        answerModel.setId(answer.getId());
        answerModel.setRating(answer.getRating());

        CommentList commentList = new CommentList();
        Pageable pageRequest = PageRequest.of(1,50);
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

    private CommentModel generateCommentModelFromComment(Comment comment) {
        CommentModel commentModel = new CommentModel();
        commentModel.setId(comment.getId());
        commentModel.setContent(comment.getContent());
        commentModel.setAuthor(comment.getAuthor().getUsername());
        commentModel.setRating(comment.getRating());
        commentModel.setCreated(comment.getCreated());
        return commentModel;
    }
}
