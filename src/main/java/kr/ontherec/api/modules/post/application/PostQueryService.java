package kr.ontherec.api.modules.post.application;

import kr.ontherec.api.modules.post.entity.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostQueryService {
    List<Post> search(String query, Pageable pageable, String username);
    Post get(Long id);
    boolean isAuthor(Long id, String username);
}
