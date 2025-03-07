package kr.ontherec.api.domain.place.application;

import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.place.domain.Place;
import kr.ontherec.api.domain.place.exception.PlaceException;
import kr.ontherec.api.domain.place.exception.PlaceExceptionCode;
import kr.ontherec.api.domain.tag.domain.Tag;
import kr.ontherec.api.infra.UnitTest;
import kr.ontherec.api.infra.fixture.HostFactory;
import kr.ontherec.api.infra.fixture.PlaceFactory;
import kr.ontherec.api.infra.fixture.TagFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@UnitTest
class PlaceQueryServiceTest {

    @Autowired private HostFactory hostFactory;
    @Autowired private TagFactory tagFactory;
    @Autowired private PlaceFactory placeFactory;

    @Autowired private PlaceQueryService placeQueryService;

    @DisplayName("플레이스 검색 성공")
    @Test
    void search() {
        // given
        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);

        // when
        List<Place> places = placeQueryService.search("place");

        // then
        assertThat(places.contains(place)).isTrue();
    }

    @DisplayName("플레이스 조회 성공")
    @Test
    void get() {
        // given
        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);

        // when
        Place foundPlace = placeQueryService.get(place.getId());

        // then
        assertThat(foundPlace.getId()).isEqualTo(place.getId());
    }
    
    @DisplayName("플레이스 조회 실패 - 등록되지 않은 플레이스")
    @Test
    void getWithUnregisteredPlaceId() {
        // given
        hostFactory.create("test");

        // when
        Throwable throwable = catchThrowable(() -> placeQueryService.get(1L));

        // then
        assertThat(throwable)
                .isInstanceOf(PlaceException.class)
                .hasMessage(PlaceExceptionCode.NOT_FOUND.getMessage());
    }

    @DisplayName("플레이스 호스트 확인 성공")
    @Test
    void isHost() {
        // given
        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);

        // when
        boolean isHost = placeQueryService.isHost(place.getId(), host);

        // then
        assertThat(isHost).isEqualTo(true);
    }

    @DisplayName("플레이스 호스트 확인 실패 - 등록되지 않은 공연장")
    @Test
    void isHostWithUnregisteredPlaceId() {
        // given
        Host host = hostFactory.create("test");

        // when
        Throwable throwable = catchThrowable(() -> placeQueryService.isHost(1L, host));

        // then
        assertThat(throwable)
                .isInstanceOf(PlaceException.class)
                .hasMessage(PlaceExceptionCode.NOT_FOUND.getMessage());
    }
}