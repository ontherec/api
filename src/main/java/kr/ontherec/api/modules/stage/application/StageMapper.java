package kr.ontherec.api.modules.stage.application;

import kr.ontherec.api.infra.config.MapperConfig;
import kr.ontherec.api.modules.item.entity.Holiday;
import kr.ontherec.api.modules.item.entity.HolidayType;
import kr.ontherec.api.modules.item.entity.Link;
import kr.ontherec.api.modules.stage.dto.StageRegisterRequestDto;
import kr.ontherec.api.modules.stage.dto.StageResponseDto;
import kr.ontherec.api.modules.stage.dto.StageUpdateRequestDto;
import kr.ontherec.api.modules.stage.entity.Stage;
import kr.ontherec.api.modules.tag.entity.Tag;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        config = MapperConfig.class,
        imports = LocalDateTime.class
)
public interface StageMapper {
    StageMapper INSTANCE = Mappers.getMapper(StageMapper.class);

    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "likeCount", expression = "java(0)")
    @Mapping(target = "viewCount", expression = "java(0)")
    @Mapping(source = "introduction", target = ".")
    @Mapping(source = "introduction.links", target = "links", qualifiedByName = "deserializeLinks")
    @Mapping(source = "area", target = ".")
    @Mapping(source = "business", target = ".")
    @Mapping(source = "engineering", target = ".")
    @Mapping(source = "documents", target = ".")
    @Mapping(source = "parking.capacity", target = "parkingCapacity")
    @Mapping(source = "parking.location", target = "parkingLocation")
    @Mapping(source = "parking.free", target = "freeParking")
    @Mapping(source = "facilities", target = ".")
    @Mapping(source = "fnbPolicies", target = ".")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    Stage registerRequestDtoToEntity(StageRegisterRequestDto dto);

    void updateImages(StageUpdateRequestDto.Images dto, @MappingTarget Stage stage);

    void updateArea(StageUpdateRequestDto.Area dto, @MappingTarget Stage stage);

    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "links", qualifiedByName = "deserializeLinks")
    void updateIntroduction(StageUpdateRequestDto.Introduction dto, @MappingTarget Stage stage);

    void updateBusiness(StageUpdateRequestDto.Business dto, @MappingTarget Stage stage);

    void updateEngineering(StageUpdateRequestDto.Engineering dto, @MappingTarget Stage stage);

    void updateDocuments(StageUpdateRequestDto.Documents dto, @MappingTarget Stage stage);

    @Mapping(source = "capacity", target = "parkingCapacity")
    @Mapping(source = "location", target = "parkingLocation")
    @Mapping(source = "free", target = "freeParking")
    void updateParking(StageUpdateRequestDto.Parking dto, @MappingTarget Stage stage);

    void updateFacilities(StageUpdateRequestDto.Facilities dto, @MappingTarget Stage stage);

    void updateFnbPolicies(StageUpdateRequestDto.FnbPolicies dto, @MappingTarget Stage stage);

    @Mapping(source = "content", target = "introduction.content")
    @Mapping(source = "tags", target = "introduction.tags")
    @Mapping(source = "links", target = "introduction.links")
    @Mapping(source = "minCapacity", target = "area.minCapacity")
    @Mapping(source = "maxCapacity", target = "area.maxCapacity")
    @Mapping(source = "stageType", target = "area.stageType")
    @Mapping(source = "stageWidth", target = "area.stageWidth")
    @Mapping(source = "stageHeight", target = "area.stageHeight")
    @Mapping(source = "holidays", target = "business.holidays")
    @Mapping(source = "bookingFrom", target = "business.bookingFrom")
    @Mapping(source = "bookingUntil", target = "business.bookingUntil")
    @Mapping(source = "refundPolicies", target = "business.refundPolicies")
    @Mapping(source = "stageManagingAvailable", target = "engineering.stageManagingAvailable")
    @Mapping(source = "stageManagingFee", target = "engineering.stageManagingFee")
    @Mapping(source = "soundEngineeringAvailable", target = "engineering.soundEngineeringAvailable")
    @Mapping(source = "soundEngineeringFee", target = "engineering.soundEngineeringFee")
    @Mapping(source = "lightEngineeringAvailable", target = "engineering.lightEngineeringAvailable")
    @Mapping(source = "lightEngineeringFee", target = "engineering.lightEngineeringFee")
    @Mapping(source = "photographingAvailable", target = "engineering.photographingAvailable")
    @Mapping(source = "photographingFee", target = "engineering.photographingFee")
    @Mapping(source = "applicationForm", target = "documents.applicationForm")
    @Mapping(source = "cueSheetTemplate", target = "documents.cueSheetTemplate")
    @Mapping(source = "cueSheetDue", target = "documents.cueSheetDue")
    @Mapping(source = "parkingCapacity", target = "parking.capacity")
    @Mapping(source = "parkingLocation", target = "parking.location")
    @Mapping(source = "freeParking", target = "parking.free")
    @Mapping(source = "hasElevator", target = "facilities.hasElevator")
    @Mapping(source = "hasRestroom", target = "facilities.hasRestroom")
    @Mapping(source = "hasWifi", target = "facilities.hasWifi")
    @Mapping(source = "hasCameraStanding", target = "facilities.hasCameraStanding")
    @Mapping(source = "hasWaitingRoom", target = "facilities.hasWaitingRoom")
    @Mapping(source = "hasProjector", target = "facilities.hasProjector")
    @Mapping(source = "hasLocker", target = "facilities.hasLocker")
    @Mapping(source = "allowsWater", target = "fnbPolicies.allowsWater")
    @Mapping(source = "allowsDrink", target = "fnbPolicies.allowsDrink")
    @Mapping(source = "allowsFood", target = "fnbPolicies.allowsFood")
    @Mapping(source = "allowsFoodDelivery", target = "fnbPolicies.allowsFoodDelivery")
    @Mapping(source = "allowsAlcohol", target = "fnbPolicies.allowsAlcohol")
    @Mapping(source = "sellDrink", target = "fnbPolicies.sellDrink")
    @Mapping(source = "sellAlcohol", target = "fnbPolicies.sellAlcohol")
    StageResponseDto EntityToResponseDto(Stage stage);

    @Named("deserializeLinks")
    default Set<Link> deserializeLinks(Set<String> links) {
        if(links == null) return new HashSet<>();

        return links.stream()
                .map(s -> Link.builder()
                        .url(s)
                        .build())
                .collect(Collectors.toSet());
    }

    default Set<Holiday> deserializeHolidays(Set<HolidayType> types) {
        if(types == null) return new HashSet<>();

        return types.stream()
                .map(type -> Holiday.builder()
                        .type(type)
                        .build()
                ).collect(Collectors.toSet());
    }

    default Set<String> serializeTags(List<Tag> tags) {
        return tags.stream()
                .map(Tag::getTitle)
                .collect(Collectors.toSet());
    }

    default Set<String> serializeLinks(Set<Link> links) {
        return links.stream()
                .map(Link::getUrl)
                .collect(Collectors.toSet());
    }

    default Set<HolidayType> serializeHolidays(Set<Holiday> holidays) {
        return holidays.stream()
                .map(Holiday::getType)
                .collect(Collectors.toSet());
    }

    @AfterMapping
    default void validateBookingPeriod(StageRegisterRequestDto dto, @MappingTarget Stage stage) {
        stage.validateBookingPeriod();
    }

    @AfterMapping
    default void validateBookingPeriod(StageUpdateRequestDto.Business dto, @MappingTarget Stage stage) {
        stage.validateBookingPeriod();
    }

    @AfterMapping
    default void validateParking(StageRegisterRequestDto dto, @MappingTarget Stage stage) {
        stage.validateParking();
    }

    @AfterMapping
    default void validateParking(StageUpdateRequestDto.Parking dto, @MappingTarget Stage stage) {
        stage.validateParking();
    }

    @AfterMapping
    default void validateEngineering(StageRegisterRequestDto dto, @MappingTarget Stage stage) {
        stage.validateEngineering();
    }

    @AfterMapping
    default void validateEngineering(StageUpdateRequestDto.Engineering dto, @MappingTarget Stage stage) {
        stage.validateEngineering();
    }
}
