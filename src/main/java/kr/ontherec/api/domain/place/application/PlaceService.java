package kr.ontherec.api.domain.place.application;

import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.keyword.domain.Keyword;
import kr.ontherec.api.domain.place.domain.Place;

import java.util.List;
import java.util.Set;

public interface PlaceService {
    List<Place> search(String query);
    Place get(Long id);
    Place register(Host host, Place newPlace, Set<Keyword> keywords);
    void update(Long id, Place newPlace, Set<Keyword> keywords);
    void delete(Long id);
    boolean isHost(Long id, Host host);
}
