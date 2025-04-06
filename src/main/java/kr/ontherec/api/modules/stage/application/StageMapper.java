package kr.ontherec.api.modules.stage.application;

import kr.ontherec.api.infra.config.MapperConfig;
import kr.ontherec.api.modules.stage.dto.StageRegisterRequestDto;
import kr.ontherec.api.modules.stage.dto.StageResponseDto;
import kr.ontherec.api.modules.stage.dto.StageUpdateRequestDto;
import kr.ontherec.api.modules.stage.entity.Stage;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@Mapper(
        config = MapperConfig.class,
        imports = LocalDateTime.class
)
public interface StageMapper {
    StageMapper INSTANCE = Mappers.getMapper(StageMapper.class);

    @Mapping(target = "likeCount", expression = "java(0)")
    @Mapping(target = "viewCount", expression = "java(0)")
    @Mapping(source = "introduction", target = ".")
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

    @Mapping(source = "stage.content", target = "introduction.content")
    @Mapping(source = "stage.tags", target = "introduction.tags")
    @Mapping(source = "stage.links", target = "introduction.links")
    @Mapping(source = "stage.minCapacity", target = "area.minCapacity")
    @Mapping(source = "stage.maxCapacity", target = "area.maxCapacity")
    @Mapping(source = "stage.stageType", target = "area.stageType")
    @Mapping(source = "stage.stageWidth", target = "area.stageWidth")
    @Mapping(source = "stage.stageHeight", target = "area.stageHeight")
    @Mapping(source = "stage.holidays", target = "business.holidays")
    @Mapping(source = "stage.bookingFrom", target = "business.bookingFrom")
    @Mapping(source = "stage.bookingUntil", target = "business.bookingUntil")
    @Mapping(source = "stage.refundPolicies", target = "business.refundPolicies")
    @Mapping(source = "stage.stageManagingAvailable", target = "engineering.stageManagingAvailable")
    @Mapping(source = "stage.stageManagingFee", target = "engineering.stageManagingFee")
    @Mapping(source = "stage.soundEngineeringAvailable", target = "engineering.soundEngineeringAvailable")
    @Mapping(source = "stage.soundEngineeringFee", target = "engineering.soundEngineeringFee")
    @Mapping(source = "stage.lightEngineeringAvailable", target = "engineering.lightEngineeringAvailable")
    @Mapping(source = "stage.lightEngineeringFee", target = "engineering.lightEngineeringFee")
    @Mapping(source = "stage.photographingAvailable", target = "engineering.photographingAvailable")
    @Mapping(source = "stage.photographingFee", target = "engineering.photographingFee")
    @Mapping(source = "stage.applicationForm", target = "documents.applicationForm")
    @Mapping(source = "stage.cueSheetTemplate", target = "documents.cueSheetTemplate")
    @Mapping(source = "stage.cueSheetDue", target = "documents.cueSheetDue")
    @Mapping(source = "stage.parkingCapacity", target = "parking.capacity")
    @Mapping(source = "stage.parkingLocation", target = "parking.location")
    @Mapping(source = "stage.freeParking", target = "parking.free")
    @Mapping(source = "stage.hasElevator", target = "facilities.hasElevator")
    @Mapping(source = "stage.hasRestroom", target = "facilities.hasRestroom")
    @Mapping(source = "stage.hasWifi", target = "facilities.hasWifi")
    @Mapping(source = "stage.hasCameraStanding", target = "facilities.hasCameraStanding")
    @Mapping(source = "stage.hasWaitingRoom", target = "facilities.hasWaitingRoom")
    @Mapping(source = "stage.hasProjector", target = "facilities.hasProjector")
    @Mapping(source = "stage.hasLocker", target = "facilities.hasLocker")
    @Mapping(source = "stage.allowsWater", target = "fnbPolicies.allowsWater")
    @Mapping(source = "stage.allowsDrink", target = "fnbPolicies.allowsDrink")
    @Mapping(source = "stage.allowsFood", target = "fnbPolicies.allowsFood")
    @Mapping(source = "stage.allowsFoodDelivery", target = "fnbPolicies.allowsFoodDelivery")
    @Mapping(source = "stage.allowsAlcohol", target = "fnbPolicies.allowsAlcohol")
    @Mapping(source = "stage.sellDrink", target = "fnbPolicies.sellDrink")
    @Mapping(source = "stage.sellAlcohol", target = "fnbPolicies.sellAlcohol")
    StageResponseDto EntityToResponseDto(Stage stage, String username);

    @AfterMapping
    default void mapLiked(Stage post, String username, @MappingTarget StageResponseDto dto) {
        if(username != null) {
            dto.setLiked(post.getLikedUsernames().contains(username));
        }
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
