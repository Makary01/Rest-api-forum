package pl.makary.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue
    @Column(unique = true)
    private UUID id;

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