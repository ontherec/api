package kr.ontherec.api.domain.host.application;

import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.host.dto.HostRegisterRequestDto;
import kr.ontherec.api.domain.host.dto.HostUpdateRequestDto;
import kr.ontherec.api.global.config.MapperConfig;
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

    @AfterMapping
    default void verifyContactTime(String username, HostRegisterRequestDto dto, @MappingTarget Host host) {
        host.verifyContactTime();
    }

    @AfterMapping
    default void verifyContactTime(HostUpdateRequestDto dto, @MappingTarget Host host) {
        host.verifyContactTime();
    }
}
