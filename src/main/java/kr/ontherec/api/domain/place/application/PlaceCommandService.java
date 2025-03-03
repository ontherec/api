package kr.ontherec.api.domain.place.application;

import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.place.domain.Place;
import kr.ontherec.api.domain.place.dto.PlaceUpdateRequestDto;
import kr.ontherec.api.domain.tag.domain.Tag;

import java.util.Set;

public interface PlaceCommandService {
    Place register(Host host, Place newPlace, Set<Tag> tags);
    void updateIntroduction(Long id, PlaceUpdateRequestDto.Introduction dto, Set<Tag> tags);
    void updateBusiness(Long id, PlaceUpdateRequestDto.Business dto);
    void updateParking(Long id, PlaceUpdateRequestDto.Parking dto);
    void delete(Long id);
}