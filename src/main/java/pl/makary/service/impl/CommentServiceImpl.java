package pl.makary.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.makary.entity.*;
import pl.makary.model.Comment.AddCommentRequest;
import pl.makary.repository.CommentRepository;
import pl.makary.repository.VoteCommentRepository;
import pl.makary.service.CommentService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final VoteCommentRepository voteCommentRepository;

    public CommentServiceImpl(CommentRepository commentRepository, VoteCommentRepository voteCommentRepository) {
        this.commentRepository = commentRepository;
        this.voteCommentRepository = voteCommentRepository;
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

    @Override
    public void upvoteComment(User user, Comment comment) {
        Optional<VoteComment> vote = voteCommentRepository.findByUserAndComment(user, comment);
        if(vote.isPresent()){
            if(vote.get().isPositive()){
                return;
            }else {
                vote.get().setPositive(true);
                voteCommentRepository.save(vote.get());
                comment.setRating(comment.getRating()+2);
                commentRepository.save(comment);
            }
        }else {
            VoteComment voteComment = new VoteComment();
            voteComment.setComment(comment);
            voteComment.setUser(user);
            voteComment.setPositive(true);
            voteCommentRepository.save(voteComment);
            comment.setRating(comment.getRating()+1);
            commentRepository.save(comment);
        }

    }

    @Override
    public void downvoteComment(User user, Comment comment) {
        Optional<VoteComment> vote = voteCommentRepository.findByUserAndComment(user, comment);
        if(vote.isPresent()){
            if(vote.get().isPositive()){
                vote.get().setPositive(false);
                voteCommentRepository.save(vote.get());
                comment.setRating(comment.getRating()-2);
                commentRepository.save(comment);
            }else {
                return;
            }
        }else {
            VoteComment voteComment = new VoteComment();
            voteComment.setComment(comment);
            voteComment.setUser(user);
            voteComment.setPositive(false);
            voteCommentRepository.save(voteComment);
            comment.setRating(comment.getRating()-1);
            commentRepository.save(comment);
        }
    }
}
