package pl.makary.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min=10, max = 155)
    private String title;

    @NotNull
    @Size(max=1024)
    private String description;

    @NotNull
    @CreatedDate
    private LocalDateTime created;

    @NotNull
    private Integer popularity;
}
