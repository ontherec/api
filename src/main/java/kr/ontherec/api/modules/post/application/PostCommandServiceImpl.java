package kr.ontherec.api.modules.post.application;

import kr.ontherec.api.modules.post.dao.PostRepository;
import kr.ontherec.api.modules.post.dto.PostUpdateRequestDto;
import kr.ontherec.api.modules.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
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
    public void like(Long id, String username) {
        Post foundPost = postRepository.findByIdOrThrow(id);
        foundPost.getLikedUsernames().add(username);
    }

    @Override
    public void unlike(Long id, String username) {
        Post foundPost = postRepository.findByIdOrThrow(id);
        foundPost.getLikedUsernames().remove(username);
    }

    @Override
    public void delete(Long id) {
        postRepository.deleteByIdOrThrow(id);
    }
}
