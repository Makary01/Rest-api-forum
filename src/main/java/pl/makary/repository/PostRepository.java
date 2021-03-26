package pl.makary.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.makary.entity.Post;
import pl.makary.entity.Role;
import pl.makary.entity.Section;
import pl.makary.entity.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    Optional<Post> findById(UUID id);

    Post findByTitle(String title);

    Page<Post> findAllBySection(Section section, Pageable pageable);


    boolean existsByTitle(String title);

    Page<Post> findAllByAuthor(User author, Pageable pageRequest);

    Page<Post> findAllByAuthorAndSection(User author, Section section, Pageable pageRequest);
}
