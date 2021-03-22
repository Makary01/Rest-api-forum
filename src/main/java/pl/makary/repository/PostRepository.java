package pl.makary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.makary.entity.Post;
import pl.makary.entity.Role;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {



}
