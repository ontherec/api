package kr.ontherec.api.domain.post.application;

import kr.ontherec.api.domain.post.domain.Post;

import java.util.List;

public interface PostQueryService {
    List<Post> search(String query);
    Post get(Long id);
    boolean isAuthor(Long id, String username);
}
