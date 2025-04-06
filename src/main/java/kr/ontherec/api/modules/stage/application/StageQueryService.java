package kr.ontherec.api.modules.stage.application;

import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.stage.entity.Stage;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface StageQueryService {
    List<Stage> search(String query, Map<String, String> params, Pageable pageable, String username);
    Stage get(Long id);
    boolean isHost(Long id, Host host);
}
