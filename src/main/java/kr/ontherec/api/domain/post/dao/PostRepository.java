package kr.ontherec.api.domain.post.dao;

import kr.ontherec.api.domain.post.domain.Post;
import kr.ontherec.api.domain.post.exception.PostException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kr.ontherec.api.domain.post.exception.PostExceptionCode.NOT_FOUND;

@Repository
@Transactional
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByTitleContainsOrContentContainsOrderByCreatedAtDesc(String title, String content);

    default List<Post> search(String query) {
        return findAllByTitleContainsOrContentContainsOrderByCreatedAtDesc(query, query);
    }

    default Post findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new PostException(NOT_FOUND));
    }

    default void deleteByIdOrThrow(Long id) {
        findById(id).orElseThrow(() -> new PostException(NOT_FOUND));
        deleteById(id);
    }
}
