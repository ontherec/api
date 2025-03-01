package kr.ontherec.api.domain.place.dao;

import kr.ontherec.api.domain.place.domain.Place;
import kr.ontherec.api.domain.place.exception.PlaceException;
import kr.ontherec.api.domain.place.exception.PlaceExceptionCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findAllByTitleContainsOrAddress_StateContainsOrAddress_CityContainsOrAddress_StreetAddressContainsAllIgnoreCase(String title, String address_state, String address_city, String address_streetAddress);

    default List<Place> search(String query) {
        return findAllByTitleContainsOrAddress_StateContainsOrAddress_CityContainsOrAddress_StreetAddressContainsAllIgnoreCase(query, query, query, query);
    }

    boolean existsByBrn(String brn);

    default Place findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new PlaceException(PlaceExceptionCode.NOT_FOUND));
    }

    default void deleteByIdOrThrow(Long id) {
        findById(id).orElseThrow(() -> new PlaceException(PlaceExceptionCode.NOT_FOUND));
        deleteById(id);
    }
}
