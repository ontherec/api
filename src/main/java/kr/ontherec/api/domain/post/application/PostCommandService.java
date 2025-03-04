package kr.ontherec.api.domain.post.application;

import kr.ontherec.api.domain.post.domain.Post;
import kr.ontherec.api.domain.post.dto.PostUpdateRequestDto;
import kr.ontherec.api.domain.tag.domain.Tag;

import java.util.Set;

public interface PostCommandService {
    Post create(Post newPost, Set<Tag> tags);
    void update(Long id, PostUpdateRequestDto dto, Set<Tag> tags);
    void delete(Long id);
}
