package pl.makary.service;

import org.springframework.data.domain.Pageable;
import pl.makary.entity.Post;
import pl.makary.entity.Section;
import pl.makary.entity.User;
import pl.makary.exception.ValidationException;
import pl.makary.model.post.CreatePostRequest;
import pl.makary.model.post.EditPostRequest;
import pl.makary.model.post.PageOfPostsResponse;

import java.util.Optional;
import java.util.UUID;

public interface PostService {
    void saveNewPost(User user, CreatePostRequest createPostRequest) throws ValidationException;

    Optional<Post> findById(UUID id);

    Post findByTitle(String title);

    void delete(Post post);

    void edit(Post post, EditPostRequest editPostRequest) throws ValidationException;

    void upvote(Post post, User user) throws ValidationException;
    void downvote(Post post, User user) throws ValidationException;

    PageOfPostsResponse readPageOfPosts(Pageable pageRequest);

    PageOfPostsResponse readPageOfPostsBySection(Pageable pageRequest, Section section);


    PageOfPostsResponse readPageOfPostsByUser(Pageable pageRequest, User user);

    PageOfPostsResponse readPageOfPostsByUserAndBySection(Pageable pageRequest, User user, Section section);
}
