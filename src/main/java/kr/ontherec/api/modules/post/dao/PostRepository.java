package kr.ontherec.api.modules.post.dao;

import kr.ontherec.api.modules.post.entity.Post;
import kr.ontherec.api.modules.post.exception.PostException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static kr.ontherec.api.modules.post.exception.PostExceptionCode.NOT_FOUND;

@Repository
@Transactional
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryExtension {
    default Post findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new PostException(NOT_FOUND));
    }

    default void deleteByIdOrThrow(Long id) {
        findById(id).orElseThrow(() -> new PostException(NOT_FOUND));
        deleteById(id);
    }
}
