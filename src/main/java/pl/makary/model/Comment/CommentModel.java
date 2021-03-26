package pl.makary.model.Comment;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CommentModel {
    private UUID id;
    private String content;
    private LocalDateTime created;
    private Integer rating;
    private String author;
}
