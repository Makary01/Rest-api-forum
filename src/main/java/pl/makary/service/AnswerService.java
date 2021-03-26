package pl.makary.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.makary.entity.Answer;
import pl.makary.entity.User;
import pl.makary.exception.ValidationException;
import pl.makary.model.Answer.AddAnswerRequest;
import pl.makary.model.Answer.EditAnswerRequest;

import java.util.Optional;
import java.util.UUID;

public interface AnswerService {
    Page<Answer> findAllByPost(UUID postId, Pageable pageable) throws ValidationException;

    void saveNewAnswer(User user, AddAnswerRequest addAnswerRequest) throws ValidationException;

    void editAnswer(UUID postId, EditAnswerRequest editAnswerRequest);

    Optional<Answer> findById(UUID id);

    void deleteAnswer(UUID id);

    void upvoteAnswer(User user, Answer answer);

    void downvoteAnswer(User user, Answer answer);
}
