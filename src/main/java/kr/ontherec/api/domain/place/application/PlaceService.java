package kr.ontherec.api.domain.place.application;

import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.keyword.domain.Keyword;
import kr.ontherec.api.domain.place.domain.Place;

import java.util.List;
import java.util.Set;

public interface PlaceService {
    List<Place> search(String query);
    boolean isHost(Long id, Host host);
    Place register(Host host, Place place, Set<Keyword> keywords);
    void update(Long id, Place place, Set<Keyword> keywords);
    void remove(Long id);
}
