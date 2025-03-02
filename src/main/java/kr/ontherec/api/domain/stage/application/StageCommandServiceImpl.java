package kr.ontherec.api.domain.stage.application;

import kr.ontherec.api.domain.place.domain.Place;
import kr.ontherec.api.domain.stage.dao.StageRepository;
import kr.ontherec.api.domain.stage.domain.Stage;
import kr.ontherec.api.domain.stage.dto.StageUpdateRequestDto;
import kr.ontherec.api.domain.tag.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service @RequiredArgsConstructor
@Transactional
public class StageCommandServiceImpl implements StageCommandService {
    private final StageRepository stageRepository;
    private final StageMapper stageMapper = StageMapper.INSTANCE;

    @Override
    public Stage register(Place place, Stage newStage, Set<Tag> tags) {
        newStage.setPlace(place);
        newStage.setTags(tags);

        return stageRepository.save(newStage);
    }

    @Override
    public void updateLocation(Long id, StageUpdateRequestDto.Location dto) {
        Stage foundStage = stageRepository.findByIdOrThrow(id);
        stageMapper.updateLocation(dto, foundStage);
        stageRepository.save(foundStage);
    }

    @Override
    public void updateArea(Long id, StageUpdateRequestDto.Area dto) {
        Stage foundStage = stageRepository.findByIdOrThrow(id);
        stageMapper.updateArea(dto, foundStage);
        stageRepository.save(foundStage);
    }

    @Override
    public void updateIntroduction(Long id, StageUpdateRequestDto.Introduction dto, Set<Tag> tags) {
        Stage foundStage = stageRepository.findByIdOrThrow(id);
        stageMapper.updateIntroduction(dto, foundStage);
        foundStage.setTags(tags);
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
