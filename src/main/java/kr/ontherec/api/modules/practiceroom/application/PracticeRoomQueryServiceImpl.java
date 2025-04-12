package kr.ontherec.api.modules.practiceroom.application;

import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.practiceroom.dao.PracticeRoomRepository;
import kr.ontherec.api.modules.practiceroom.entity.PracticeRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class PracticeRoomQueryServiceImpl implements PracticeRoomQueryService {
    private final PracticeRoomRepository practiceRoomRepository;

    @Override
    public List<PracticeRoom> search(Map<String, String> params, Pageable pageable, String username) {
        return practiceRoomRepository.search(params, pageable, username);
    }

    @Override
    public PracticeRoom get(Long id) {
        return practiceRoomRepository.findByIdOrThrow(id);
    }

    @Override
    public boolean isHost(Long id, Host host) {
        PracticeRoom practiceRoom = practiceRoomRepository.findByIdOrThrow(id);
        return practiceRoom.getHost().equals(host);
    }
}
