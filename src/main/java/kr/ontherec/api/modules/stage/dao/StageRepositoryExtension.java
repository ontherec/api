package kr.ontherec.api.modules.stage.dao;

import kr.ontherec.api.modules.stage.entity.Stage;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface StageRepositoryExtension {
    List<Stage> search(String query, Pageable pageable);
}
