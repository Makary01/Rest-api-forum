package pl.makary.model.post;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnswerModel {
    private String content;
    private Integer rating;
    private AuthorModel author;
    private LocalDateTime created;
    private LocalDateTime edited;
}
