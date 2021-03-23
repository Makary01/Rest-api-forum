package pl.makary.service;

import pl.makary.entity.Post;
import pl.makary.entity.User;
import pl.makary.exception.ValidationException;
import pl.makary.model.post.CreatePostRequest;
import pl.makary.model.post.EditPostRequest;

import java.util.Optional;

public interface PostService {
    void saveNewPost(User user, CreatePostRequest createPostRequest) throws ValidationException;

    Optional<Post> findById(Long id);

    void delete(Post post);

    void edit(Post post, EditPostRequest editPostRequest) throws ValidationException;

    void upvote(Post post, User user) throws ValidationException;
    void downvote(Post post, User user) throws ValidationException;
}
