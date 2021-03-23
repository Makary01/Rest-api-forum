package pl.makary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.makary.entity.Post;
import pl.makary.entity.User;
import pl.makary.entity.VotePost;

public interface VotePostRepository extends JpaRepository<VotePost, Long> {

    boolean existsByUserAndPost(User user,Post post);

}
