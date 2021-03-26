package pl.makary.model.post;

import lombok.Data;

import java.util.List;

@Data
public class PageOfPostsResponse {

    List<PostResponse> posts;
    int numberOfPage;
    int totalPages;
}
