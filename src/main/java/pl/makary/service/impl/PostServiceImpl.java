package pl.makary.service.impl;

import org.springframework.stereotype.Service;
import pl.makary.entity.Post;
import pl.makary.entity.Section;
import pl.makary.entity.User;
import pl.makary.exception.IncorrectSectionNameException;
import pl.makary.exception.ValidationException;
import pl.makary.model.post.CreatePostRequest;
import pl.makary.repository.PostRepository;
import pl.makary.repository.SectionRepository;
import pl.makary.service.PostService;

import java.time.LocalDateTime;

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
        if(section==null){
            throw new IncorrectSectionNameException();
        }

        setDefaultPostFields(post);
        post.setAuthor(user);
        post.setSection(section);
        post.setTitle(createPostRequest.getTitle());
        post.setContent(createPostRequest.getContent());

        postRepository.save(post);
    }

    private void setDefaultPostFields(Post post){
        post.setCreated(LocalDateTime.now());
        post.setStatus(1);
        post.setBestAnswer(null);
        post.setEdited(null);
        post.setRating(0);
        post.setPopularity(100);
        post.setId(null);
    }
}
