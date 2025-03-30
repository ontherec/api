package kr.ontherec.api.modules.host.application;

import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.host.dto.HostUpdateRequestDto;

public interface HostService {
    Host get(Long id);
    Host getByUsername(String username);
    Host register(Host host);
    void update(Host host, HostUpdateRequestDto dto);
}
