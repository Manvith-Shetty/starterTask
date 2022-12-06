package sparta.startertask.controller;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import sparta.startertask.dto.PostReq;
import sparta.startertask.dto.PostRes;
import sparta.startertask.repository.PostRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class PostControllerTest {

    @Autowired
    PostController postController;
    @Autowired
    PostRepository postRepository;

    @BeforeEach
    void beforeEach() {
        PostReq postReq;
        for (int i = 0; i < 100; i++) {
            postReq = PostReq.builder()
                    .title("title")
                    .content("content")
                    .author("author")
                    .password("password")
                    .build();
            postController.uploadPost(postReq);
        }
    }

    @AfterEach
    void afterEach() {
        postController.deleteAll();
    }

    @DisplayName("1. 게시글 작성")
    @Test
    void test_1() {
        PostReq postReq = PostReq.builder()
                .title("title")
                .content("content")
                .author("author")
                .password("password")
                .build();

        PostRes postRes = postController.uploadPost(postReq);

        assertThat(postRes.getTitle()).isEqualTo(postReq.getTitle());
        assertThat(postRes.getContent()).isEqualTo(postReq.getContent());
        assertThat(postRes.getAuthor()).isEqualTo(postReq.getAuthor());
    } // id 1 ~ 100 rptlrmf =>삭제

    // 101 ~ 200 idx 를 1번부터 ...
    @DisplayName("2. 게시글 단건 조회")
    @Test
    void test_2() {
        Long postId = 100L;

        PostRes postRes = postController.getOnePost(postId);

        assertThat(postRes.getTitle()).isEqualTo("title");
        assertThat(postRes.getAuthor()).isEqualTo("author");
        assertThat(postRes.getContent()).isEqualTo("content");
        assertThat(postRes.getId()).isEqualTo(postId);
    }



    @DisplayName("3. 게시글 단건 조회 - 없는 게시물 예외")
    @Test
    void test_3 () {
        assertThatIllegalArgumentException().isThrownBy(() -> postController.getOnePost(200L));
    }

    @DisplayName("4. 게시글 전체 조회")
    @Test
    void test_4() {
        List<PostRes> postRes = postController.getAllPosts();
        assertThat(postRes.size()).isEqualTo(10);
        assertThat(postRes.stream().allMatch(Objects::nonNull)).isTrue();
    }

    @DisplayName("5. 게시글 수정 - 정상 로직")
    @Test
    void test_5 () {
        Long postId = 1L;
        PostReq postReq = PostReq.builder()
                .title("title-edited")
                .content("content-edited")
                .author("author-edited")
                .password("password")
                .build();

        PostRes postRes = postController.editPost(postId, postReq);

        assertThat(postRes.getTitle()).isEqualTo("title-edited");
        assertThat(postRes.getContent()).isEqualTo("content-edited");
        assertThat(postRes.getAuthor()).isEqualTo("author-edited");
        assertThat(postRes.getId()).isEqualTo(postId);
        assertThat(postRes.getModifiedAt()).isAfter(postRes.getCreatedAt());
    }

    @DisplayName("6. 게시글 수정 - 잘못된 비밀번호")
    @Test
    void test_6() {
        Long postId = 1L;
        PostReq postReq = PostReq.builder()
                .title("title-edited")
                .content("content-edited")
                .author("author-edited")
                .password("passwordWrong")
                .build();
        assertThatIllegalArgumentException().isThrownBy(() -> postController.editPost(postId, postReq)).withMessage("비밀번호 불일치");
    }

    @DisplayName("7. 게시글 수정 - 없는 게시글")
    @Test
    void test_7() {
        Long postId = 20L;

        PostReq postReq = PostReq.builder()
                .title("title-edited")
                .content("content-edited")
                .author("author-edited")
                .password("password")
                .build();

        assertThatIllegalArgumentException().isThrownBy(() -> postController.editPost(postId, postReq)).withMessage("없음");
    }

    @DisplayName("8. 게시글 삭제 - 정상 로직")
    @Test
    void test_8() {
        Long postId = 1L;
        HashMap<String, String> password = new HashMap<>();
        password.put("password", "password");
        Map<String, Boolean> result = postController.deletePost(postId, password);
        List<PostRes> allPosts = postController.getAllPosts();

        assertThat(result.get("success")).isTrue();
        assertThat(allPosts.size()).isEqualTo(9);
        assertThatIllegalArgumentException().isThrownBy(() -> postController.getOnePost(postId)).withMessage("없음");
    }

    @DisplayName("9. 게시글 삭제 - 잘못된 비밀번호")
    @Test
    void test_9() {
        Long postId = 1L;
        HashMap<String, String> password = new HashMap<>();
        password.put("password", "wrong-password");

        assertThatIllegalArgumentException().isThrownBy(() -> postController.deletePost(postId, password)).withMessage("비밀번호 불일치");

        List<PostRes> allPosts = postController.getAllPosts();

        assertThat(allPosts.size()).isEqualTo(100);
        assertThatNoException().isThrownBy(() -> postController.getOnePost(postId));
    }

    @DisplayName("10. 게시글 삭제 - 없는 게시글")
    @Test
    void test_10() {
        Long postId = 100L;
        HashMap<String, String> password = new HashMap<>();
        password.put("password", "password");

        assertThatIllegalArgumentException().isThrownBy(() -> postController.deletePost(postId, password)).withMessage("없음");

        List<PostRes> allPosts = postController.getAllPosts();

        assertThat(allPosts.size()).isEqualTo(10);
        assertThatNoException().isThrownBy(() -> postController.getOnePost(postId));
    }
}
