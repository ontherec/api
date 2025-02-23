package kr.ontherec.api.infra.fixture;

import kr.ontherec.api.domain.place.domain.Address;
import kr.ontherec.api.domain.place.domain.Place;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

public class PlaceGenerator {

    public static Place generate(String name, String brn) {
        Address newAddress = Address.builder()
                .zipcode("00000")
                .state("경기도")
                .city("수원시 장안구")
                .streetAddress("율전로")
                .detail(null)
                .latitude(new BigDecimal("000.0000000000"))
                .longitude(new BigDecimal("000.0000000000"))
                .build();

        return Place.builder()
                .brn(brn)
                .name(name)
                .address(newAddress)
                .introduction(name)
                .keywords(null)
                .links(null)
                .bookingFrom(Duration.ofDays(30))
                .bookingUntil(Duration.ofDays(1))
                .holidays(null)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }
}
