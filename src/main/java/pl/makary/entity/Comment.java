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
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @CreatedBy
    private User author;

    @NotNull
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