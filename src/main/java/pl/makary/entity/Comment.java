package pl.makary.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @CreatedBy
    @ManyToOne
    private User author;

    @NotNull
    @ManyToOne
    private Answer answer;

    @NotNull
    @Size(min = 8, max = 8000)
    private String content;

    @NotNull
    @CreatedDate
    private LocalDateTime created;

    @NotNull
    private Integer rating;
}