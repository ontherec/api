package kr.ontherec.api.modules.post.application;

import kr.ontherec.api.modules.post.entity.Post;

import java.util.List;

public interface PostQueryService {
    List<Post> search(String query);
    Post get(Long id);
    boolean isAuthor(Long id, String username);
}
