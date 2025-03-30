package kr.ontherec.api.modules.place.application;

import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.place.entity.Place;

import java.util.List;

public interface PlaceQueryService {
    List<Place> search(String query);
    Place get(Long id);
    boolean isHost(Long id, Host host);
}