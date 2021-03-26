package pl.makary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.makary.entity.Comment;
import pl.makary.entity.User;
import pl.makary.entity.VoteComment;

import java.util.Optional;

@Repository
public interface VoteCommentRepository extends JpaRepository<VoteComment, Long> {


    Optional<VoteComment> findByUserAndComment(User user, Comment comment);
}
