package kr.ontherec.api.domain.host.application;

import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.host.dto.HostRegisterRequestDto;
import kr.ontherec.api.domain.host.dto.HostUpdateRequestDto;
import kr.ontherec.api.global.config.MapperConfig;
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

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "dto", target = ".")
    @Mapping(target = "contactFrom", ignore = true)
    @Mapping(target = "contactUntil", ignore = true)
    @Mapping(target = "averageResponseTime", ignore = true)
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    Host registerRequestDtoToEntity(String username, HostRegisterRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "averageResponseTime", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    void update(HostUpdateRequestDto dto, @MappingTarget Host host);
}
