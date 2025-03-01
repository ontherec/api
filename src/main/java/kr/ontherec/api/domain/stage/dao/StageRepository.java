package kr.ontherec.api.domain.stage.dao;

import kr.ontherec.api.domain.stage.domain.Stage;
import kr.ontherec.api.domain.stage.exception.StageException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static kr.ontherec.api.domain.stage.exception.StageExceptionCode.NOT_FOUND;

public interface StageRepository extends JpaRepository<Stage, Long> {
    List<Stage> findAllByTitleContainsOrPlace_TitleContainsOrPlace_Address_StateContainsOrPlace_Address_CityContainsOrPlace_Address_StreetAddressContainsOrderByViewCountDescAllIgnoreCase(String title, String place_title, String place_address_state, String place_address_city, String place_address_streetAddress);

    default List<Stage> search(String query) {
        return findAllByTitleContainsOrPlace_TitleContainsOrPlace_Address_StateContainsOrPlace_Address_CityContainsOrPlace_Address_StreetAddressContainsOrderByViewCountDescAllIgnoreCase(query, query, query, query, query);
    }

    default Stage findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new StageException(NOT_FOUND));
    }

    default void deleteByIdOrThrow(Long id) {
        findById(id).orElseThrow(() -> new StageException(NOT_FOUND));
        deleteById(id);
    }
}
