package pl.makary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.makary.entity.Post;
import pl.makary.entity.User;
import pl.makary.entity.VotePost;
@Repository
public interface VotePostRepository extends JpaRepository<VotePost, Long> {

    boolean existsByUserAndPost(User user,Post post);

    VotePost findByUserAndPost(User user, Post post);

    Integer countAllByPost(Post post);

//    Integer countAllByPostAndIsPositive(Post post, boolean isPositive);
}
