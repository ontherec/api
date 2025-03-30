package kr.ontherec.api.modules.stage.application;

import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.stage.entity.Stage;

import java.util.List;

public interface StageQueryService {
    List<Stage> search(String query);
    Stage get(Long id);
    boolean isHost(Long id, Host host);
}
