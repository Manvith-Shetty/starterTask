package sparta.startertask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sparta.startertask.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
