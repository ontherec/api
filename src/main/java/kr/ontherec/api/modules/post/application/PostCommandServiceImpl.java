package kr.ontherec.api.modules.post.application;

import kr.ontherec.api.modules.post.dao.PostRepository;
import kr.ontherec.api.modules.post.dto.PostUpdateRequestDto;
import kr.ontherec.api.modules.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@PreAuthorize("hasRole('ADMIN')")
public class PostCommandServiceImpl implements PostCommandService {
    private final PostRepository postRepository;
    private final PostMapper postMapper = PostMapper.INSTANCE;

    @Override
    public Post create(String author, Post newPost) {
        newPost.setAuthor(author);
        return postRepository.save(newPost);
    }

    @Override
    public void update(Long id, PostUpdateRequestDto dto) {
        Post foundPost = postRepository.findByIdOrThrow(id);
        postMapper.update(dto, foundPost);
        postRepository.save(foundPost);
    }

    @Override
    public void delete(Long id) {
        postRepository.deleteByIdOrThrow(id);
    }
}
