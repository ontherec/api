package kr.ontherec.api.modules.practiceroom.dao;

import kr.ontherec.api.modules.practiceroom.entity.PracticeRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
@Transactional
public interface PracticeRoomRepositoryExtension {
    List<PracticeRoom> search(Map<String, String> params, Pageable pageable, String username);
}
