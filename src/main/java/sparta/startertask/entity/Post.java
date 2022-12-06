package sparta.startertask.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sparta.startertask.dto.PostReq;
import sparta.startertask.dto.PostRes;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value={"modifiedDate"}, allowGetters=true)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String author;

    private String password;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    public PostRes toPostRes() {
        return PostRes.builder()
                .id(id)
                .title(title)
                .content(content)
                .author(author)
                .createdAt(createdDate)
                .modifiedAt(modifiedDate)
                .build();
    }

    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }

    public Post editPost(PostReq editPostReq) {
        this.title = editPostReq.getTitle();
        this.content = editPostReq.getContent();
        this.author = editPostReq.getAuthor();
        return this;
    }
}