package pl.makary.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.makary.entity.Answer;
import pl.makary.entity.Post;
import pl.makary.entity.User;
import pl.makary.entity.VoteAnswer;
import pl.makary.exception.IncorrectPostIdException;
import pl.makary.exception.ValidationException;
import pl.makary.model.Answer.AddAnswerRequest;
import pl.makary.model.Answer.EditAnswerRequest;
import pl.makary.repository.AnswerRepository;
import pl.makary.repository.PostRepository;
import pl.makary.repository.VoteAnswerRepository;
import pl.makary.service.AnswerService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final PostRepository postRepository;
    private final VoteAnswerRepository voteAnswerRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository, PostRepository postRepository, VoteAnswerRepository voteAnswerRepository) {
        this.answerRepository = answerRepository;
        this.postRepository = postRepository;
        this.voteAnswerRepository = voteAnswerRepository;
    }

    @Override
    public Page<Answer> readPageByPost(UUID postId, Pageable pageable) throws ValidationException {
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

    @Override
    public void editAnswer(UUID answerId, EditAnswerRequest editAnswerRequest) {
        Answer answer = answerRepository.getOne(answerId);
        answer.setContent(editAnswerRequest.getContent());
        answer.setEdited(LocalDateTime.now());
        answerRepository.save(answer);
    }

    @Override
    public Optional<Answer> findById(UUID id) {
        return answerRepository.findById(id);
    }

    @Override
    public void deleteAnswer(UUID id) {
        answerRepository.delete(answerRepository.getOne(id));
    }

    @Override
    public void upvoteAnswer(User user, Answer answer) {
        Optional<VoteAnswer> vote = voteAnswerRepository.findByUserAndAnswer(user, answer);
        if(vote.isPresent()){
            if(vote.get().isPositive()){
               return;
            }else {
                vote.get().setPositive(true);
                voteAnswerRepository.save(vote.get());
                answer.setRating(answer.getRating()+2);
                answerRepository.save(answer);
            }
        }else {
            VoteAnswer voteAnswer = new VoteAnswer();
            voteAnswer.setAnswer(answer);
            voteAnswer.setUser(user);
            voteAnswer.setPositive(true);
            voteAnswerRepository.save(voteAnswer);
            answer.setRating(answer.getRating()+1);
            answerRepository.save(answer);
        }
    }

    @Override
    public void downvoteAnswer(User user, Answer answer) {
        Optional<VoteAnswer> vote = voteAnswerRepository.findByUserAndAnswer(user, answer);
        if(vote.isPresent()){
            if(vote.get().isPositive()){
                vote.get().setPositive(false);
                voteAnswerRepository.save(vote.get());
                answer.setRating(answer.getRating()-2);
                answerRepository.save(answer);
            }else {
                return;
            }
        }else {
            VoteAnswer voteAnswer = new VoteAnswer();
            voteAnswer.setAnswer(answer);
            voteAnswer.setUser(user);
            voteAnswer.setPositive(false);
            voteAnswerRepository.save(voteAnswer);
            answer.setRating(answer.getRating()-1);
            answerRepository.save(answer);
        }
    }

    private void setAnswerDefaultFields(Answer answer){
        answer.setBest(false);
        answer.setId(null);
        answer.setRating(0);
        answer.setCreated(LocalDateTime.now());
        answer.setEdited(null);
    }

}
