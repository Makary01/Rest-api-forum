package pl.makary.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.makary.entity.Answer;
import pl.makary.entity.Comment;
import pl.makary.repository.CommentRepository;
import pl.makary.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Page<Comment> findAllByAnswer(Answer answer, Pageable pageRequest) {

        return null;
    }
}
