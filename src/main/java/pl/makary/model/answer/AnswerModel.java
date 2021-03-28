package pl.makary.model.answer;

import lombok.Data;
import pl.makary.model.comment.CommentList;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AnswerModel {
    private UUID id;
    private String content;
    private Integer rating;
    private String author;
    private LocalDateTime created;
    private LocalDateTime edited;
    private boolean isBest;
    private CommentList comments;
}
