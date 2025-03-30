package kr.ontherec.api.modules.place.application;

import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.place.dao.PlaceRepository;
import kr.ontherec.api.modules.place.entity.Place;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
@Transactional
public class PlaceQueryServiceImpl implements PlaceQueryService {
    private final PlaceRepository placeRepository;

    @Override
    public List<Place> search(String query) {
        if(query == null) return placeRepository.findAll();
        return placeRepository.search(query);
    }

    @Override
    public Place get(Long id) {
        return placeRepository.findByIdOrThrow(id);
    }

    @Override
    public boolean isHost(Long id, Host host) {
        Place place = placeRepository.findByIdOrThrow(id);
        return place.getHost().equals(host);
    }
}
