package pl.makary.model.post;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostResponse {
    private String title;
    private String content;
    private LocalDateTime created;
    private LocalDateTime edited;
    private Integer rating;
    private String section;
    private String author;


}
