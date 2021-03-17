package pl.makary.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min = 5, max = 64)
    private String Title;

    @NotNull
    @Size(min = 30, max = 63206)
    private String content;

    @NotNull
    @CreatedBy
    private User author;

    @NotNull
    private Section section;

    @NotNull
    @CreatedDate
    private LocalDateTime created;

    private LocalDateTime edited;

    @NotNull
    private Integer rating;

    @NotNull
    private Integer popularity;

    private Answer bestAnswer;

    @NotNull
    private Integer status;
}
