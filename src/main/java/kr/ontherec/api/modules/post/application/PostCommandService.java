package kr.ontherec.api.modules.post.application;

import kr.ontherec.api.modules.post.dto.PostUpdateRequestDto;
import kr.ontherec.api.modules.post.entity.Post;
import kr.ontherec.api.modules.tag.entity.Tag;

import java.util.Set;

public interface PostCommandService {
    Post create(String author, Post newPost, Set<Tag> tags);
    void update(Long id, PostUpdateRequestDto dto, Set<Tag> tags);
    void delete(Long id);
}
