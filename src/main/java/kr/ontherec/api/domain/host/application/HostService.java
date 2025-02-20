package kr.ontherec.api.domain.host.application;

import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.host.dto.HostUpdateRequestDto;

public interface HostService {
    Host register(Host host);
    Host get(Long id);
    void update(String username, HostUpdateRequestDto dto);
}
