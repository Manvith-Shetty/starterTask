package sparta.startertask.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import sparta.startertask.dto.PostReq;
import sparta.startertask.entity.Post;
import sparta.startertask.repository.PostRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class MockPostControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;


    @After
    public void tearDown() throws Exception {
        postRepository.deleteAll();
    }

    @DisplayName("1. 게시글 작성")
    @Test
    void test_1() throws Exception {
        String title = "title";
        String author = "author";
        String content = "content";
        String password = "password";

        PostReq postReq = PostReq.builder()
                .title(title)
                .author(author)
                .content(content)
                .password(password)
                .build();

        String url = "http://localhost:" + port + "/api/post";

        mvc.perform(
                        post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(postReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.content").value(content));

        // then
        List<Post> all = postRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
        assertThat(all.get(0).getAuthor()).isEqualTo(author);
        assertThat(all.get(0).getPassword()).isEqualTo(password);
        assertThat(all.get(0).getCreatedDate()).isEqualTo(all.get(0).getModifiedDate());
    }

    @DisplayName("2. ")
    @Test
    void test_2() throws Exception {
        String title = "title";
        String author = "author";
        String content = "content";
        String password = "password";

        PostReq postReq = PostReq.builder()
                .title(title)
                .author(author)
                .content(content)
                .password(password)
                .build();

        String url = "http://localhost:" + port + "/api/post";

        mvc.perform(
                        post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(postReq))
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.author").value(author));
    }
}

//    @Before
//    public void setup() {
//        mvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .
//                .build();
//    }