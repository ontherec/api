package kr.ontherec.api.domain.place.application;

import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.place.domain.Place;
import kr.ontherec.api.domain.tag.domain.Tag;

import java.util.List;
import java.util.Set;

public interface PlaceService {
    List<Place> search(String query);
    Place get(Long id);
    Place register(Host host, Place newPlace, Set<Tag> tags);
    void update(Long id, Place newPlace, Set<Tag> tags);
    void delete(Long id);
    boolean isHost(Long id, Host host);
}
