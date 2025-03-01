package kr.ontherec.api.domain.stage.application;

import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.stage.domain.Stage;

import java.util.List;

public interface StageQueryService {
    List<Stage> search(String query);
    Stage get(Long id);
    boolean isHost(Long id, Host host);
}
