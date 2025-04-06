package kr.ontherec.api.modules.stage.application;

import kr.ontherec.api.infra.UnitTest;
import kr.ontherec.api.infra.fixture.HostFactory;
import kr.ontherec.api.infra.fixture.StageFactory;
import kr.ontherec.api.infra.model.BaseEntity;
import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.stage.entity.Stage;
import kr.ontherec.api.modules.stage.exception.StageException;
import kr.ontherec.api.modules.stage.exception.StageExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@UnitTest
class StageQueryServiceTest {

    @Autowired private HostFactory hostFactory;
    @Autowired private StageFactory stageFactory;

    @Autowired private StageQueryService stageQueryService;

    @DisplayName("공연장 검색 성공")
    @Test
    void search() {
        // given
        Host host = hostFactory.create("test");
        Stage stage = stageFactory.create(host, "stage", "0000000000");
        Map<String, String> params = new HashMap<>();
        params.put("q", "stage");
        params.put("minCapacity", "30");
        params.put("parkingAvailable", "true");
        params.put("stageManagingAvailable", "false");

        // when
        List<Stage> stages = stageQueryService.search(params ,
                PageRequest.of(0, 12, Sort.sort(Stage.class).by(BaseEntity::getCreatedAt).descending()),
                "test"
        );

        // then
        assertThat(stages.contains(stage)).isTrue();
    }

    @DisplayName("공연장 검색 실패 - 미지원 필터")
    @Test
    void searchWithUnSupportParams() {
        // given
        Host host = hostFactory.create("test");
        Stage stage = stageFactory.create(host, "stage", "0000000000");
        Map<String, String> params = new HashMap<>();
        params.put("newFilter", "value");

        // when
        Throwable throwable = catchThrowable(() -> stageQueryService.search(
                params,
                PageRequest.of(0, 12, Sort.sort(Stage.class).by(BaseEntity::getCreatedAt).descending()),
                "test"
        ));

        // then
        assertThat(throwable)
                .isInstanceOf(StageException.class)
                .hasMessage(StageExceptionCode.NOT_SUPPORT_FILTER.getMessage());
    }

    @DisplayName("공연장 검색 실패 - 유효하지 않은 필터")
    @Test
    void searchWithInvalidParams() {
        // given
        Host host = hostFactory.create("test");
        Stage stage = stageFactory.create(host, "stage", "0000000000");
        Map<String, String> params = new HashMap<>();
        params.put("stageManagingAvailable", "5");

        // when
        Throwable throwable = catchThrowable(() -> stageQueryService.search(
                params,
                PageRequest.of(0, 12, Sort.sort(Stage.class).by(BaseEntity::getCreatedAt).descending()),
                "test"
        ));

        // then
        assertThat(throwable)
                .isInstanceOf(StageException.class)
                .hasMessage(StageExceptionCode.NOT_VALID_FILTER.getMessage());
    }

    @DisplayName("공연장 조회 성공")
    @Test
    void get() {
        // given
        Host host = hostFactory.create("test");
        Stage stage = stageFactory.create(host, "stage", "0000000000");

        // when
        Stage foundStage = stageQueryService.get(stage.getId());

        // then
        assertThat(foundStage.getId()).isEqualTo(stage.getId());

    }

    @DisplayName("공연장 호스트 확인 성공 - 호스트")
    @Test
    void isHost() {
        // given
        Host host = hostFactory.create("test");
        Stage stage = stageFactory.create(host, "stage", "0000000000");

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
        Stage stage = stageFactory.create(host, "stage", "0000000000");

        // when
        boolean isHost = stageQueryService.isHost(stage.getId(), me);

        // then
        assertThat(isHost).isFalse();
    }
}