package pl.makary.service.impl;

import org.springframework.stereotype.Service;
import pl.makary.entity.*;
import pl.makary.exception.IncorrectSectionNameException;
import pl.makary.exception.ValidationException;
import pl.makary.model.post.CreatePostRequest;
import pl.makary.model.post.EditPostRequest;
import pl.makary.repository.PostRepository;
import pl.makary.repository.SectionRepository;
import pl.makary.service.PostService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final SectionRepository sectionRepository;

    public PostServiceImpl(PostRepository postRepository, SectionRepository sectionRepository) {
        this.postRepository = postRepository;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public void saveNewPost(User user, CreatePostRequest createPostRequest) throws ValidationException {
        Post post = new Post();
        Section section = sectionRepository.findByName(createPostRequest.getSectionName());
        if (section == null) throw new IncorrectSectionNameException();


        setDefaultPostFields(post);
        post.setAuthor(user);
        post.setSection(section);
        post.setTitle(createPostRequest.getTitle());
        post.setContent(createPostRequest.getContent());

        postRepository.save(post);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public void delete(Post post) {
        postRepository.delete(post);
    }

    @Override
    public void edit(Post post, EditPostRequest editPostRequest) throws ValidationException {
        Section section = sectionRepository.findByName(editPostRequest.getSectionName());
        if (null == section) throw new IncorrectSectionNameException();

        post.setSection(section);
        post.setContent(editPostRequest.getContent());
        post.setTitle(editPostRequest.getTitle());
        post.setEdited(LocalDateTime.now());
        postRepository.save(post);

    }

    @Override
    public void upvote(Post post, User user) throws ValidationException {


        VotePost votePost = new VotePost();
        votePost.setPost(post);
        votePost.setUser(user);
        votePost.setPositive(true);

    }

    @Override
    public void downvote(Post post, User user) throws ValidationException {

    }

    private void setDefaultPostFields(Post post) {
        post.setCreated(LocalDateTime.now());
        post.setStatus(1);
        post.setEdited(null);
        post.setRating(0);
        post.setPopularity(100);
        post.setId(null);
    }
}
