package sparta.startertask.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sparta.startertask.dto.PostReq;
import sparta.startertask.dto.PostRes;
import sparta.startertask.entity.Post;
import sparta.startertask.repository.PostRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostRes uploadPost(PostReq postReq) {
        Post post = postReq.toPost();
        postRepository.save(post);
        return post.toPostRes();
    }

    public List<PostRes> getAllPosts() {
        List<Post> postList = postRepository.findAll();
        return postList.stream().map(Post::toPostRes).toList();
    }

    public PostRes showOnePost(Long postId) {
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("없음"));

        return findPost.toPostRes();
    }

    public PostRes editPost(Long postId, PostReq editPostReq) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("없음"));
        if(post.validatePassword(editPostReq.getPassword())) {
            return post.editPost(editPostReq).toPostRes();
        } else {
            throw new IllegalArgumentException("비밀번호 불일치");
        }
    }

    public Map<String, Boolean> deletePost(Long postId, Map<String, String> password) throws IllegalArgumentException{
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("없음"));
        if(findPost.validatePassword(password.get("password"))){
            postRepository.deleteById(postId);
            Map<String, Boolean> map = new HashMap<>();
            map.put("success", true);
            return map;
        } else {
            throw new IllegalArgumentException("비밀번호 불일치");
        }
    }

    public void deleteAll() {
        postRepository.deleteAll();
    }
}
