package pl.makary.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.makary.entity.Answer;
import pl.makary.entity.Post;
import pl.makary.exception.IncorrectPostIdException;
import pl.makary.exception.ValidationException;
import pl.makary.repository.AnswerRepository;
import pl.makary.repository.PostRepository;
import pl.makary.service.AnswerService;

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

}
