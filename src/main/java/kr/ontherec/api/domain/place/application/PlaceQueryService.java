package kr.ontherec.api.domain.place.application;

import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.place.domain.Place;

import java.util.List;

public interface PlaceQueryService {
    List<Place> search(String query);
    Place get(Long id);
    boolean isHost(Long id, Host host);
}