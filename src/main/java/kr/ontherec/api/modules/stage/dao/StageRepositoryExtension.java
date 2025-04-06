package kr.ontherec.api.modules.stage.dao;

import kr.ontherec.api.modules.stage.entity.Stage;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
@Transactional
public interface StageRepositoryExtension {
    List<Stage> search(String query, Map<String, String> params, Pageable pageable, String username);
}
