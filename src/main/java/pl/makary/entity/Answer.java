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
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @CreatedBy
    @ManyToOne
    private User author;

    @NotNull
    @ManyToOne
    private Post post;

    @NotNull
    @Size(min=10, max = 10000)
    private String content;

    @NotNull
    private Integer rating;

    @NotNull
    @CreatedDate
    private LocalDateTime created;

    private LocalDateTime edited;
}