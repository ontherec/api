package kr.ontherec.api.modules.post.dao;

import kr.ontherec.api.modules.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface PostRepositoryExtension {
    List<Post> search(String query, Pageable pageable, String username);
}
