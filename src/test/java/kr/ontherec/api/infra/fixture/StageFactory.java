package kr.ontherec.api.infra.fixture;

import kr.ontherec.api.modules.item.entity.RefundPolicy;
import kr.ontherec.api.modules.place.entity.Place;
import kr.ontherec.api.modules.stage.application.StageCommandService;
import kr.ontherec.api.modules.stage.entity.Stage;
import kr.ontherec.api.modules.tag.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

import static kr.ontherec.api.modules.stage.entity.StageType.RECTANGLE;

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
                .content(title)
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
