package kr.ontherec.api.domain.place.application;

import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.place.domain.Place;
import kr.ontherec.api.domain.place.dto.AddressRegisterRequestDto;
import kr.ontherec.api.domain.place.dto.PlaceRegisterRequestDto;
import kr.ontherec.api.domain.place.dto.PlaceUpdateRequestDto;
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

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Set;

import static kr.ontherec.api.domain.place.domain.HolidayType.설날;
import static kr.ontherec.api.domain.place.domain.HolidayType.추석;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@UnitTest
class PlaceCommandServiceTest {

    @Autowired private HostFactory hostFactory;
    @Autowired private TagFactory tagFactory;
    @Autowired private PlaceFactory placeFactory;

    @Autowired private PlaceCommandService placeCommandService;

    private final PlaceMapper placeMapper = PlaceMapper.INSTANCE;

    @DisplayName("플레이스 등록 성공")
    @Test
    void register() {
        // given
        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");

        AddressRegisterRequestDto addressDto = new AddressRegisterRequestDto(
                "00000",
                "경기도",
                "수원시 장안구",
                "율전로",
                null,
                new BigDecimal("000.0000000000"),
                new BigDecimal("000.0000000000")
        );

        PlaceRegisterRequestDto placeDto = new PlaceRegisterRequestDto(
                "0000000000",
                addressDto,
                new PlaceRegisterRequestDto.Introduction(
                        "place",
                        "place",
                        Set.of("tag"),
                        Set.of("https://ontherec.kr")
                ),
                new PlaceRegisterRequestDto.Business(
                        Duration.ofDays(30),
                        Duration.ofDays(1),
                        Set.of(설날)
                ),
                new PlaceRegisterRequestDto.Parking(
                        2,
                        "건물 뒤편",
                        true
                )
        );

        Place newPlace = placeMapper.registerRequestDtoToEntity(placeDto);

        // when
        Place place = placeCommandService.register(host, newPlace, tags);

        // then
        assertThat(place.getBrn()).isEqualTo(newPlace.getBrn());
    }

    @DisplayName("플레이스 소개 수정 성공")
    @Test
    void updateIntroduction() {
        // given
        Host host = hostFactory.create("test");
        Place place = placeFactory.create(host, "place", "0000000000", null);
        PlaceUpdateRequestDto.Introduction dto = new PlaceUpdateRequestDto.Introduction(
                "newPlace",
                "newPlace",
                null,
                Set.of("https://ontherec.live")
        );
        Set<Tag> tags = tagFactory.create("newTag");

        // when
        placeCommandService.updateIntroduction(place.getId(), dto, tags);

        // then
        assertThat(place.getTitle()).isEqualTo(dto.title());
        assertThat(place.getContent()).isEqualTo(dto.content());
    }

    @DisplayName("플레이스 영업 정보 수정 성공")
    @Test
    void updateBusiness() {
        // given
        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);
        PlaceUpdateRequestDto.Business dto = new PlaceUpdateRequestDto.Business(
                Duration.ofDays(90),
                Duration.ofDays(7),
                Set.of(추석)
        );

        // when
        placeCommandService.updateBusiness(place.getId(), dto);

        // then
        assertThat(place.getBookingFrom()).isEqualTo(dto.bookingFrom());
        assertThat(place.getBookingUntil()).isEqualTo(dto.bookingUntil());
    }

    @DisplayName("플레이스 주차 정보 수정 성공")
    @Test
    void updateParking() {
        // given
        Host host = hostFactory.create("test");
        Set<Tag> tags = tagFactory.create("tag");
        Place place = placeFactory.create(host, "place", "0000000000", tags);
        PlaceUpdateRequestDto.Parking dto = new PlaceUpdateRequestDto.Parking(
                30,
                "건물 건너편 주차장",
                false
        );

        // when
        placeCommandService.updateParking(place.getId(), dto);

        // then
        assertThat(place.getParkingCapacity()).isEqualTo(dto.capacity());
        assertThat(place.getParkingLocation()).isEqualTo(dto.location());
        assertThat(place.getFreeParking()).isEqualTo(dto.free());
    }

    @DisplayName("플레이스 삭제 성공")
    @Test
    void delete() {
        // given
        Host host = hostFactory.create("test");
        Place place = placeFactory.create(host, "place", "0000000000", null);

        // when
        placeCommandService.delete(place.getId());

        // then
        Throwable throwable = catchThrowable(() -> placeCommandService.delete(place.getId()));

        assertThat(throwable)
                .isInstanceOf(PlaceException.class)
                .hasMessage(PlaceExceptionCode.NOT_FOUND.getMessage());
    }
}