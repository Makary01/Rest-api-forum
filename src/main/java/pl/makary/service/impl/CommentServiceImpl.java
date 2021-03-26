package pl.makary.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.makary.entity.Answer;
import pl.makary.entity.Comment;
import pl.makary.entity.User;
import pl.makary.model.Comment.AddCommentRequest;
import pl.makary.repository.CommentRepository;
import pl.makary.service.CommentService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Page<Comment> findAllByAnswer(Answer answer, Pageable pageRequest) {
        return commentRepository.findAllByAnswer(answer,pageRequest);
    }

    @Override
    public void saveNewComment(User user, AddCommentRequest addCommentRequest,Answer answer) {
        Comment comment = new Comment();
        comment.setAnswer(answer);
        comment.setAuthor(user);
        comment.setContent(addCommentRequest.getContent());
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);
    }

    @Override
    public Optional<Comment> findById(UUID commentId) {
        return commentRepository.findById(commentId);
    }

    @Override
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }
}
