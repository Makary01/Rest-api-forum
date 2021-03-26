package pl.makary.model.post;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class PostResponse {
    private UUID id;
    private String title;
    private String content;
    private LocalDateTime created;
    private LocalDateTime edited;
    private Integer rating;
    private String section;
    private String author;


}
