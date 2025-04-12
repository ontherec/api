package kr.ontherec.api.modules.practiceroom.application;

import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.practiceroom.entity.PracticeRoom;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PracticeRoomQueryService {
    List<PracticeRoom> search(Map<String, String> params, Pageable pageable, String username);
    PracticeRoom get(Long id);
    boolean isHost(Long id, Host host);
}
