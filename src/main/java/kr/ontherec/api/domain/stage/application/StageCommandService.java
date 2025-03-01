package kr.ontherec.api.domain.stage.application;

import kr.ontherec.api.domain.place.domain.Place;
import kr.ontherec.api.domain.stage.domain.Stage;
import kr.ontherec.api.domain.stage.dto.StageRegisterRequestDto;
import kr.ontherec.api.domain.stage.dto.StageUpdateRequestDto;
import kr.ontherec.api.domain.tag.domain.Tag;

import java.util.Set;

public interface StageCommandService {
    Stage register(StageRegisterRequestDto dto, Place place, Set<Tag> tags);
    void updateLocation(Long id, StageUpdateRequestDto.Location dto);
    void updateInformation(Long id, StageUpdateRequestDto.Information dto);
    void updateIntroduction(Long id, StageUpdateRequestDto.Introduction dto);
    void updateBusiness(Long id, StageUpdateRequestDto.Business dto);
    void updateEngineering(Long id, StageUpdateRequestDto.Engineering dto);
    void updateDocuments(Long id, StageUpdateRequestDto.Documents dto);
    void updateFacilities(Long id, StageUpdateRequestDto.Facilities dto);
    void updateFnbPolicies(Long id, StageUpdateRequestDto.FnbPolicies dto);
    void delete(Long id);
}
