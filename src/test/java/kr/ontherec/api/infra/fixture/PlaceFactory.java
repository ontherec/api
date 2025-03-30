package kr.ontherec.api.infra.fixture;

import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.place.application.PlaceCommandService;
import kr.ontherec.api.modules.place.entity.Address;
import kr.ontherec.api.modules.place.entity.Holiday;
import kr.ontherec.api.modules.place.entity.Link;
import kr.ontherec.api.modules.place.entity.Place;
import kr.ontherec.api.modules.tag.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

import static kr.ontherec.api.modules.place.entity.HolidayType.설날;

@Component
public class PlaceFactory {

    @Autowired private PlaceCommandService placeCommandService;

    public Place create(Host host, String title, String brn, Set<Tag> tags) {
        Address newAddress = Address.builder()
                .zipcode("00000")
                .state("경기도")
                .city("수원시 장안구")
                .streetAddress("율전로")
                .detail(title)
                .latitude(new BigDecimal("000.0000000000"))
                .longitude(new BigDecimal("000.0000000000"))
                .build();

        Place newPlace = Place.builder()
                .brn(brn)
                .title(title)
                .address(newAddress)
                .content(title)
                .tags(null)
                .links(Set.of(Link.builder().url("https://ontherec.kr").build()))
                .bookingFrom(Duration.ofDays(30))
                .bookingUntil(Duration.ofDays(1))
                .holidays(Set.of(Holiday.builder().type(설날).build()))
                .parkingCapacity(2)
                .parkingLocation("건물 뒤편")
                .freeParking(true)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        return placeCommandService.register(host, newPlace, tags);
    }
}
