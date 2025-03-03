package kr.ontherec.api.domain.place.application;

import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.place.dao.PlaceRepository;
import kr.ontherec.api.domain.place.domain.Place;
import kr.ontherec.api.domain.place.dto.PlaceUpdateRequestDto;
import kr.ontherec.api.domain.place.exception.PlaceException;
import kr.ontherec.api.domain.place.exception.PlaceExceptionCode;
import kr.ontherec.api.domain.tag.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service @RequiredArgsConstructor
@Transactional
public class PlaceCommandServiceImpl implements PlaceCommandService {
    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper = PlaceMapper.INSTANCE;

    @Override
    public Place register(Host host, Place newPlace, Set<Tag> tags) {
        if(placeRepository.existsByBrn(newPlace.getBrn()))
            throw new PlaceException(PlaceExceptionCode.EXIST_BRN);

        newPlace.setHost(host);
        newPlace.setTags(tags);

        return placeRepository.save(newPlace);
    }

    @Override
    public void updateLocation(Long id, PlaceUpdateRequestDto.Title dto) {
        Place foundPlace = placeRepository.findByIdOrThrow(id);

        placeMapper.updateLocation(dto, foundPlace);

        placeRepository.save(foundPlace);
    }

    @Override
    public void updateIntroduction(Long id, PlaceUpdateRequestDto.Introduction dto, Set<Tag> tags) {
        Place foundPlace = placeRepository.findByIdOrThrow(id);

        placeMapper.updateIntroduction(dto, foundPlace);
        foundPlace.setTags(tags);

        placeRepository.save(foundPlace);
    }

    @Override
    public void updateBusiness(Long id, PlaceUpdateRequestDto.Business dto) {
        Place foundPlace = placeRepository.findByIdOrThrow(id);

        placeMapper.updateBusiness(dto, foundPlace);

        placeRepository.save(foundPlace);
    }

    @Override
    public void delete(Long id) {
        placeRepository.deleteByIdOrThrow(id);
    }
}
