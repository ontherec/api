package kr.ontherec.api.modules.stage.application;

import kr.ontherec.api.infra.config.MapperConfig;
import kr.ontherec.api.modules.stage.dto.StageRegisterRequestDto;
import kr.ontherec.api.modules.stage.dto.StageResponseDto;
import kr.ontherec.api.modules.stage.dto.StageUpdateRequestDto;
import kr.ontherec.api.modules.stage.entity.Stage;
import kr.ontherec.api.modules.tag.entity.Tag;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        config = MapperConfig.class,
        imports = LocalDateTime.class
)
public interface StageMapper {
    StageMapper INSTANCE = Mappers.getMapper(StageMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "likeCount", expression = "java(0)")
    @Mapping(target = "viewCount", expression = "java(0)")
    @Mapping(source = "introduction", target = ".")
    @Mapping(source = "location", target = ".")
    @Mapping(source = "area", target = ".")
    @Mapping(source = "business", target = ".")
    @Mapping(source = "engineering", target = ".")
    @Mapping(source = "documents", target = ".")
    @Mapping(source = "facilities", target = ".")
    @Mapping(source = "fnbPolicies", target = ".")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    Stage registerRequestDtoToEntity(StageRegisterRequestDto dto);

    void updateArea(StageUpdateRequestDto.Area dto, @MappingTarget Stage stage);

    @Mapping(target = "tags", ignore = true)
    void updateIntroduction(StageUpdateRequestDto.Introduction dto, @MappingTarget Stage stage);

    void updateBusiness(StageUpdateRequestDto.Business dto, @MappingTarget Stage stage);

    void updateEngineering(StageUpdateRequestDto.Engineering dto, @MappingTarget Stage stage);

    void updateDocuments(StageUpdateRequestDto.Documents dto, @MappingTarget Stage stage);

    void updateFacilities(StageUpdateRequestDto.Facilities dto, @MappingTarget Stage stage);

    void updateFnbPolicies(StageUpdateRequestDto.FnbPolicies dto, @MappingTarget Stage stage);

    @Mapping(source = "title", target = "introduction.title")
    @Mapping(source = "content", target = "introduction.content")
    @Mapping(source = "guide", target = "introduction.guide")
    @Mapping(source = "tags", target = "introduction.tags")
    @Mapping(source = "floor", target = "location.floor")
    @Mapping(source = "hasElevator", target = "location.hasElevator")
    @Mapping(source = "minCapacity", target = "area.minCapacity")
    @Mapping(source = "maxCapacity", target = "area.maxCapacity")
    @Mapping(source = "stageType", target = "area.stageType")
    @Mapping(source = "stageWidth", target = "area.stageWidth")
    @Mapping(source = "stageHeight", target = "area.stageHeight")
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

    default Set<String> serializeTags(Set<Tag> tags) {
        if(tags == null) return null;
        return tags.stream()
                .map(Tag::getTitle)
                .collect(Collectors.toSet());
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
