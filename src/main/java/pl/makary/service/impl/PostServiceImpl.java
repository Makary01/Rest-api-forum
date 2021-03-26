package pl.makary.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.makary.entity.*;
import pl.makary.exception.IncorrectSectionNameException;
import pl.makary.exception.ValidationException;
import pl.makary.model.post.CreatePostRequest;
import pl.makary.model.post.EditPostRequest;
import pl.makary.model.post.PageOfPostsResponse;
import pl.makary.model.post.PostResponse;
import pl.makary.repository.PostRepository;
import pl.makary.repository.SectionRepository;
import pl.makary.repository.VotePostRepository;
import pl.makary.service.PostService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final SectionRepository sectionRepository;
    private final VotePostRepository votePostRepository;

    public PostServiceImpl(PostRepository postRepository, SectionRepository sectionRepository, VotePostRepository votePostRepository) {
        this.postRepository = postRepository;
        this.sectionRepository = sectionRepository;
        this.votePostRepository = votePostRepository;
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
    public Optional<Post> findById(UUID id) {
        return postRepository.findById(id);
    }

    @Override
    public Post findByTitle(String title) {
        return postRepository.findByTitle(title);
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
        VotePost votePost;
        if(votePostRepository.existsByUserAndPost(user,post)){
            votePost = votePostRepository.findByUserAndPost(user, post);
            if(votePost.isPositive()==false){
                post.setRating(post.getRating()+2);
            }else {
                return;
            }
        }else {
            votePost = new VotePost();
            votePost.setPost(post);
            votePost.setUser(user);
            post.setRating(post.getRating()+1);
        }
        votePost.setPositive(true);

        changePopularityByVote(votePost);


        votePostRepository.save(votePost);
    }

    @Override
    public void downvote(Post post, User user) throws ValidationException {
        VotePost votePost;
        if(votePostRepository.existsByUserAndPost(user,post)){
            votePost = votePostRepository.findByUserAndPost(user, post);
            if(votePost.isPositive()==true){
                post.setRating(post.getRating()-2);
            }else {
                return;
            }
        }else {
            votePost = new VotePost();
            votePost.setPost(post);
            votePost.setUser(user);
            post.setRating(post.getRating()-1);
        }
        votePost.setPositive(false);

        changePopularityByVote(votePost);


        votePostRepository.save(votePost);
    }

    @Override
    public PageOfPostsResponse readPageOfPosts(Pageable pageRequest) {
        Page<Post> postsPage = postRepository.findAll(pageRequest);
        return generatePageOfPostsResponse(postsPage);
    }

    @Override
    public PageOfPostsResponse readPageOfPostsBySection(Pageable pageRequest, Section section) {
        Page<Post> postsPage = postRepository.findAllBySection(section, pageRequest);
        return generatePageOfPostsResponse(postsPage);
    }

    @Override
    public PageOfPostsResponse readPageOfPostsByUser(Pageable pageRequest, User user) {
        Page<Post> postsPage = postRepository.findAllByAuthor(user, pageRequest);
        return generatePageOfPostsResponse(postsPage);
    }

    @Override
    public PageOfPostsResponse readPageOfPostsByUserAndBySection(Pageable pageRequest, User user, Section section) {
        Page<Post> postsPage = postRepository.findAllByAuthorAndSection(user,section, pageRequest);
        return generatePageOfPostsResponse(postsPage);
    }

    private PageOfPostsResponse generatePageOfPostsResponse(Page<Post> postsPage) {
        PageOfPostsResponse postsPageResponse = new PageOfPostsResponse();
        List<PostResponse> postResponseList = new ArrayList<>();

        for (Post post : postsPage.getContent()) {
            PostResponse postResponse = generatePostResponse(post);
            postResponseList.add(postResponse);
        }
        postsPageResponse.setTotalPages(postsPage.getTotalPages());
        postsPageResponse.setNumberOfPage(postsPage.getNumber()+1);
        postsPageResponse.setPosts(postResponseList);
        return postsPageResponse;
    }

    private PostResponse generatePostResponse(Post post){
        PostResponse postResponse = new PostResponse();
        postResponse.setId(post.getId());
        postResponse.setTitle(post.getTitle());
        postResponse.setContent(post.getContent());
        postResponse.setRating(post.getRating());
        postResponse.setCreated(post.getCreated());
        postResponse.setEdited(post.getEdited());
        postResponse.setAuthor(post.getAuthor().getUsername());
        postResponse.setSection(post.getSection().getName());
        return postResponse;
    }



    private void updatePostPopularity(Post post){

    }

    private void changePopularityByVote(VotePost votePost){
        if(votePost.getId()==null){
            if(votePost.isPositive()){
                firstTimeUpvote(votePost);
            }else {
                firstTimeDownvote(votePost);
            }
        }else {
            if(votePost.isPositive()){
                fromDownvoteToUpvote(votePost);
            }else {
                fromUpvoteToDownvote(votePost);
            }
        }

    }

    private void fromUpvoteToDownvote(VotePost votePost) {
        Post post = votePost.getPost();
        Integer popularity = post.getPopularity();
        post.setPopularity(popularity-8);
    }

    private void fromDownvoteToUpvote(VotePost votePost) {
        Post post = votePost.getPost();
        Integer popularity = post.getPopularity();
        post.setPopularity(popularity+8);
    }

    private void firstTimeDownvote(VotePost votePost) {
        Post post = votePost.getPost();
        Integer popularity = post.getPopularity();
        post.setPopularity(popularity+2);
    }

    private void firstTimeUpvote(VotePost votePost) {
        Post post = votePost.getPost();
        Integer popularity = post.getPopularity();
        post.setPopularity(popularity+10);
    }

    private void setDefaultPostFields(Post post) {
        post.setCreated(LocalDateTime.now());
        post.setStatus(1);
        post.setEdited(null);
        post.setRating(0);
        post.setPopularity(10);
        post.setId(null);
    }
}
