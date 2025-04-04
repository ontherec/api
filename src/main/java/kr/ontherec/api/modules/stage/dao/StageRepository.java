package kr.ontherec.api.modules.stage.dao;

import kr.ontherec.api.modules.stage.entity.Stage;
import kr.ontherec.api.modules.stage.exception.StageException;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static kr.ontherec.api.modules.stage.exception.StageExceptionCode.NOT_FOUND;

@Repository
@Transactional
public interface StageRepository extends JpaRepository<Stage, Long>, StageRepositoryExtension {
    @NonNull Page<Stage> findAll(@NonNull Pageable pageable);

    default Stage findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new StageException(NOT_FOUND));
    }

    default void deleteByIdOrThrow(Long id) {
        findById(id).orElseThrow(() -> new StageException(NOT_FOUND));
        deleteById(id);
    }

    boolean existsByBrn(String brn);
}
