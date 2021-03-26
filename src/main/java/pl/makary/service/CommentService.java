package pl.makary.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.makary.entity.Answer;
import pl.makary.entity.Comment;
import pl.makary.entity.User;
import pl.makary.model.Comment.AddCommentRequest;

import java.util.Optional;
import java.util.UUID;

public interface CommentService {
    Page<Comment> findAllByAnswer(Answer answer, Pageable pageRequest);

    void saveNewComment(User user, AddCommentRequest addCommentRequest, Answer answer);

    Optional<Comment> findById(UUID commentId);

    void deleteComment(Comment comment);

    void upvoteComment(User user, Comment comment);
    void downvoteComment(User user, Comment comment);
}
