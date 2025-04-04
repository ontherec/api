package kr.ontherec.api.infra.fixture;

import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.item.entity.*;
import kr.ontherec.api.modules.stage.application.StageCommandService;
import kr.ontherec.api.modules.stage.entity.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static kr.ontherec.api.modules.item.entity.HolidayType.설날;
import static kr.ontherec.api.modules.stage.entity.StageType.RECTANGLE;

@Component
public class StageFactory {
    @Autowired StageCommandService stageCommandService;

    public Stage create(Host host, String title, String brn, Set<Tag> tags) {
        Address newAddress = Address.builder()
                .zipcode("00000")
                .state("경기도")
                .city("수원시 장안구")
                .streetAddress("율전로")
                .detail(title)
                .latitude(new BigDecimal("000.0000000000"))
                .longitude(new BigDecimal("000.0000000000"))
                .build();

        Stage newStage = Stage.builder()
                .images(new ArrayList<>(List.of("https://ontherec.kr")))
                .brn(brn)
                .title(title)
                .address(newAddress)
                // introduction
                .content(title)
                .links(new HashSet<>(Set.of(Link.builder().url("https://ontherec.kr").build())))
                // area
                .minCapacity(60)
                .maxCapacity(120)
                .stageType(RECTANGLE)
                .stageWidth(BigDecimal.valueOf(10.5))
                .stageHeight(BigDecimal.valueOf(5))
                // business
                .holidays(new HashSet<>(Set.of(Holiday.builder().type(설날).build())))
                .bookingFrom(Duration.ofDays(30))
                .bookingUntil(Duration.ofDays(1))
                .refundPolicies(
                        new HashSet<>(Set.of(RefundPolicy.builder()
                                .dayBefore(Duration.ofDays(30))
                                .percent(BigDecimal.valueOf(33.3))
                                .build())))
                // engineering
                .stageManagingAvailable(false)
                .stageManagingFee(null)
                .soundEngineeringAvailable(true)
                .soundEngineeringFee(100000L)
                .lightEngineeringAvailable(false)
                .lightEngineeringFee(null)
                .photographingAvailable(true)
                .photographingFee(100000L)
                // documents
                .applicationForm(null)
                .cueSheetTemplate("https://docs.google.com/document")
                .cueSheetDue(Duration.ofDays(3))
                // parking
                .parkingCapacity(2)
                .parkingLocation("건물 뒤편")
                .freeParking(true)
                // facilities
                .hasElevator(false)
                .hasRestroom(true)
                .hasWifi(true)
                .hasCameraStanding(true)
                .hasWaitingRoom(false)
                .hasProjector(true)
                .hasLocker(false)
                // fnb policies
                .allowsWater(true)
                .allowsDrink(false)
                .allowsFood(false)
                .allowsFoodDelivery(false)
                .allowsAlcohol(false)
                .sellDrink(true)
                .sellAlcohol(false)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        return stageCommandService.register(host, newStage, tags == null ? new HashSet<>() : tags);
    }
}
