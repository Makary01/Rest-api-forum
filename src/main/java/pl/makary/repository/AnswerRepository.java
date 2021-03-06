package pl.makary.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.makary.entity.Answer;
import pl.makary.entity.Post;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, UUID> {

    Page<Answer> findAllByPost(Post post, Pageable pageable);

    Optional<Answer> findByPostAndIsBest(Post post,boolean isBest);

}
