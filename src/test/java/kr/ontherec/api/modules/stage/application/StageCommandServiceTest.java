package kr.ontherec.api.modules.stage.application;

import kr.ontherec.api.infra.UnitTest;
import kr.ontherec.api.infra.fixture.HostFactory;
import kr.ontherec.api.infra.fixture.StageFactory;
import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.item.dto.AddressRegisterRequestDto;
import kr.ontherec.api.modules.stage.dto.*;
import kr.ontherec.api.modules.stage.entity.Stage;
import kr.ontherec.api.modules.stage.exception.StageException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static kr.ontherec.api.modules.item.entity.DOW.MON;
import static kr.ontherec.api.modules.item.entity.HolidayType.설날;
import static kr.ontherec.api.modules.item.entity.HolidayType.추석;
import static kr.ontherec.api.modules.stage.entity.StageType.RECTANGLE;
import static kr.ontherec.api.modules.stage.entity.StageType.T;
import static kr.ontherec.api.modules.stage.exception.StageExceptionCode.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@UnitTest
class StageCommandServiceTest {

    @Autowired private HostFactory hostFactory;
    @Autowired private StageFactory stageFactory;

    @Autowired private StageCommandService stageCommandService;
    @Autowired private StageQueryService stageQueryService;

    private final StageMapper stageMapper = StageMapper.INSTANCE;

    @DisplayName("공연장 등록 성공")
    @Test
    void register() {
        // given
        Host host = hostFactory.create("test");

        StageRegisterRequestDto dto = new StageRegisterRequestDto(
                List.of("https://d3j0mzt56d6iv2.cloudfront.net/images/o/test/logo-symbol.jpg"),
                "stage",
                "0000000000",
                new AddressRegisterRequestDto(
                    "00000",
                    "경기도",
                    "수원시 장안구",
                    "율전로",
                    null,
                    new BigDecimal("000.0000000000"),
                    new BigDecimal("000.0000000000")
                ),
                new StageRegisterRequestDto.Introduction(
                        "stage",
                        null,
                        Set.of("https://ontherec.kr")
                ),
                new StageRegisterRequestDto.Area(
                        60,
                        120,
                        RECTANGLE,
                        BigDecimal.valueOf(10.5),
                        BigDecimal.valueOf(5)
                ),
                new StageRegisterRequestDto.Business(
                        Set.of(설날),
                        Set.of(new TimeBlockCreateRequestDto(
                                MON,
                                LocalTime.NOON,
                                LocalTime.MAX,
                                Duration.ofHours(3),
                                BigDecimal.valueOf(300000),
                                BigDecimal.valueOf(20000)
                        )),
                        Duration.ofDays(30),
                        Duration.ofDays(1),
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
                new StageRegisterRequestDto.Parking(
                        2,
                        "건물 뒤편",
                        true
                ),
                new StageRegisterRequestDto.Facilities(
                        false,
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
        Stage stage = stageCommandService.register(host, newStage);

        // then
        assertThat(stage.getTitle()).isEqualTo(newStage.getTitle());
        assertThat(stage.getBrn()).isEqualTo(newStage.getBrn());
    }

    @DisplayName("공연장 등록 실패 - 중복된 사업자등록번호")
    @Test
    void registerWithDuplicatedBrn() {
        // given
        Host host = hostFactory.create("test");
        stageFactory.create(host, "stage", "0000000000");

        StageRegisterRequestDto dto = new StageRegisterRequestDto(
                List.of("https://d3j0mzt56d6iv2.cloudfront.net/images/o/test/logo-symbol.jpg"),
                "stage",
                "0000000000",
                new AddressRegisterRequestDto(
                        "00000",
                        "경기도",
                        "수원시 장안구",
                        "율전로",
                        null,
                        new BigDecimal("000.0000000000"),
                        new BigDecimal("000.0000000000")
                ),
                new StageRegisterRequestDto.Introduction(
                        "stage",
                        null,
                        Set.of("https://ontherec.kr")
                ),
                new StageRegisterRequestDto.Area(
                        60,
                        120,
                        RECTANGLE,
                        BigDecimal.valueOf(10.5),
                        BigDecimal.valueOf(5)
                ),
                new StageRegisterRequestDto.Business(
                        Set.of(설날),
                        Set.of(new TimeBlockCreateRequestDto(
                                MON,
                                LocalTime.NOON,
                                LocalTime.MAX,
                                Duration.ofHours(3),
                                BigDecimal.valueOf(300000),
                                BigDecimal.valueOf(20000)
                        )),
                        Duration.ofDays(30),
                        Duration.ofDays(1),
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
                new StageRegisterRequestDto.Parking(
                        2,
                        "건물 뒤편",
                        true
                ),
                new StageRegisterRequestDto.Facilities(
                        false,
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
        Throwable throwable = catchThrowable(() -> stageCommandService.register(host, newStage));

        // then
        assertThat(throwable)
                .isInstanceOf(StageException.class)
                .hasMessage(EXIST_BRN.getMessage());
    }

    @DisplayName("공연장 등록 실패 - 유효하지 않은 예약 기간")
    @Test
    void registerWithInvalidBookingPeriod() {
        // given
        hostFactory.create("test");
        StageRegisterRequestDto dto = new StageRegisterRequestDto(
                List.of("https://d3j0mzt56d6iv2.cloudfront.net/images/o/test/logo-symbol.jpg"),
                "stage",
                "0000000000",
                new AddressRegisterRequestDto(
                        "00000",
                        "경기도",
                        "수원시 장안구",
                        "율전로",
                        null,
                        new BigDecimal("000.0000000000"),
                        new BigDecimal("000.0000000000")
                ),
                new StageRegisterRequestDto.Introduction(
                        "stage",
                        null,
                        Set.of("https://ontherec.kr")
                ),
                new StageRegisterRequestDto.Area(
                        60,
                        120,
                        RECTANGLE,
                        BigDecimal.valueOf(10.5),
                        BigDecimal.valueOf(5)
                ),
                new StageRegisterRequestDto.Business(
                        Set.of(설날),
                        Set.of(new TimeBlockCreateRequestDto(
                                MON,
                                LocalTime.NOON,
                                LocalTime.MAX,
                                Duration.ofHours(3),
                                BigDecimal.valueOf(300000),
                                BigDecimal.valueOf(20000)
                        )),
                        Duration.ofDays(30),
                        Duration.ofDays(30),
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
                new StageRegisterRequestDto.Parking(
                        2,
                        "건물 뒤편",
                        true
                ),
                new StageRegisterRequestDto.Facilities(
                        false,
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

        // when
        Throwable throwable = catchThrowable(() -> stageMapper.registerRequestDtoToEntity(dto));

        // then
        assertThat(throwable)
                .isInstanceOf(StageException.class)
                .hasMessage(NOT_VALID_BOOKING_PERIOD.getMessage());
    }

    @DisplayName("공연장 등록 실패 - 유효하지 않은 엔지니어링 요금")
    @Test
    void registerWithInvalidEngineering() {
        // given
        hostFactory.create("test");
        StageRegisterRequestDto dto = new StageRegisterRequestDto(
                List.of("https://d3j0mzt56d6iv2.cloudfront.net/images/o/test/logo-symbol.jpg"),
                "stage",
                "0000000000",
                new AddressRegisterRequestDto(
                        "00000",
                        "경기도",
                        "수원시 장안구",
                        "율전로",
                        null,
                        new BigDecimal("000.0000000000"),
                        new BigDecimal("000.0000000000")
                ),
                new StageRegisterRequestDto.Introduction(
                        "stage",
                        null,
                        Set.of("https://ontherec.kr")
                ),
                new StageRegisterRequestDto.Area(
                        60,
                        120,
                        RECTANGLE,
                        BigDecimal.valueOf(10.5),
                        BigDecimal.valueOf(5)
                ),
                new StageRegisterRequestDto.Business(
                        Set.of(설날),
                        Set.of(new TimeBlockCreateRequestDto(
                                MON,
                                LocalTime.NOON,
                                LocalTime.MAX,
                                Duration.ofHours(3),
                                BigDecimal.valueOf(300000),
                                BigDecimal.valueOf(20000)
                        )),
                        Duration.ofDays(30),
                        Duration.ofDays(1),
                        Set.of(new RefundPolicyRegisterRequestDto(
                                Duration.ofDays(30),
                                BigDecimal.valueOf(33.3)
                        ))
                ),
                new StageRegisterRequestDto.Engineering(
                        false,
                        50000L,
                        true,
                        null,
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
                new StageRegisterRequestDto.Parking(
                        2,
                        "건물 뒤편",
                        true
                ),
                new StageRegisterRequestDto.Facilities(
                        false,
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

        // when
        Throwable throwable = catchThrowable(() -> stageMapper.registerRequestDtoToEntity(dto));

        // then
        assertThat(throwable)
                .isInstanceOf(StageException.class)
                .hasMessage(NOT_VALID_ENGINEERING_FEE.getMessage());
    }

    @DisplayName("공연장 이미지 수정 성공")
    @Test
    void updateImages() {
        // given
        Host host = hostFactory.create("test");
        Stage stage = stageFactory.create(host, "stage", "0000000000");
        StageUpdateRequestDto.Images dto = new StageUpdateRequestDto.Images(
                List.of("https://d3j0mzt56d6iv2.cloudfront.net/images/o/test/logo-symbol.jpg")
        );

        // when
        stageCommandService.updateImages(stage.getId(), dto);

        // then
        assertThat(stage.getImages().stream().toList().get(0)).isEqualTo(dto.images().stream().toList().get(0));
    }

    @DisplayName("공연장 소개 수정 성공")
    @Test
    void updateIntroduction() {
        // given
        Host host = hostFactory.create("test");
        Stage stage = stageFactory.create(host, "stage", "0000000000");
        StageUpdateRequestDto.Introduction dto = new StageUpdateRequestDto.Introduction(
                "newStage",
                null,
                Set.of("https://ontherec.live")
        );

        // when
        stageCommandService.updateIntroduction(stage.getId(), dto);

        // then
        assertThat(stage.getContent()).isEqualTo(dto.content());
    }

    @DisplayName("공연장 면적 정보 수정 성공")
    @Test
    void updateArea() {
        // given
        Host host = hostFactory.create("test");
        Stage stage = stageFactory.create(host, "stage", "0000000000");
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
        Stage stage = stageFactory.create(host, "stage", "0000000000");
        StageUpdateRequestDto.Business dto = new StageUpdateRequestDto.Business(
                Set.of(추석),
                Set.of(new TimeBlockUpdateRequestDto(
                        stage.getStageTimeBlocks().stream().toList().get(0).getId(),
                        MON,
                        LocalTime.MIDNIGHT,
                        LocalTime.MAX,
                        Duration.ofHours(7),
                        BigDecimal.valueOf(500000),
                        BigDecimal.valueOf(50000)
                )),
                Duration.ofDays(90),
                Duration.ofDays(7),
                Set.of(new RefundPolicyUpdateRequestDto(
                        stage.getRefundPolicies().stream().toList().get(0).getId(),
                        Duration.ofDays(15),
                        BigDecimal.valueOf(30)
                ))
        );

        // when
        stageCommandService.updateBusiness(stage.getId(), dto);

        // then
        assertThat(stage.getBookingFrom()).isEqualTo(dto.bookingFrom());
        assertThat(stage.getBookingUntil()).isEqualTo(dto.bookingUntil());
    }

    @DisplayName("공연장 영업 정보 수정 실패 - 유효하지 않은 예약 기간")
    @Test
    void updateBusinessWithInvalidBookingPeriod() {
        // given
        Host host = hostFactory.create("test");
        Stage stage = stageFactory.create(host, "stage", "0000000000");
        StageUpdateRequestDto.Business dto = new StageUpdateRequestDto.Business(
                Set.of(추석),
                Set.of(new TimeBlockUpdateRequestDto(
                        stage.getStageTimeBlocks().stream().toList().get(0).getId(),
                        MON,
                        LocalTime.MIDNIGHT,
                        LocalTime.MAX,
                        Duration.ofHours(7),
                        BigDecimal.valueOf(500000),
                        BigDecimal.valueOf(50000)
                )),
                Duration.ofDays(30),
                Duration.ofDays(30),
                Set.of(new RefundPolicyUpdateRequestDto(
                        stage.getRefundPolicies().stream().toList().get(0).getId(),
                        Duration.ofDays(30),
                        BigDecimal.valueOf(30)
                ))
        );

        // when
        Throwable throwable = catchThrowable(() -> stageCommandService.updateBusiness(stage.getId(), dto));

        // then
        assertThat(throwable)
                .isInstanceOf(StageException.class)
                .hasMessage(NOT_VALID_BOOKING_PERIOD.getMessage());
    }

    @DisplayName("공연장 엔지니어링 정보 수정 성공")
    @Test
    void updateEngineering() {
        // given
        Host host = hostFactory.create("test");
        Stage stage = stageFactory.create(host, "stage", "0000000000");
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

    @DisplayName("공연장 엔지니어링 정보 수정 실패 - 유효하지 않은 엔지니어링 요금")
    @Test
    void updateEngineeringWithInvalidEngineeringFee() {
        // given
        Host host = hostFactory.create("test");
        Stage stage = stageFactory.create(host, "stage", "0000000000");
        StageUpdateRequestDto.Engineering dto = new StageUpdateRequestDto.Engineering(
                false,
                50000L,
                true,
                null,
                false,
                null,
                true,
                100000L
        );

        // when
        Throwable throwable = catchThrowable(() -> stageCommandService.updateEngineering(stage.getId(), dto));

        // then
        assertThat(throwable)
                .isInstanceOf(StageException.class)
                .hasMessage(NOT_VALID_ENGINEERING_FEE.getMessage());
    }

    @DisplayName("공연장 문서 정보 수정 성공")
    @Test
    void updateDocuments() {
        // given
        Host host = hostFactory.create("test");
        Stage stage = stageFactory.create(host, "stage", "0000000000");
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

    @DisplayName("공연장 주차 정보 수정 성공")
    @Test
    void updateParking() {
        // given
        Host host = hostFactory.create("test");
        Stage stage = stageFactory.create(host, "stage", "0000000000");
        StageUpdateRequestDto.Parking dto = new StageUpdateRequestDto.Parking(
                30,
                "건물 건너편 주차장",
                false
        );

        // when
        stageCommandService.updateParking(stage.getId(), dto);

        // then
        assertThat(stage.getParkingCapacity()).isEqualTo(dto.capacity());
        assertThat(stage.getParkingLocation()).isEqualTo(dto.location());
        assertThat(stage.getFreeParking()).isEqualTo(dto.free());
    }

    @DisplayName("공연장 편의시설 정보 수정 성공")
    @Test
    void updateFacilities() {
        // given
        Host host = hostFactory.create("test");
        Stage stage = stageFactory.create(host, "stage", "0000000000");
        StageUpdateRequestDto.Facilities dto = new StageUpdateRequestDto.Facilities(
                false,
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
        assertThat(stage.isHasElevator()).isEqualTo(dto.hasElevator());
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
        Stage stage = stageFactory.create(host, "stage", "0000000000");
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

    @DisplayName("공연장 좋아요 성공")
    @Test
    void like() {
        // given
        String username = "test";
        Host host = hostFactory.create(username);
        Stage stage = stageFactory.create(host, "stage", "0000000000");

        // when
        stageCommandService.like(stage.getId(), username);

        // then
        assertThat(stage.getLikedUsernames().contains(username)).isTrue();
    }

    @DisplayName("공연장 좋아요 취소 성공")
    @Test
    void unlike() {
        // given
        String username = "test";
        Host host = hostFactory.create(username);
        Stage stage = stageFactory.create(host, "stage", "0000000000");

        // when
        stageCommandService.unlike(stage.getId(), username);

        // then
        assertThat(stage.getLikedUsernames().contains(username)).isFalse();
    }

    @DisplayName("공연장 삭제 성공")
    @Test
    void delete() {
        // given
        Host host = hostFactory.create("test");
        Stage stage = stageFactory.create(host, "stage", "0000000000");

        // when
        stageCommandService.delete(stage.getId());

        // then
        Throwable throwable = catchThrowable(() -> stageQueryService.get(stage.getId()));

        assertThat(throwable)
                .isInstanceOf(StageException.class)
                .hasMessage(NOT_FOUND.getMessage());
    }
}