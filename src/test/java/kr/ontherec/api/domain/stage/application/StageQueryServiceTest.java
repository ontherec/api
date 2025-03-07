package kr.ontherec.api.domain.stage.application;

import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.place.domain.Place;
import kr.ontherec.api.domain.stage.domain.Stage;
import kr.ontherec.api.domain.tag.domain.Tag;
import kr.ontherec.api.infra.UnitTest;
import kr.ontherec.api.infra.fixture.HostFactory;
import kr.ontherec.api.infra.fixture.PlaceFactory;
import kr.ontherec.api.infra.fixture.StageFactory;
import kr.ontherec.api.infra.fixture.TagFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@UnitTest
class StageQueryServiceTest {

    @Autowired private StageQueryService stageQueryService;
    @Autowired private PlaceFactory placeFactory;
    @Autowired private HostFactory hostFactory;
    @Autowired private StageFactory stageFactory;
    @Autowired
    private TagFactory tagFactory;

    @DisplayName("공연장 검색 성공")
    @Test
    void search() {
        // given
        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);
        Stage stage = stageFactory.create(place, "stage", tags);

        // when
        List<Stage> stages = stageQueryService.search("stage");

        // then
        assertThat(stages.contains(stage)).isTrue();
    }

    @DisplayName("공연장 조회 성공")
    @Test
    void get() {
        // given
        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);
        Stage stage = stageFactory.create(place, "stage", tags);

        // when
        Stage foundStage = stageQueryService.get(stage.getId());

        // then
        assertThat(foundStage.getId()).isEqualTo(stage.getId());

    }

    @DisplayName("공연장 호스트 확인 성공")
    @Test
    void isHost() {
        // given
        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);
        Stage stage = stageFactory.create(place, "stage", tags);

        // when
        boolean isHost = stageQueryService.isHost(stage.getId(), host);

        // then
        assertThat(isHost).isTrue();
    }

    @DisplayName("공연장 호스트 확인 성공 - 다른 호스트")
    @Test
    void isHostWithOtherHost() {
        // given
        Host me = hostFactory.create("test");
        Host host = hostFactory.create("host");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);
        Stage stage = stageFactory.create(place, "stage", tags);

        // when
        boolean isHost = stageQueryService.isHost(stage.getId(), me);

        // then
        assertThat(isHost).isFalse();
    }
}