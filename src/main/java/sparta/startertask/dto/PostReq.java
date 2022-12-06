package sparta.startertask.dto;

import lombok.Builder;
import lombok.Getter;
import sparta.startertask.entity.Post;

@Builder
@Getter
public class PostReq {
    private String title;
    private String content;
    private String author;
    private String password;

    public Post toPost() {
        return Post.builder()
                .title(title)
                .content(content)
                .author(author)
                .password(password)
                .build();
    }
}
