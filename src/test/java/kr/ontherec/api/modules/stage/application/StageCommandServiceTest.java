package kr.ontherec.api.modules.stage.application;

import kr.ontherec.api.infra.UnitTest;
import kr.ontherec.api.infra.fixture.HostFactory;
import kr.ontherec.api.infra.fixture.PlaceFactory;
import kr.ontherec.api.infra.fixture.StageFactory;
import kr.ontherec.api.infra.fixture.TagFactory;
import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.item.entity.RefundPolicy;
import kr.ontherec.api.modules.place.entity.Place;
import kr.ontherec.api.modules.stage.dto.RefundPolicyRegisterRequestDto;
import kr.ontherec.api.modules.stage.dto.RefundPolicyUpdateRequestDto;
import kr.ontherec.api.modules.stage.dto.StageRegisterRequestDto;
import kr.ontherec.api.modules.stage.dto.StageUpdateRequestDto;
import kr.ontherec.api.modules.stage.entity.Stage;
import kr.ontherec.api.modules.stage.exception.StageException;
import kr.ontherec.api.modules.stage.exception.StageExceptionCode;
import kr.ontherec.api.modules.tag.entity.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Set;

import static kr.ontherec.api.modules.stage.entity.StageType.RECTANGLE;
import static kr.ontherec.api.modules.stage.entity.StageType.T;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@UnitTest
class StageCommandServiceTest {

    @Autowired private HostFactory hostFactory;
    @Autowired private TagFactory tagFactory;
    @Autowired private PlaceFactory placeFactory;
    @Autowired private StageFactory stageFactory;

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
                new StageRegisterRequestDto.Introduction(
                        "stage",
                        "stage",
                        "stage",
                        null
                ),
                new StageRegisterRequestDto.Location(
                        -1,
                        false
                ),
                new StageRegisterRequestDto.Area(
                        60,
                        120,
                        RECTANGLE,
                        BigDecimal.valueOf(10.5),
                        BigDecimal.valueOf(5)
                ),
                new StageRegisterRequestDto.Business(
                        Set.of(new RefundPolicyRegisterRequestDto(
                                Duration.ofDays(30),
                                BigDecimal.valueOf(33.3)
                        ))
                ),
                new StageRegisterRequestDto.Engineering(
                        false,
                        null,
                        true,
                        100000L,
                        false,
                        null,
                        true,
                        100000L
                ),
                new StageRegisterRequestDto.Documents(
                        "https://docs.google.com/document",
                        "https://docs.google.com/document",
                        Duration.ofDays(3)
                ),
                new StageRegisterRequestDto.Facilities(
                        true,
                        true,
                        true,
                        false,
                        true,
                        false
                ),
                new StageRegisterRequestDto.FnbPolicies(
                        true,
                        false,
                        false,
                        false,
                        false,
                        true,
                        false
                )
        );
        Stage newStage = stageMapper.registerRequestDtoToEntity(dto);

        // when
        Stage stage = stageCommandService.register(place, newStage, tags);

        // then
        assertThat(stage.getTitle()).isEqualTo(newStage.getTitle());
        assertThat(stage.getFloor()).isEqualTo(newStage.getFloor());
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
                "newStage",
                null
        );
        Set<Tag> tags = tagFactory.create("tag");

        // when
        stageCommandService.updateIntroduction(stage.getId(), dto, tags);

        // then
        assertThat(stage.getTitle()).isEqualTo(dto.title());
        assertThat(stage.getContent()).isEqualTo(dto.content());
        assertThat(stage.getGuide()).isEqualTo(dto.guide());
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
        assertThat(stage.getMinCapacity()).isEqualTo(dto.minCapacity());
        assertThat(stage.getMaxCapacity()).isEqualTo(dto.maxCapacity());
        assertThat(stage.getStageType()).isEqualTo(dto.stageType());
        assertThat(stage.getStageWidth()).isEqualTo(dto.stageWidth());
        assertThat(stage.getStageHeight()).isEqualTo(dto.stageHeight());
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
        RefundPolicy foundRefundPolicy = stage.getRefundPolicies().stream().toList().get(0);
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
        assertThat(stage.isStageManagingAvailable()).isEqualTo(dto.stageManagingAvailable());
        assertThat(stage.getStageManagingFee()).isEqualTo(dto.stageManagingFee());
        assertThat(stage.isSoundEngineeringAvailable()).isEqualTo(dto.soundEngineeringAvailable());
        assertThat(stage.getSoundEngineeringFee()).isEqualTo(dto.soundEngineeringFee());
        assertThat(stage.isLightEngineeringAvailable()).isEqualTo(dto.lightEngineeringAvailable());
        assertThat(stage.getLightEngineeringFee()).isEqualTo(dto.lightEngineeringFee());
        assertThat(stage.isPhotographingAvailable()).isEqualTo(dto.photographingAvailable());
        assertThat(stage.getPhotographingFee()).isEqualTo(dto.photographingFee());
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
        assertThat(stage.getApplicationForm()).isEqualTo(dto.applicationForm());
        assertThat(stage.getCueSheetTemplate()).isEqualTo(dto.cueSheetTemplate());
        assertThat(stage.getCueSheetDue()).isEqualTo(dto.cueSheetDue());
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
        assertThat(stage.isHasRestroom()).isEqualTo(dto.hasRestroom());
        assertThat(stage.isHasWifi()).isEqualTo(dto.hasWifi());
        assertThat(stage.isHasCameraStanding()).isEqualTo(dto.hasCameraStanding());
        assertThat(stage.isHasWaitingRoom()).isEqualTo(dto.hasWaitingRoom());
        assertThat(stage.isHasProjector()).isEqualTo(dto.hasProjector());
        assertThat(stage.isHasLocker()).isEqualTo(dto.hasLocker());
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
        assertThat(stage.isAllowsWater()).isEqualTo(dto.allowsWater());
        assertThat(stage.isAllowsDrink()).isEqualTo(dto.allowsDrink());
        assertThat(stage.isAllowsFood()).isEqualTo(dto.allowsFood());
        assertThat(stage.isAllowsFoodDelivery()).isEqualTo(dto.allowsFoodDelivery());
        assertThat(stage.isAllowsAlcohol()).isEqualTo(dto.allowsAlcohol());
        assertThat(stage.isSellDrink()).isEqualTo(dto.sellDrink());
        assertThat(stage.isSellAlcohol()).isEqualTo(dto.sellAlcohol());
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
}