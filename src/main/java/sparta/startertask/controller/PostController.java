package sparta.startertask.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sparta.startertask.dto.PostReq;
import sparta.startertask.dto.PostRes;
import sparta.startertask.service.PostService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public List<PostRes> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{post-id}")
    public PostRes getOnePost(@PathVariable("post-id") Long postId) {
        return postService.showOnePost(postId);
    }

    @PostMapping
    public PostRes uploadPost(@RequestBody PostReq postReq) {
        return postService.uploadPost(postReq);
    }

    @PutMapping("/{post_id}")
    public PostRes editPost(@PathVariable("post_id") Long postId, @RequestBody PostReq editPostReq) {
        return postService.editPost(postId, editPostReq);
    }

    @DeleteMapping("/{post_id}")
    public Map<String, Boolean> deletePost(@PathVariable("post_id") Long postId, @RequestBody Map<String, String> password) {
        return postService.deletePost(postId, password);
    }

    public void deleteAll() {
        postService.deleteAll();
    }
}
