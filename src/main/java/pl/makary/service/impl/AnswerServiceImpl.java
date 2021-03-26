package pl.makary.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.makary.entity.Answer;
import pl.makary.entity.Post;
import pl.makary.entity.User;
import pl.makary.exception.IncorrectPostIdException;
import pl.makary.exception.ValidationException;
import pl.makary.model.Answer.AddAnswerRequest;
import pl.makary.repository.AnswerRepository;
import pl.makary.repository.PostRepository;
import pl.makary.service.AnswerService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final PostRepository postRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository, PostRepository postRepository) {
        this.answerRepository = answerRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Page<Answer> findAllByPost(UUID postId, Pageable pageable) throws ValidationException {
        Optional<Post> post = postRepository.findById(postId);
        if(!post.isPresent()) throw new IncorrectPostIdException();
        return answerRepository.findAllByPost(post.get(), pageable);
    }

    @Override
    public void saveNewAnswer(User user, AddAnswerRequest addAnswerRequest) throws ValidationException {
        Optional<Post> postOptional = postRepository.findById(addAnswerRequest.getPostId());
        if(!postOptional.isPresent()) throw new IncorrectPostIdException();
        Answer answer = new Answer();
        answer.setAuthor(user);
        answer.setContent(addAnswerRequest.getContent());
        setAnswerDefaultFields(answer);
        answer.setPost(postOptional.get());

        answerRepository.save(answer);
    }

    private void setAnswerDefaultFields(Answer answer){
        answer.setBest(false);
        answer.setId(null);
        answer.setRating(0);
        answer.setCreated(LocalDateTime.now());
        answer.setEdited(null);
    }

}
