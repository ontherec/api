package kr.ontherec.api.modules.stage.application;

import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.stage.dto.StageUpdateRequestDto;
import kr.ontherec.api.modules.stage.entity.Stage;
import kr.ontherec.api.modules.tag.entity.Tag;

import java.util.Set;

public interface StageCommandService {
    Stage register(Host host, Stage newStage, Set<Tag> tags);
    void updateImages(Long id, StageUpdateRequestDto.Images dto);
    void updateIntroduction(Long id, StageUpdateRequestDto.Introduction dto, Set<Tag> tags);
    void updateArea(Long id, StageUpdateRequestDto.Area dto);
    void updateBusiness(Long id, StageUpdateRequestDto.Business dto);
    void updateEngineering(Long id, StageUpdateRequestDto.Engineering dto);
    void updateDocuments(Long id, StageUpdateRequestDto.Documents dto);
    void updateParking(Long id, StageUpdateRequestDto.Parking dto);
    void updateFacilities(Long id, StageUpdateRequestDto.Facilities dto);
    void updateFnbPolicies(Long id, StageUpdateRequestDto.FnbPolicies dto);
    void delete(Long id);
}
