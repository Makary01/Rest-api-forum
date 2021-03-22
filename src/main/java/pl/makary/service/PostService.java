package pl.makary.service;

import pl.makary.entity.User;
import pl.makary.exception.ValidationException;
import pl.makary.model.post.CreatePostRequest;

public interface PostService {
    void saveNewPost(User user, CreatePostRequest createPostRequest) throws ValidationException;

}
