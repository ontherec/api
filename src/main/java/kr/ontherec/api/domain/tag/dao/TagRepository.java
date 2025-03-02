package kr.ontherec.api.domain.tag.dao;

import kr.ontherec.api.domain.tag.domain.Tag;
import kr.ontherec.api.domain.tag.exception.TagException;
import kr.ontherec.api.domain.tag.exception.TagExceptionCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByTitle(String value);
    Optional<Tag> findByTitle(String value);
    default Tag findByTitleOrThrow(String value) {
        return findByTitle(value).orElseThrow(() -> new TagException(TagExceptionCode.NOT_FOUND));
    }
}
