package kr.ontherec.api.modules.host.application;

import kr.ontherec.api.modules.host.dao.HostRepository;
import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.host.dto.HostUpdateRequestDto;
import kr.ontherec.api.modules.host.exception.HostException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kr.ontherec.api.modules.host.exception.HostExceptionCode.EXIST_USERNAME;

@Service @RequiredArgsConstructor
@Transactional
public class HostServiceImpl implements HostService {
    private final HostRepository hostRepository;
    private final HostMapper hostMapper = HostMapper.INSTANCE;

    @Override
    public Host get(Long id) {
        return hostRepository.findByIdOrThrow(id);
    }

    @Override
    public Host register(Host host) {
        if(hostRepository.existsByUsername(host.getUsername()))
            throw new HostException(EXIST_USERNAME);

        // TODO: 사용자 확인

        return hostRepository.save(host);
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
