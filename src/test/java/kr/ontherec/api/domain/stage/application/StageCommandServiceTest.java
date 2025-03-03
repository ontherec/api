package kr.ontherec.api.domain.stage.application;

import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.item.domain.RefundPolicy;
import kr.ontherec.api.domain.place.domain.Place;
import kr.ontherec.api.domain.stage.domain.Stage;
import kr.ontherec.api.domain.stage.dto.RefundPolicyRegisterRequestDto;
import kr.ontherec.api.domain.stage.dto.RefundPolicyUpdateRequestDto;
import kr.ontherec.api.domain.stage.dto.StageRegisterRequestDto;
import kr.ontherec.api.domain.stage.dto.StageUpdateRequestDto;
import kr.ontherec.api.domain.stage.exception.StageException;
import kr.ontherec.api.domain.stage.exception.StageExceptionCode;
import kr.ontherec.api.domain.tag.application.TagService;
import kr.ontherec.api.domain.tag.domain.Tag;
import kr.ontherec.api.infra.UnitTest;
import kr.ontherec.api.infra.fixture.HostFactory;
import kr.ontherec.api.infra.fixture.PlaceFactory;
import kr.ontherec.api.infra.fixture.StageFactory;
import kr.ontherec.api.infra.fixture.TagFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

import static kr.ontherec.api.domain.stage.domain.StageType.RECTANGLE;
import static kr.ontherec.api.domain.stage.domain.StageType.T;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@UnitTest
class StageCommandServiceTest {

    @Autowired private HostFactory hostFactory;
    @Autowired private TagFactory tagFactory;
    @Autowired private PlaceFactory placeFactory;
    @Autowired private StageFactory stageFactory;

    @Autowired private TagService tagService;
    @Autowired private StageCommandService stageCommandService;
    @Autowired private StageQueryService stageQueryService;

    private final StageMapper stageMapper = StageMapper.INSTANCE;

    @DisplayName("공연장 등록 성공")
    @Test
    void register() {
        // given
        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);
        StageRegisterRequestDto dto = new StageRegisterRequestDto(
                place.getId(),
                // location
                "stage",
                -1,
                false,
                // introduction
                "stage",
                "stage",
                null,
                // area
                60,
                120,
                RECTANGLE,
                BigDecimal.valueOf(10.5),
                BigDecimal.valueOf(5),
                // business
                Set.of(new RefundPolicyRegisterRequestDto(
                        Duration.ofDays(30),
                        BigDecimal.valueOf(33.3)
                )),
                // engineering
                false,
                null,
                true,
                100000L,
                false,
                null,
                true,
                100000L,
                // documents
                null,
                "https://docs.google.com/document",
                Duration.ofDays(3),
                // facilities
                true,
                true,
                true,
                false,
                true,
                false,
                // fnb policies
                true,
                false,
                false,
                false,
                false,
                true,
                false
        );
        Stage newStage = stageMapper.registerRequestDtoToEntity(dto);

        // when
        Stage stage = stageCommandService.register(place, newStage, tags);

        // then
        assertThat(stage.getTitle()).isEqualTo(newStage.getTitle());
        assertThat(stage.getFloor()).isEqualTo(newStage.getFloor());
    }

    @DisplayName("공연장 이름 수정 성공")
    @Test
    void updateTitle() {
        // given
        Host host = hostFactory.create("test");
        Place place = placeFactory.create(host, "place", "0000000000", null);
        Stage stage = stageFactory.create(place, "stage", null);
        StageUpdateRequestDto.Title dto = new StageUpdateRequestDto.Title("newStage");

        // when
        stageCommandService.updateTitle(stage.getId(), dto);

        // then
        Stage foundStage = stageQueryService.get(stage.getId());

        assertThat(foundStage.getTitle()).isEqualTo(dto.title());
    }

    @DisplayName("공연장 이름 수정 실패 - 등록되지 않은 공연장")
    @Test
    void updateTitleWithUnregisteredId() {
        // given
        Host host = hostFactory.create("test");
        placeFactory.create(host, "place", "0000000000", null);
        StageUpdateRequestDto.Title dto = new StageUpdateRequestDto.Title("newStage");

        // when
        Throwable throwable = catchThrowable(() -> stageCommandService.updateTitle(1L, dto));

        // then
        assertThat(throwable)
                .isInstanceOf(StageException.class)
                .hasMessage(StageExceptionCode.NOT_FOUND.getMessage());
    }

    @DisplayName("공연장 소개 수정 성공")
    @Test
    void updateIntroduction() {
        // given
        Host host = hostFactory.create("test");
        Place place = placeFactory.create(host, "place", "0000000000", null);
        Stage stage = stageFactory.create(place, "stage", null);
        StageUpdateRequestDto.Introduction dto = new StageUpdateRequestDto.Introduction(
                "newStage",
                "newStage",
                Set.of("newTag")
        );

        Set<Tag> tags = dto.tags() == null ? null : dto.tags()
                .stream()
                .map(s -> Tag.builder().title(s).build())
                .map(tagService::getOrCreate)
                .collect(Collectors.toSet());

        // when
        stageCommandService.updateIntroduction(stage.getId(), dto, tags);

        // then
        Stage foundStage = stageQueryService.get(stage.getId());

        assertThat(foundStage.getIntroduction()).isEqualTo(dto.introduction());
        assertThat(foundStage.getGuide()).isEqualTo(dto.guide());
    }

    @DisplayName("공연장 면적 정보 수정 성공")
    @Test
    void updateArea() {
        // given
        Host host = hostFactory.create("test");
        Place place = placeFactory.create(host, "place", "0000000000", null);
        Stage stage = stageFactory.create(place, "stage", null);
        StageUpdateRequestDto.Area dto = new StageUpdateRequestDto.Area(
                100,
                200,
                T,
                BigDecimal.valueOf(12),
                BigDecimal.valueOf(6)
        );

        // when
        stageCommandService.updateArea(stage.getId(), dto);

        // then
        Stage foundStage = stageQueryService.get(stage.getId());

        assertThat(foundStage.getMinCapacity()).isEqualTo(dto.minCapacity());
        assertThat(foundStage.getMaxCapacity()).isEqualTo(dto.maxCapacity());
        assertThat(foundStage.getStageType()).isEqualTo(dto.stageType());
        assertThat(foundStage.getStageWidth()).isEqualTo(dto.stageWidth());
        assertThat(foundStage.getStageHeight()).isEqualTo(dto.stageHeight());
    }

    @DisplayName("공연장 영업 정보 수정 성공")
    @Test
    void updateBusiness() {
        // given
        Host host = hostFactory.create("test");
        Place place = placeFactory.create(host, "place", "0000000000", null);
        Stage stage = stageFactory.create(place, "stage", null);
        StageUpdateRequestDto.Business dto = new StageUpdateRequestDto.Business(
                Set.of(new RefundPolicyUpdateRequestDto(
                        stage.getRefundPolicies().stream().toList().get(0).getId(),
                        Duration.ofDays(15),
                        BigDecimal.valueOf(30)
                ))
        );

        // when
        stageCommandService.updateBusiness(stage.getId(), dto);

        // then
        Stage foundStage = stageQueryService.get(stage.getId());
        RefundPolicy foundRefundPolicy = foundStage.getRefundPolicies().stream().toList().get(0);
        RefundPolicyUpdateRequestDto refundPolicyUpdateRequestDto = dto.refundPolicies().stream().toList().get(0);

        assertThat(foundRefundPolicy.getDayBefore()).isEqualTo(refundPolicyUpdateRequestDto.dayBefore());
        assertThat(foundRefundPolicy.getPercent()).isEqualTo(refundPolicyUpdateRequestDto.percent());
    }

    @DisplayName("공연장 엔지니어링 정보 수정 성공")
    @Test
    void updateEngineering() {
        // given
        Host host = hostFactory.create("test");
        Place place = placeFactory.create(host, "place", "0000000000", null);
        Stage stage = stageFactory.create(place, "stage", null);
        StageUpdateRequestDto.Engineering dto = new StageUpdateRequestDto.Engineering(
                true,
                50000L,
                true,
                100000L,
                true,
                100000L,
                true,
                100000L
        );

        // when
        stageCommandService.updateEngineering(stage.getId(), dto);

        // then
        Stage foundStage = stageQueryService.get(stage.getId());

        assertThat(foundStage.isStageManagingAvailable()).isEqualTo(dto.stageManagingAvailable());
        assertThat(foundStage.getStageManagingFee()).isEqualTo(dto.stageManagingFee());
        assertThat(foundStage.isSoundEngineeringAvailable()).isEqualTo(dto.soundEngineeringAvailable());
        assertThat(foundStage.getSoundEngineeringFee()).isEqualTo(dto.soundEngineeringFee());
        assertThat(foundStage.isLightEngineeringAvailable()).isEqualTo(dto.lightEngineeringAvailable());
        assertThat(foundStage.getLightEngineeringFee()).isEqualTo(dto.lightEngineeringFee());
        assertThat(foundStage.isPhotographingAvailable()).isEqualTo(dto.photographingAvailable());
        assertThat(foundStage.getPhotographingFee()).isEqualTo(dto.photographingFee());
    }

    @DisplayName("공연장 문서 정보 수정 성공")
    @Test
    void updateDocuments() {
        // given
        Host host = hostFactory.create("test");
        Place place = placeFactory.create(host, "place", "0000000000", null);
        Stage stage = stageFactory.create(place, "stage", null);
        StageUpdateRequestDto.Documents dto = new StageUpdateRequestDto.Documents(
                "https://docs.google.com/document/u/0",
                "https://docs.google.com/document/u/0",
                Duration.ofDays(7)
        );

        // when
        stageCommandService.updateDocuments(stage.getId(), dto);

        // then
        Stage foundStage = stageQueryService.get(stage.getId());

        assertThat(foundStage.getApplicationForm()).isEqualTo(dto.applicationForm());
        assertThat(foundStage.getCueSheetTemplate()).isEqualTo(dto.cueSheetTemplate());
        assertThat(foundStage.getCueSheetDue()).isEqualTo(dto.cueSheetDue());
    }

    @DisplayName("공연장 편의시설 정보 수정 성공")
    @Test
    void updateFacilities() {
        // given
        Host host = hostFactory.create("test");
        Place place = placeFactory.create(host, "place", "0000000000", null);
        Stage stage = stageFactory.create(place, "stage", null);
        StageUpdateRequestDto.Facilities dto = new StageUpdateRequestDto.Facilities(
                true,
                true,
                true,
                true,
                true,
                true
        );

        // when
        stageCommandService.updateFacilities(stage.getId(), dto);

        // then
        Stage foundStage = stageQueryService.get(stage.getId());

        assertThat(foundStage.isHasRestroom()).isEqualTo(dto.hasRestroom());
        assertThat(foundStage.isHasWifi()).isEqualTo(dto.hasWifi());
        assertThat(foundStage.isHasCameraStanding()).isEqualTo(dto.hasCameraStanding());
        assertThat(foundStage.isHasWaitingRoom()).isEqualTo(dto.hasWaitingRoom());
        assertThat(foundStage.isHasProjector()).isEqualTo(dto.hasProjector());
        assertThat(foundStage.isHasLocker()).isEqualTo(dto.hasLocker());
    }

    @DisplayName("공연장 식음료 정책 수정 성공")
    @Test
    void updateFnbPolicies() {
        // given
        Host host = hostFactory.create("test");
        Place place = placeFactory.create(host, "place", "0000000000", null);
        Stage stage = stageFactory.create(place, "stage", null);
        StageUpdateRequestDto.FnbPolicies dto = new StageUpdateRequestDto.FnbPolicies(
                false,
                false,
                false,
                true,
                false,
                true,
                true
        );

        // when
        stageCommandService.updateFnbPolicies(stage.getId(), dto);

        // then
        Stage foundStage = stageQueryService.get(stage.getId());

        assertThat(foundStage.isAllowsWater()).isEqualTo(dto.allowsWater());
        assertThat(foundStage.isAllowsDrink()).isEqualTo(dto.allowsDrink());
        assertThat(foundStage.isAllowsFood()).isEqualTo(dto.allowsFood());
        assertThat(foundStage.isAllowsFoodDelivery()).isEqualTo(dto.allowsFoodDelivery());
        assertThat(foundStage.isAllowsAlcohol()).isEqualTo(dto.allowsAlcohol());
        assertThat(foundStage.isSellDrink()).isEqualTo(dto.sellDrink());
        assertThat(foundStage.isSellAlcohol()).isEqualTo(dto.sellAlcohol());
    }

    @DisplayName("공연장 삭제 성공")
    @Test
    void delete() {
        // given
        Host host = hostFactory.create("test");
        Place place = placeFactory.create(host, "place", "0000000000", null);
        Stage stage = stageFactory.create(place, "stage", null);

        // when
        stageCommandService.delete(stage.getId());

        // then
        Throwable throwable = catchThrowable(() -> stageQueryService.get(stage.getId()));

        assertThat(throwable)
                .isInstanceOf(StageException.class)
                .hasMessage(StageExceptionCode.NOT_FOUND.getMessage());
    }

    @DisplayName("공연장 삭제 실패 - 등록되지 않은 공연장")
    @Test
    void deleteWithUnregisteredId() {
        // given
        hostFactory.create("test");

        // when
        Throwable throwable = catchThrowable(() -> stageCommandService.delete(1L));

        // then
        assertThat(throwable)
                .isInstanceOf(StageException.class)
                .hasMessage(StageExceptionCode.NOT_FOUND.getMessage());
    }
}