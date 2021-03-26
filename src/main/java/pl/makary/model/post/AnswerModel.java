package pl.makary.model.post;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AnswerModel {
    private UUID id;
    private String content;
    private Integer rating;
    private AuthorModel author;
    private LocalDateTime created;
    private LocalDateTime edited;
}
