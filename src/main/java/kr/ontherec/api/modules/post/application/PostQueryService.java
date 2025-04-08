package kr.ontherec.api.modules.post.application;

import kr.ontherec.api.modules.post.entity.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostQueryService {
    List<Post> search(String query, Boolean liked, Pageable pageable, String username);
    Post get(Long id);
    boolean isAuthor(Long id, String username);
}
