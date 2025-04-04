package kr.ontherec.api.modules.stage.application;

import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.item.entity.Tag;
import kr.ontherec.api.modules.stage.dao.StageRepository;
import kr.ontherec.api.modules.stage.dto.StageUpdateRequestDto;
import kr.ontherec.api.modules.stage.entity.Stage;
import kr.ontherec.api.modules.stage.exception.StageException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static kr.ontherec.api.modules.stage.exception.StageExceptionCode.EXIST_BRN;

@Service @RequiredArgsConstructor
@Transactional
public class StageCommandServiceImpl implements StageCommandService {
    private final StageRepository stageRepository;
    private final StageMapper stageMapper = StageMapper.INSTANCE;

    @Override
    public Stage register(Host host, Stage newStage, Set<Tag> tags) {
        if(stageRepository.existsByBrn(newStage.getBrn()))
            throw new StageException(EXIST_BRN);

        newStage.setHost(host);
        newStage.getTags().addAll(tags);

        return stageRepository.save(newStage);
    }

    @Override
    public void updateImages(Long id, StageUpdateRequestDto.Images dto) {
        Stage foundStage = stageRepository.findByIdOrThrow(id);
        stageMapper.updateImages(dto, foundStage);
        stageRepository.save(foundStage);
    }

    @Override
    public void updateIntroduction(Long id, StageUpdateRequestDto.Introduction dto, Set<Tag> tags) {
        Stage foundStage = stageRepository.findByIdOrThrow(id);
        stageMapper.updateIntroduction(dto, foundStage);
        foundStage.getTags().addAll(tags);
        stageRepository.save(foundStage);
    }

    @Override
    public void updateArea(Long id, StageUpdateRequestDto.Area dto) {
        Stage foundStage = stageRepository.findByIdOrThrow(id);
        stageMapper.updateArea(dto, foundStage);
        stageRepository.save(foundStage);
    }

    @Override
    public void updateBusiness(Long id, StageUpdateRequestDto.Business dto) {
        Stage foundStage = stageRepository.findByIdOrThrow(id);
        stageMapper.updateBusiness(dto, foundStage);
        stageRepository.save(foundStage);
    }

    @Override
    public void updateEngineering(Long id, StageUpdateRequestDto.Engineering dto) {
        Stage foundStage = stageRepository.findByIdOrThrow(id);
        stageMapper.updateEngineering(dto, foundStage);
        stageRepository.save(foundStage);
    }

    @Override
    public void updateDocuments(Long id, StageUpdateRequestDto.Documents dto) {
        Stage foundStage = stageRepository.findByIdOrThrow(id);
        stageMapper.updateDocuments(dto, foundStage);
        stageRepository.save(foundStage);
    }

    @Override
    public void updateParking(Long id, StageUpdateRequestDto.Parking dto) {
        Stage foundStage = stageRepository.findByIdOrThrow(id);
        stageMapper.updateParking(dto, foundStage);
        stageRepository.save(foundStage);
    }

    @Override
    public void updateFacilities(Long id, StageUpdateRequestDto.Facilities dto) {
        Stage foundStage = stageRepository.findByIdOrThrow(id);
        stageMapper.updateFacilities(dto, foundStage);
        stageRepository.save(foundStage);
    }

    @Override
    public void updateFnbPolicies(Long id, StageUpdateRequestDto.FnbPolicies dto) {
        Stage foundStage = stageRepository.findByIdOrThrow(id);
        stageMapper.updateFnbPolicies(dto, foundStage);
        stageRepository.save(foundStage);
    }

    @Override
    public void delete(Long id) {
        stageRepository.deleteByIdOrThrow(id);
    }
}
