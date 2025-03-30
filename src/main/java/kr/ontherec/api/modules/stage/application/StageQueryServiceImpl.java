package kr.ontherec.api.modules.stage.application;

import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.stage.dao.StageRepository;
import kr.ontherec.api.modules.stage.entity.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StageQueryServiceImpl implements StageQueryService{
    private final StageRepository stageRepository;

    @Override
    public List<Stage> search(String query) {
        if(query == null) return stageRepository.findAll();
        return stageRepository.search(query);
    }

    @Override
    public Stage get(Long id) {
        return stageRepository.findByIdOrThrow(id);
    }

    @Override
    public boolean isHost(Long id, Host host) {
        Stage stage = stageRepository.findByIdOrThrow(id);
        return stage.getPlace().getHost().equals(host);
    }
}
