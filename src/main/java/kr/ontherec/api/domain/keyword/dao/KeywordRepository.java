package kr.ontherec.api.domain.keyword.dao;

import kr.ontherec.api.domain.keyword.domain.Keyword;
import kr.ontherec.api.domain.keyword.exception.KeywordException;
import kr.ontherec.api.domain.keyword.exception.KeywordExceptionCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    boolean existsByTitle(String value);
    Optional<Keyword> findByTitle(String value);
    default Keyword findByTitleOrThrow(String value) {
        return findByTitle(value).orElseThrow(() -> new KeywordException(KeywordExceptionCode.NOT_FOUND));
    }
}
