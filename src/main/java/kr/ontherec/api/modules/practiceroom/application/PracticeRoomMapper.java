package kr.ontherec.api.modules.practiceroom.application;

import kr.ontherec.api.infra.config.MapperConfig;
import kr.ontherec.api.modules.practiceroom.dto.PracticeRoomRegisterRequestDto;
import kr.ontherec.api.modules.practiceroom.dto.PracticeRoomResponseDto;
import kr.ontherec.api.modules.practiceroom.dto.PracticeRoomUpdateRequestDto;
import kr.ontherec.api.modules.practiceroom.entity.PracticeRoom;
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
public interface PracticeRoomMapper {
    PracticeRoomMapper INSTANCE = Mappers.getMapper(PracticeRoomMapper.class);

    @Mapping(target = "likeCount", expression = "java(0)")
    @Mapping(target = "viewCount", expression = "java(0)")
    @Mapping(source = "introduction", target = ".")
    @Mapping(source = "area", target = ".")
    @Mapping(source = "business", target = ".")
    @Mapping(source = "parking.capacity", target = "parkingCapacity")
    @Mapping(source = "parking.location", target = "parkingLocation")
    @Mapping(source = "parking.free", target = "freeParking")
    @Mapping(source = "facilities", target = ".")
    @Mapping(source = "fnbPolicies", target = ".")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    PracticeRoom registerRequestDtoToEntity(PracticeRoomRegisterRequestDto dto);

    void updateImages(PracticeRoomUpdateRequestDto.Images dto, @MappingTarget PracticeRoom practiceRoom);

    void updateArea(PracticeRoomUpdateRequestDto.Area dto, @MappingTarget PracticeRoom practiceRoom);

    void updateIntroduction(PracticeRoomUpdateRequestDto.Introduction dto, @MappingTarget PracticeRoom practiceRoom);

    void updateBusiness(PracticeRoomUpdateRequestDto.Business dto, @MappingTarget PracticeRoom practiceRoom);

    @Mapping(source = "capacity", target = "parkingCapacity")
    @Mapping(source = "location", target = "parkingLocation")
    @Mapping(source = "free", target = "freeParking")
    void updateParking(PracticeRoomUpdateRequestDto.Parking dto, @MappingTarget PracticeRoom practiceRoom);

    void updateFacilities(PracticeRoomUpdateRequestDto.Facilities dto, @MappingTarget PracticeRoom practiceRoom);

    void updateFnbPolicies(PracticeRoomUpdateRequestDto.FnbPolicies dto, @MappingTarget PracticeRoom practiceRoom);

    @Mapping(source = "practiceRoom.content", target = "introduction.content")
    @Mapping(source = "practiceRoom.tags", target = "introduction.tags")
    @Mapping(source = "practiceRoom.links", target = "introduction.links")
    @Mapping(source = "practiceRoom.standardCapacity", target = "area.standardCapacity")
    @Mapping(source = "practiceRoom.maxCapacity", target = "area.maxCapacity")
    @Mapping(source = "practiceRoom.extraPerPerson", target = "area.extraPerPerson")
    @Mapping(source = "practiceRoom.holidays", target = "business.holidays")
    @Mapping(source = "practiceRoom.timeBlocks", target = "business.timeBlocks")
    @Mapping(source = "practiceRoom.bookingFrom", target = "business.bookingFrom")
    @Mapping(source = "practiceRoom.bookingUntil", target = "business.bookingUntil")
    @Mapping(source = "practiceRoom.refundPolicies", target = "business.refundPolicies")
    @Mapping(source = "practiceRoom.parkingCapacity", target = "parking.capacity")
    @Mapping(source = "practiceRoom.parkingLocation", target = "parking.location")
    @Mapping(source = "practiceRoom.freeParking", target = "parking.free")
    @Mapping(source = "practiceRoom.hasElevator", target = "facilities.hasElevator")
    @Mapping(source = "practiceRoom.hasRestroom", target = "facilities.hasRestroom")
    @Mapping(source = "practiceRoom.hasWifi", target = "facilities.hasWifi")
    @Mapping(source = "practiceRoom.hasCameraStanding", target = "facilities.hasCameraStanding")
    @Mapping(source = "practiceRoom.allowsWater", target = "fnbPolicies.allowsWater")
    @Mapping(source = "practiceRoom.allowsDrink", target = "fnbPolicies.allowsDrink")
    @Mapping(source = "practiceRoom.allowsFood", target = "fnbPolicies.allowsFood")
    @Mapping(source = "practiceRoom.sellDrink", target = "fnbPolicies.sellDrink")
    PracticeRoomResponseDto EntityToResponseDto(PracticeRoom practiceRoom, String username);

    @AfterMapping
    default void mapLiked(PracticeRoom post, String username, @MappingTarget PracticeRoomResponseDto dto) {
        if(username != null) {
            dto.setLiked(post.getLikedUsernames().contains(username));
        }
    }
}
