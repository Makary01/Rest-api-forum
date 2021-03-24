package pl.makary.controller;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import pl.makary.entity.Answer;
import pl.makary.entity.Post;
import pl.makary.exception.ValidationException;
import pl.makary.model.OkResponse;
import pl.makary.model.post.*;
import pl.makary.service.AnswerService;
import pl.makary.service.PostService;
import pl.makary.service.SectionService;
import pl.makary.util.CurrentUser;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;
    private final AnswerService answerService;
    private final SectionService sectionService;

    public PostController(PostService postService, AnswerService answerService, SectionService sectionService) {
        this.postService = postService;
        this.answerService = answerService;
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<?> createPost(@AuthenticationPrincipal CurrentUser currentUser,
                                        @RequestBody @Valid CreatePostRequest createPostRequest, BindingResult result){
        if(result.hasErrors()) return generateResponseFromBindingResult(result);

        try {
            postService.saveNewPost(currentUser.getUser(), createPostRequest);
        }catch (ValidationException e){
            e.generateErrorResponse();
        }

        return generateOkResponse("Saved new post");
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<?> readPost(@PathVariable Long id){
        Optional<Post> postOptional = postService.findById(id);
        return postOptional.isPresent() ? generatePostResponse(postOptional.get()) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id:\\d+}")
    public ResponseEntity<?> editPost(@AuthenticationPrincipal CurrentUser currentUser,
                                      @RequestBody @Valid EditPostRequest editPostRequest, BindingResult result,
                                      @PathVariable Long id){
        if(result.hasErrors()) return generateResponseFromBindingResult(result);

        Optional<Post> post = postService.findById(id);
        if(post.isPresent()){
            if(post.get().getAuthor().getId() != currentUser.getUser().getId()){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }else {
            return ResponseEntity.notFound().build();
        }

        try{
            postService.edit(post.get(),editPostRequest);
            return generateOkResponse("Edited post");
        }catch (ValidationException e){
            return e.generateErrorResponse();
        }

    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<?> deletePost(@AuthenticationPrincipal CurrentUser currentUser, @PathVariable Long id){
        Optional<Post> postOptional = postService.findById(id);
        if(postOptional.isPresent()){
            if(postOptional.get().getAuthor().getId() == currentUser.getUser().getId()){
                postService.delete(postOptional.get());
                return generateOkResponse("Deleted post");
            }else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id:\\d+}/upvote")
    public ResponseEntity<?> upvotePost(@PathVariable Long id, @AuthenticationPrincipal CurrentUser currentUser){
        Optional<Post> postOptional = postService.findById(id);
        if(!postOptional.isPresent()) return ResponseEntity.notFound().build();

        try{
            postService.upvote(postOptional.get(),currentUser.getUser());
            return generateOkResponse("Upvoted post");
        }catch (ValidationException e){
            return e.generateErrorResponse();
        }
    }

    @PostMapping("/{id:\\d+}/downvote")
    public ResponseEntity<?> downvotePost(@PathVariable Long id, @AuthenticationPrincipal CurrentUser currentUser){
        Optional<Post> postOptional = postService.findById(id);
        if(!postOptional.isPresent()) return ResponseEntity.notFound().build();

        try{
            postService.downvote(postOptional.get(),currentUser.getUser());
            return generateOkResponse("Downvoted post");
        }catch (ValidationException e){
            return e.generateErrorResponse();
        }
    }

    @GetMapping
    public ResponseEntity<?> readPosts(@RequestParam(name = "sortBy",defaultValue = "popularity") String sortBy,
                                       @RequestParam(name = "order",defaultValue = "desc") String order,
                                       @RequestParam(name = "page", defaultValue = "1")String page,
                                       @RequestParam(name = "section", defaultValue = "any")String section,
                                       @RequestParam(name = "postsOnPage", defaultValue = "20")String postsOnPage){

        if(!validateSortBy(sortBy)) sortBy="popularity";
        if(!order.equals("asc")&&!order.equals("desc")) order = "desc";
        if(!validateSectionName(section)) section = "any";

        Integer pageNumber;
        try{
            pageNumber = Integer.parseInt(page);
            if(pageNumber<=0) pageNumber = 1;
        }catch (NumberFormatException e){
            pageNumber = 1;
        }
        Integer postsOnPageNumber;

        try{
            postsOnPageNumber = Integer.parseInt(postsOnPage);
            if(postsOnPageNumber>60||postsOnPageNumber<20) postsOnPageNumber = 20;
        }catch (NumberFormatException e){
            postsOnPageNumber = 20;
        }

        Pageable pageRequest = generatePageRequest(sortBy, order, pageNumber-1, postsOnPageNumber);
        PageOfPostsResponse postsResponse;


        if(section.equals("any")) {
            postsResponse = postService.readPageOfPosts(pageRequest);

        }else {
            postsResponse = postService.readPageOfPostsBySection(pageRequest,sectionService.findByName(section));
        }
        return ResponseEntity.ok(postsResponse);
    }

    private Pageable generatePageRequest(String sortBy, String order, Integer pageNumber, Integer postsOnPage) {

        Pageable pageRequest;
        if(order.equals("desc")){
            pageRequest = PageRequest.of(pageNumber,postsOnPage, Sort.by(sortBy).descending());
        }else {
            pageRequest = PageRequest.of(pageNumber,postsOnPage, Sort.by(sortBy).ascending());
        }

        return pageRequest;
    }

    boolean validateSectionName(String sectionName){
        if(sectionName.equals("any")) return true;
        return sectionService.existsByName(sectionName);
    }

    boolean validateSortBy(String sortBy){
        switch (sortBy){
            case "popularity":
            case "title":
            case "created":
            case "rating":
                return true;
            default:
                return false;
        }
    }

    private ResponseEntity<?> generateResponseFromBindingResult(BindingResult result) {
        Map<String, String> errors = result.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    private ResponseEntity<OkResponse> generateOkResponse(String message){
        return ResponseEntity.ok(new OkResponse(message));
    }

    private ResponseEntity<PostResponse> generatePostResponse(Post post){
        PostResponse postResponse = new PostResponse();
        postResponse.setTitle(post.getTitle());
        postResponse.setContent(post.getContent());
        postResponse.setRating(post.getRating());
        postResponse.setCreated(post.getCreated());
        postResponse.setEdited(post.getEdited());
        postResponse.setAuthor(post.getAuthor().getUsername());
        postResponse.setSection(post.getSection().getName());
        return ResponseEntity.ok(postResponse);
    }

    private AnswerModel generateAnswerModel(Answer answer){
        return null;
    }
}
