package pl.makary.model.comment;

import lombok.Data;

import java.util.List;

@Data
public class CommentList {
    private List<CommentModel> comments;
    private Integer pageNumber;
    private Integer totalPages;
}
