package kr.ontherec.api.modules.place.application;

import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.place.entity.Place;
import kr.ontherec.api.modules.place.dto.PlaceUpdateRequestDto;
import kr.ontherec.api.modules.tag.entity.Tag;

import java.util.Set;

public interface PlaceCommandService {
    Place register(Host host, Place newPlace, Set<Tag> tags);
    void updateIntroduction(Long id, PlaceUpdateRequestDto.Introduction dto, Set<Tag> tags);
    void updateBusiness(Long id, PlaceUpdateRequestDto.Business dto);
    void updateParking(Long id, PlaceUpdateRequestDto.Parking dto);
    void delete(Long id);
}