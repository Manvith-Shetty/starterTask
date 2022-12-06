package sparta.startertask.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sparta.startertask.dto.PostReq;
import sparta.startertask.dto.PostRes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class PostControllerTest {

    @Autowired
    PostController postController;

    @DisplayName("1. 게시글 작성")
    @Test
    void test_1() {
        // given
        PostReq postReq = PostReq.builder()
                .title("title")
                .content("content")
                .author("author")
                .password("password")
                .build();

        // when
        PostRes postRes = postController.uploadPost(postReq);

        // then
        assertThat(postRes.getTitle()).isEqualTo(postReq.getTitle());
        assertThat(postRes.getContent()).isEqualTo(postReq.getContent());
        assertThat(postRes.getAuthor()).isEqualTo(postReq.getAuthor());
        assertThat(postRes.getId()).isNotNull();
        assertThat(postRes.getCreatedAt()).isEqualTo(postRes.getModifiedAt());
    }

    @DisplayName("2. 게시글 단건 조회")
    @Test
    void test_2() {
        // given
        PostReq postReq = PostReq.builder()
                .title("title")
                .content("content")
                .author("author")
                .password("password")
                .build();
        PostRes postRes = postController.uploadPost(postReq);

        // when
        PostRes findPost = postController.getOnePost(postRes.getId());

        // then
        assertThat(findPost.getTitle()).isEqualTo("title");
        assertThat(findPost.getAuthor()).isEqualTo("author");
        assertThat(findPost.getContent()).isEqualTo("content");
        assertThat(findPost.getId()).isEqualTo(postRes.getId());
    }

    @DisplayName("3. 게시글 단건 조회 - 없는 게시물 예외")
    @Test
    void test_3 () {
        // 없는 게시물(Long.MaxValue) 찾기
        assertThatIllegalArgumentException().isThrownBy(() -> postController.getOnePost(Long.MAX_VALUE));
    }

    @DisplayName("4. 게시글 전체 조회")
    @Test
    void test_4() {
        // given
        PostReq postReq = PostReq.builder()
                .title("title")
                .content("content")
                .author("author")
                .password("password")
                .build();
        int beforeSize = postController.getAllPosts().size();
        postController.uploadPost(postReq);

        // when
        List<PostRes> postRes = postController.getAllPosts();
        int afterSize = postController.getAllPosts().size();

        //then
        assertThat(postRes.stream().allMatch(Objects::nonNull)).isTrue();
        assertThat(beforeSize).isEqualTo(afterSize -1);
    }

    @DisplayName("5. 게시글 수정 - 정상 로직")
    @Test
    void test_5 () throws InterruptedException {
        // given
        PostReq uploadPost = PostReq.builder()
                .title("title")
                .content("content")
                .author("author")
                .password("password")
                .build();

        PostRes uploadedPost = postController.uploadPost(uploadPost);

        PostReq editReq = PostReq.builder()
                .title("title-edited")
                .content("content-edited")
                .author("author-edited")
                .password("password")
                .build();

        // when
        PostRes editedPost = postController.editPost(uploadedPost.getId(), editReq);

        // then
        assertThat(editedPost.getTitle()).isEqualTo("title-edited");
        assertThat(editedPost.getContent()).isEqualTo("content-edited");
        assertThat(editedPost.getAuthor()).isEqualTo("author-edited");
        assertThat(editedPost.getId()).isEqualTo(uploadedPost.getId());
        assertThat(editedPost.getModifiedAt()).isAfter(uploadedPost.getCreatedAt());
    }

    @DisplayName("6. 게시글 수정 - 잘못된 비밀번호")
    @Test
    void test_6() {
        // given
        PostReq uploadPost = PostReq.builder()
                .title("title")
                .content("content")
                .author("author")
                .password("password")
                .build();

        PostRes uploadedPost = postController.uploadPost(uploadPost);

        PostReq editReq = PostReq.builder()
                .title("title-edited")
                .content("content-edited")
                .author("author-edited")
                .password("passwordWrong")
                .build();

        // when - then
        assertThatIllegalArgumentException().isThrownBy(() -> postController.editPost(uploadedPost.getId(), editReq)).withMessage("비밀번호 불일치");
        assertThat(uploadedPost.getTitle()).isEqualTo("title");
        assertThat(uploadedPost.getAuthor()).isEqualTo("author");
        assertThat(uploadedPost.getContent()).isEqualTo("content");
        assertThat(uploadedPost.getModifiedAt()).isEqualTo(uploadedPost.getCreatedAt());
    }

    @DisplayName("7. 게시글 수정 - 없는 게시글")
    @Test
    void test_7() {
        // given
        Long postId = Long.MAX_VALUE;
        PostReq editReq = PostReq.builder()
                .title("title-edited")
                .content("content-edited")
                .author("author-edited")
                .password("password")
                .build();

        // when - then
        assertThatIllegalArgumentException().isThrownBy(() -> postController.editPost(postId, editReq)).withMessage("없음");
    }

    @DisplayName("8. 게시글 삭제 - 정상 로직")
    @Test
    void test_8() {
        // given
        PostReq uploadPost = PostReq.builder()
                .title("title")
                .content("content")
                .author("author")
                .password("password")
                .build();

        PostRes postRes = postController.uploadPost(uploadPost);

        Long postId = postRes.getId();

        HashMap<String, String> password = new HashMap<>();
        password.put("password", "password");

        int beforeSize = postController.getAllPosts().size();

        // when
        Map<String, Boolean> result = postController.deletePost(postId, password);
        int afterSize = postController.getAllPosts().size();

        // then
        assertThat(result.get("success")).isTrue();
        assertThat(beforeSize).isEqualTo(afterSize + 1);
        assertThatIllegalArgumentException().isThrownBy(() -> postController.getOnePost(postId)).withMessage("없음");
    }

    @DisplayName("9. 게시글 삭제 - 잘못된 비밀번호")
    @Test
    void test_9() {
        // given
        PostReq postReq = PostReq.builder()
                .title("title")
                .content("content")
                .author("author")
                .password("password")
                .build();

        PostRes postRes = postController.uploadPost(postReq);

        Long postId = postRes.getId();

        HashMap<String, String> password = new HashMap<>();
        password.put("password", "wrong-password");

        int beforeSize = postController.getAllPosts().size();

        // when(+then)
        assertThatIllegalArgumentException().isThrownBy(() -> postController.deletePost(postId, password)).withMessage("비밀번호 불일치");
        int afterSize = postController.getAllPosts().size();

        // then
        assertThat(beforeSize).isEqualTo(afterSize);
        assertThatNoException().isThrownBy(() -> postController.getOnePost(postId));
    }

    @DisplayName("10. 게시글 삭제 - 없는 게시글")
    @Test
    void test_10() {
        // given
        Long postId = Long.MAX_VALUE;

        HashMap<String, String> password = new HashMap<>();
        password.put("password", "password");

        int beforeSize = postController.getAllPosts().size();

        // when(+then)
        assertThatIllegalArgumentException().isThrownBy(() -> postController.deletePost(postId, password)).withMessage("없음");
        int afterSize = postController.getAllPosts().size();

        // then
        assertThat(beforeSize).isEqualTo(afterSize);
    }
}
