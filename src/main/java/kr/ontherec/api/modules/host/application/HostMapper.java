package kr.ontherec.api.modules.host.application;

import kr.ontherec.api.modules.host.entity.Host;
import kr.ontherec.api.modules.host.dto.HostRegisterRequestDto;
import kr.ontherec.api.modules.host.dto.HostResponseDto;
import kr.ontherec.api.modules.host.dto.HostUpdateRequestDto;
import kr.ontherec.api.infra.config.MapperConfig;
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
public interface HostMapper {
    HostMapper INSTANCE = Mappers.getMapper(HostMapper.class);

    @Mapping(source = "dto", target = ".")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    Host registerRequestDtoToEntity(String username, HostRegisterRequestDto dto);

    void update(HostUpdateRequestDto dto, @MappingTarget Host host);

    HostResponseDto entityToResponseDto(Host host);

    @AfterMapping
    default void validateContactTime(String username, HostRegisterRequestDto dto, @MappingTarget Host host) {
        host.validateContactTime();
    }

    @AfterMapping
    default void validateContactTime(HostUpdateRequestDto dto, @MappingTarget Host host) {
        host.validateContactTime();
    }
}
