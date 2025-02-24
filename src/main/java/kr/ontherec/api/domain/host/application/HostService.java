package kr.ontherec.api.domain.host.application;

import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.host.dto.HostUpdateRequestDto;

public interface HostService {
    Host get(Long id);
    Host getByUsername(String username);
    Host register(Host host);
    void update(Host host, HostUpdateRequestDto dto);
}
