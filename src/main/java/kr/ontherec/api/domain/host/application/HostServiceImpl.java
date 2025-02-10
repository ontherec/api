package kr.ontherec.api.domain.host.application;

import kr.ontherec.api.domain.host.dao.HostRepository;
import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.host.dto.HostUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class HostServiceImpl implements HostService {
    private final HostRepository hostRepository;
    private final HostMapper hostMapper = HostMapper.INSTANCE;

    @Override
    public Long register(Host host) {
        return hostRepository.save(host).getId();
    }

    @Override
    public void update(String username, HostUpdateRequestDto dto) {
        Host foundHost = hostRepository.findByUsernameOrThrow(username);
        hostMapper.update(dto, foundHost);
        hostRepository.save(foundHost);
    }
}
