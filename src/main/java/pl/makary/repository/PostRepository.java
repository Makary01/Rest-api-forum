package pl.makary.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.makary.entity.Post;
import pl.makary.entity.Role;
import pl.makary.entity.Section;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(Long id);

    Page<Post> findAllBySection(Section section, Pageable pageable);


    boolean existsByTitle(String title);
}
