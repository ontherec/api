package kr.ontherec.api.modules.place.application;

import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.place.entity.Place;
import kr.ontherec.api.modules.tag.entity.Tag;
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
        assertThat(isHost).isTrue();
    }

    @DisplayName("플레이스 호스트 확인 성공 - 다른 호스트")
    @Test
    void isHostWithOtherHost() {
        // given
        Host me = hostFactory.create("test");
        Host host = hostFactory.create("host");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);

        // when
        boolean isHost = placeQueryService.isHost(place.getId(), me);

        // then
        assertThat(isHost).isFalse();
    }
}