package kr.ontherec.api.modules.post.application;

import kr.ontherec.api.modules.post.dto.PostUpdateRequestDto;
import kr.ontherec.api.modules.post.entity.Post;

public interface PostCommandService {
    Post create(String author, Post newPost);
    void update(Long id, PostUpdateRequestDto dto);
    void like(Long id, String username);
    void unlike(Long id, String username);
    void delete(Long id);
}
