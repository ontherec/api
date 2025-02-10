package kr.ontherec.api.domain.host.application;

import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.host.dto.HostUpdateRequestDto;

public interface HostService {
    Long register(Host host);

    void update(String username, HostUpdateRequestDto dto);
}
