package kr.ontherec.api.domain.place.application;

import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.place.dao.PlaceRepository;
import kr.ontherec.api.domain.place.domain.Place;
import kr.ontherec.api.domain.place.exception.PlaceException;
import kr.ontherec.api.domain.place.exception.PlaceExceptionCode;
import kr.ontherec.api.domain.tag.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper = PlaceMapper.INSTANCE;

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
    public Place register(Host host, Place newPlace, Set<Tag> tags) {
        if(placeRepository.existsByBrn(newPlace.getBrn()))
            throw new PlaceException(PlaceExceptionCode.EXIST_BRN);

        newPlace.setHost(host);
        newPlace.setTags(tags);

        return placeRepository.save(newPlace);
    }

    @Override
    public void update(Long id, Place place, Set<Tag> tags) {
        Place foundPlace = placeRepository.findByIdOrThrow(id);

        placeMapper.update(place, foundPlace);
        foundPlace.setTags(tags);

        placeRepository.save(foundPlace);
    }

    @Override
    public void delete(Long id) {
        placeRepository.deleteByIdOrThrow(id);
    }

    @Override
    public boolean isHost(Long id, Host host) {
        Place place = placeRepository.findByIdOrThrow(id);
        return place.getHost().equals(host);
    }
}
