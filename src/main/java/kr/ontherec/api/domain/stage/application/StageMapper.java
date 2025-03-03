package kr.ontherec.api.domain.stage.application;

import kr.ontherec.api.domain.stage.domain.Stage;
import kr.ontherec.api.domain.stage.dto.StageRegisterRequestDto;
import kr.ontherec.api.domain.stage.dto.StageResponseDto;
import kr.ontherec.api.domain.stage.dto.StageUpdateRequestDto;
import kr.ontherec.api.domain.tag.domain.Tag;
import kr.ontherec.api.global.config.MapperConfig;
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
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    Stage registerRequestDtoToEntity(StageRegisterRequestDto dto);

    void updateLocation(StageUpdateRequestDto.Title dto, @MappingTarget Stage stage);

    void updateArea(StageUpdateRequestDto.Area dto, @MappingTarget Stage stage);

    @Mapping(target = "tags", ignore = true)
    void updateIntroduction(StageUpdateRequestDto.Introduction dto, @MappingTarget Stage stage);

    void updateBusiness(StageUpdateRequestDto.Business dto, @MappingTarget Stage stage);

    void updateEngineering(StageUpdateRequestDto.Engineering dto, @MappingTarget Stage stage);

    void updateDocuments(StageUpdateRequestDto.Documents dto, @MappingTarget Stage stage);

    void updateFacilities(StageUpdateRequestDto.Facilities dto, @MappingTarget Stage stage);

    void updateFnbPolicies(StageUpdateRequestDto.FnbPolicies dto, @MappingTarget Stage stage);

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

    @AfterMapping
    default void validateParking(StageRegisterRequestDto dto, @MappingTarget Stage stage) {
        stage.validateParking();
    }

    @AfterMapping
    default void validateParking(StageUpdateRequestDto.Facilities dto, @MappingTarget Stage stage) {
        stage.validateParking();
    }
}
