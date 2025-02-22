package kr.ontherec.api.domain.place.application;

import kr.ontherec.api.domain.keyword.domain.Keyword;
import kr.ontherec.api.domain.place.domain.Holiday;
import kr.ontherec.api.domain.place.domain.HolidayType;
import kr.ontherec.api.domain.place.domain.Link;
import kr.ontherec.api.domain.place.domain.Place;
import kr.ontherec.api.domain.place.dto.PlaceRegisterRequestDto;
import kr.ontherec.api.domain.place.dto.PlaceResponseDto;
import kr.ontherec.api.domain.place.dto.PlaceUpdateRequestDto;
import kr.ontherec.api.global.config.MapperConfig;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        config = MapperConfig.class,
        imports = LocalDateTime.class
)
public interface PlaceMapper {
    PlaceMapper INSTANCE = Mappers.getMapper(PlaceMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "keywords", ignore = true)
    @Mapping(target = "links", qualifiedByName = "deserializeLinks")
    @Mapping(target = "holidays", qualifiedByName = "deserializeHolidays")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    Place registerRequestDtoToEntity(PlaceRegisterRequestDto dto);

    @Mapping(target = "keywords", ignore = true)
    @Mapping(target = "links", qualifiedByName = "deserializeLinks")
    @Mapping(target = "holidays", qualifiedByName = "deserializeHolidays")
    Place updateRequestDtoToEntity(PlaceUpdateRequestDto dto);

    void update(Place newPlace, @MappingTarget Place place);

    PlaceResponseDto EntityToResponseDto(Place place);

    @Named("deserializeLinks")
    default Set<Link> deserializeLinks(Set<String> links) {
        if(links == null) return null;

        return links.stream()
                .map(s -> Link.builder()
                        .url(s)
                        .build())
                .collect(Collectors.toSet());
    }

    @Named("deserializeHolidays")
    default Set<Holiday> deserializeHolidays(Set<HolidayType> types) {
        if(types == null) return null;

        return types.stream()
                .map(type -> Holiday.builder()
                        .type(type)
                        .build()
                ).collect(Collectors.toSet());
    }

    default Set<String> serializeKeywords(Set<Keyword> keywords) {
        if(keywords == null) return null;
        return keywords.stream()
                .map(Keyword::getTitle)
                .collect(Collectors.toSet());
    }

    default Set<String> serializeLinks(Set<Link> keywords) {
        if(keywords == null) return null;
        return keywords.stream()
                .map(Link::getUrl)
                .collect(Collectors.toSet());
    }

    default Set<HolidayType> serializeHolidays(Set<Holiday> keywords) {
        if(keywords == null) return null;
        return keywords.stream()
                .map(Holiday::getType)
                .collect(Collectors.toSet());
    }

    @AfterMapping
    default void validateBookingPeriod(PlaceRegisterRequestDto dto, @MappingTarget Place place) {
        place.validateBookingPeriod();
    }

    @AfterMapping
    default void validateBookingPeriod(PlaceUpdateRequestDto dto, @MappingTarget Place place) {
        place.validateBookingPeriod();
    }
}
