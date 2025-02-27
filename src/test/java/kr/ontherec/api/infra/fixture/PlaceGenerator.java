package kr.ontherec.api.infra.fixture;

import kr.ontherec.api.domain.place.domain.Address;
import kr.ontherec.api.domain.place.domain.Holiday;
import kr.ontherec.api.domain.place.domain.Link;
import kr.ontherec.api.domain.place.domain.Place;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

import static kr.ontherec.api.domain.place.domain.HolidayType.설날;

public class PlaceGenerator {

    public static Place generate(String title, String brn) {
        Address newAddress = Address.builder()
                .zipcode("00000")
                .state("경기도")
                .city("수원시 장안구")
                .streetAddress("율전로")
                .detail(title)
                .latitude(new BigDecimal("000.0000000000"))
                .longitude(new BigDecimal("000.0000000000"))
                .build();

        return Place.builder()
                .brn(brn)
                .title(title)
                .address(newAddress)
                .introduction(title)
                .tags(null)
                .links(Set.of(Link.builder().url("https://ontherec.kr").build()))
                .bookingFrom(Duration.ofDays(30))
                .bookingUntil(Duration.ofDays(1))
                .holidays(Set.of(Holiday.builder().type(설날).build()))
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }
}
