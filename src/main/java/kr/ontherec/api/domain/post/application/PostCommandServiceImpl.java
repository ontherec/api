package kr.ontherec.api.domain.post.application;

import kr.ontherec.api.domain.post.dao.PostRepository;
import kr.ontherec.api.domain.post.domain.Post;
import kr.ontherec.api.domain.post.dto.PostUpdateRequestDto;
import kr.ontherec.api.domain.tag.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
@PreAuthorize("hasRole('ADMIN')")
public class PostCommandServiceImpl implements PostCommandService {
    private final PostRepository postRepository;
    private final PostMapper postMapper = PostMapper.INSTANCE;

    @Override
    public Post create(String author, Post newPost, Set<Tag> tags) {
        newPost.setAuthor(author);
        newPost.setTags(tags);
        return postRepository.save(newPost);
    }

    @Override
    public void update(Long id, PostUpdateRequestDto dto, Set<Tag> tags) {
        Post foundPost = postRepository.findByIdOrThrow(id);
        postMapper.update(dto, foundPost);
        foundPost.setTags(tags);
        postRepository.save(foundPost);
    }

    @Override
    public void delete(Long id) {
        postRepository.deleteByIdOrThrow(id);
    }
}
