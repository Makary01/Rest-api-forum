package pl.makary.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.makary.entity.Post;
import pl.makary.entity.Section;
import pl.makary.entity.User;
import pl.makary.exception.IncorrectSectionNameException;
import pl.makary.exception.ValidationException;
import pl.makary.model.post.CreatePostRequest;
import pl.makary.repository.PostRepository;
import pl.makary.repository.SectionRepository;
import pl.makary.repository.VotePostRepository;

import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private VotePostRepository votePostRepository;

    private PostServiceImpl postService;

    private Post validPost;

    private User validAuthor;

    @BeforeEach
    void init(){
        postService = new PostServiceImpl(postRepository,sectionRepository,votePostRepository);
        validPost = new Post();
        validPost.setTitle("testTitle");
        validPost.setContent("testContent testContent testContent testContent");
        Section section = new Section();
        section.setId(1L);
        section.setName("sectionName");
        section.setDescription("sectionDescription");
        section.setCreated(LocalDateTime.now());
        section.setPopularity(150);
        validPost.setSection(section);
        validPost.setCreated(LocalDateTime.now());
        validAuthor = new User();
        validAuthor.setUsername("testUsername");
        validPost.setAuthor(validAuthor);
    }

    @Test
    @DisplayName("Should save new post")
    void shouldSaveNewPost() {
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setTitle("Some title");
        createPostRequest.setContent("Some content some content some content some content");
        createPostRequest.setSectionName("Some section");
        when(sectionRepository.findByName(createPostRequest.getSectionName()))
                .thenReturn(new Section());
        try{
            postService.saveNewPost(validAuthor, createPostRequest);
            verify(sectionRepository).findByName("Some section");
            verify(postRepository).save(any());
        } catch (ValidationException e) {
            fail();
        }
    }
    @Test
    @DisplayName("Should fail saving new post incorrect section name")
    void shouldFailSavingNewPostIncorrectSectionName() {
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setTitle("Some title");
        createPostRequest.setContent("Some content some content some content some content");
        createPostRequest.setSectionName("Some section");
        when(sectionRepository.findByName(createPostRequest.getSectionName()))
                .thenReturn(null);
        assertThrows(IncorrectSectionNameException.class,()-> postService.saveNewPost(validAuthor,createPostRequest));

    }
}