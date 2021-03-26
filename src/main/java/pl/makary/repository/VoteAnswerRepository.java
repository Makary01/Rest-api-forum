package pl.makary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.makary.entity.Answer;
import pl.makary.entity.User;
import pl.makary.entity.VoteAnswer;

import java.util.Optional;

@Repository
public interface VoteAnswerRepository extends JpaRepository<VoteAnswer, Long> {
    Optional<VoteAnswer> findByUserAndAnswer(User user, Answer answer);
}
