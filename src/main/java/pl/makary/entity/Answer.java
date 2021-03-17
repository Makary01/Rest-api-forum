package pl.makary.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
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
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @CreatedBy
    private User author;

    @NotNull
    private Post post;

    @NotNull
    @Size(min=10, max = 63206)
    private String content;

    @NotNull
    private Integer rating;

    @NotNull
    @CreatedDate
    private LocalDateTime created;


    private LocalDateTime edited;
}