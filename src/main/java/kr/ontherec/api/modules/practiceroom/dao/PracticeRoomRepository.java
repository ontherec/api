package kr.ontherec.api.modules.practiceroom.dao;

import kr.ontherec.api.modules.practiceroom.entity.PracticeRoom;
import kr.ontherec.api.modules.practiceroom.exception.PracticeRoomException;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static kr.ontherec.api.modules.practiceroom.exception.PracticeRoomExceptionCode.NOT_FOUND;

@Repository
@Transactional
public interface PracticeRoomRepository extends JpaRepository<PracticeRoom, Long>, PracticeRoomRepositoryExtension {
    @NonNull Page<PracticeRoom> findAll(@NonNull Pageable pageable);

    default PracticeRoom findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new PracticeRoomException(NOT_FOUND));
    }

    default void deleteByIdOrThrow(Long id) {
        findById(id).orElseThrow(() -> new PracticeRoomException(NOT_FOUND));
        deleteById(id);
    }

    boolean existsByBrn(String brn);
}
