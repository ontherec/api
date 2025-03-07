package kr.ontherec.api.domain.stage.application;

import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.place.domain.Place;
import kr.ontherec.api.domain.stage.domain.Stage;
import kr.ontherec.api.domain.stage.exception.StageException;
import kr.ontherec.api.domain.stage.exception.StageExceptionCode;
import kr.ontherec.api.infra.UnitTest;
import kr.ontherec.api.infra.fixture.HostFactory;
import kr.ontherec.api.infra.fixture.PlaceFactory;
import kr.ontherec.api.infra.fixture.StageFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@UnitTest
class StageQueryServiceTest {

    @Autowired private StageQueryService stageQueryService;
    @Autowired private PlaceFactory placeFactory;
    @Autowired private HostFactory hostFactory;
    @Autowired private StageFactory stageFactory;

    @DisplayName("공연장 검색 성공")
    @Test
    void search() {
        // given
        Host host = hostFactory.create("test");
        Place place = placeFactory.create(host, "place", "0000000000", null);
        Stage stage = stageFactory.create(place, "stage", null);

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
        Place place = placeFactory.create(host, "place", "0000000000", null);
        Stage stage = stageFactory.create(place, "stage", null);

        // when
        Stage foundStage = stageQueryService.get(stage.getId());

        // then
        assertThat(foundStage.getId()).isEqualTo(stage.getId());

    }

    @DisplayName("공연장 조회 실패 - 등록되지 않은 공연장")
    @Test
    void getWithUnregisteredId() {
        // given
        Host host = hostFactory.create("test");
        placeFactory.create(host, "place", "0000000000", null);

        // when
        Throwable throwable = catchThrowable(() -> stageQueryService.get(1L));

        // then
        assertThat(throwable)
                .isInstanceOf(StageException.class)
                .hasMessage(StageExceptionCode.NOT_FOUND.getMessage());
    }

    @DisplayName("공연장 호스트 확인 성공")
    @Test
    void isHost() {
        // given
        Host host = hostFactory.create("test");
        Place place = placeFactory.create(host, "place", "0000000000", null);
        Stage stage = stageFactory.create(place, "stage", null);

        // when
        boolean isHost = stageQueryService.isHost(stage.getId(), host);

        // then
        assertThat(isHost).isEqualTo(true);
    }

    @DisplayName("공연장 호스트 확인 실패 - 등록되지 않은 공연장")
    @Test
    void isHostWithUnregisteredId() {
        // given
        Host host = hostFactory.create("test");

        // when
        Throwable throwable = catchThrowable(() -> stageQueryService.isHost(1L, host));

        // then
        assertThat(throwable)
                .isInstanceOf(StageException.class)
                .hasMessage(StageExceptionCode.NOT_FOUND.getMessage());
    }
}