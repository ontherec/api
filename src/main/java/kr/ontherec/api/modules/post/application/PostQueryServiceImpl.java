package kr.ontherec.api.modules.post.application;

import kr.ontherec.api.modules.post.dao.PostRepository;
import kr.ontherec.api.modules.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostQueryServiceImpl implements PostQueryService {
    private final PostRepository postRepository;

    @Override
    public List<Post> search(String query, Boolean liked, Pageable pageable, String username) {
        return postRepository.search(query, liked, pageable, username);
    }

    @Override
    public Post get(Long id) {
        return postRepository.findByIdOrThrow(id);
    }

    @Override
    public boolean isAuthor(Long id, String username) {
        Post post = postRepository.findByIdOrThrow(id);
        return post.getAuthor().equals(username);
    }
}
