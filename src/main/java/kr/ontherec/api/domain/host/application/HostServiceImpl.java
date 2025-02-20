package kr.ontherec.api.domain.host.application;

import kr.ontherec.api.domain.host.dao.HostRepository;
import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.host.dto.HostUpdateRequestDto;
import kr.ontherec.api.domain.host.exception.HostException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kr.ontherec.api.domain.host.exception.HostExceptionCode.EXIST_USERNAME;

@Service
@Transactional
@RequiredArgsConstructor
public class HostServiceImpl implements HostService {
    private final HostRepository hostRepository;
    private final HostMapper hostMapper = HostMapper.INSTANCE;

    @Override
    public Host register(Host host) {
        if(hostRepository.existsByUsername(host.getUsername()))
            throw new HostException(EXIST_USERNAME);

        return hostRepository.save(host);
    }

    @Override
    public Host getById(Long id) {
        return hostRepository.findByIdOrThrow(id);
    }

    @Override
    public Host getByUsername(String username) {
        return hostRepository.findByUsernameOrThrow(username);
    }

    @Override
    public void update(Host host, HostUpdateRequestDto dto) {
        hostMapper.update(dto, host);
        hostRepository.save(host);
    }
}
