package kr.ontherec.api.infra.fixture;

import kr.ontherec.api.domain.item.domain.RefundPolicy;
import kr.ontherec.api.domain.place.domain.Place;
import kr.ontherec.api.domain.stage.application.StageCommandService;
import kr.ontherec.api.domain.stage.domain.Stage;
import kr.ontherec.api.domain.tag.domain.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

import static kr.ontherec.api.domain.stage.domain.StageType.RECTANGLE;

@Component
public class StageFactory {
    @Autowired StageCommandService stageCommandService;

    public Stage create(Place place, String title, Set<Tag> tags) {
        Stage newStage = Stage.builder()
                .place(null)
                .title(title)
                .floor(-1)
                .hasElevator(false)
                // introduction
                .introduction(title)
                .guide(title)
                .tags(null)
                // area
                .minCapacity(60)
                .maxCapacity(120)
                .stageType(RECTANGLE)
                .stageWidth(BigDecimal.valueOf(10.5))
                .stageHeight(BigDecimal.valueOf(5))
                // business
                .refundPolicies(
                        Set.of(RefundPolicy.builder()
                                .dayBefore(Duration.ofDays(30))
                                .percent(BigDecimal.valueOf(33.3))
                                .build())
                )
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
                // facilities
                .parkingCapacity(2)
                .parkingLocation("건물 뒤편")
                .freeParking(true)
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

        return stageCommandService.register(place, newStage, tags);
    }
}
