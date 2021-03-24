package pl.makary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.makary.entity.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section,Long> {
    Section findByName(String name);

    boolean existsByName(String name);
}
