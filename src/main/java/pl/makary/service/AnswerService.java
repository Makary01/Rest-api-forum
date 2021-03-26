package pl.makary.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.makary.entity.Answer;
import pl.makary.exception.ValidationException;

import java.util.UUID;

public interface AnswerService {
    Page<Answer> findAllByPost(UUID postId, Pageable pageable) throws ValidationException;
}
