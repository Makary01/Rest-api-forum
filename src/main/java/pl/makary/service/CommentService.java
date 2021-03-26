package pl.makary.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.makary.entity.Answer;
import pl.makary.entity.Comment;

public interface CommentService {
    Page<Comment> findAllByAnswer(Answer answer, Pageable pageRequest);
}
