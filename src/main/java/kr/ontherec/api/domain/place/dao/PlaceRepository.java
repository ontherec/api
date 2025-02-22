package kr.ontherec.api.domain.place.dao;

import kr.ontherec.api.domain.place.domain.Place;
import kr.ontherec.api.domain.place.exception.PlaceException;
import kr.ontherec.api.domain.place.exception.PlaceExceptionCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findAllByNameContains(String name);

    default List<Place> search(String query) {
        return findAllByNameContains(query);
    }

    boolean existsByBrn(String brn);

    default Place findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new PlaceException(PlaceExceptionCode.NOT_FOUND));
    }
}
