package kr.ontherec.api.modules.practiceroom.application;

import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.practiceroom.dto.PracticeRoomUpdateRequestDto;
import kr.ontherec.api.modules.practiceroom.entity.PracticeRoom;

public interface PracticeRoomCommandService {
    PracticeRoom register(Host host, PracticeRoom newPracticeRoom);
    void updateImages(Long id, PracticeRoomUpdateRequestDto.Images dto);
    void updateIntroduction(Long id, PracticeRoomUpdateRequestDto.Introduction dto);
    void updateArea(Long id, PracticeRoomUpdateRequestDto.Area dto);
    void updateBusiness(Long id, PracticeRoomUpdateRequestDto.Business dto);
    void updateParking(Long id, PracticeRoomUpdateRequestDto.Parking dto);
    void updateFacilities(Long id, PracticeRoomUpdateRequestDto.Facilities dto);
    void updateFnbPolicies(Long id, PracticeRoomUpdateRequestDto.FnbPolicies dto);
    void like(Long id, String username);
    void unlike(Long id, String username);
    void delete(Long id);
}
