package kr.ontherec.api.domain.host.application;

import kr.ontherec.api.domain.host.domain.Host;
import kr.ontherec.api.domain.host.dto.HostRegisterRequestDto;
import kr.ontherec.api.domain.host.dto.HostUpdateRequestDto;
import kr.ontherec.api.global.config.MapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(
        config = MapperConfig.class
)
public interface HostMapper {
    HostMapper INSTANCE = Mappers.getMapper(HostMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "dto", target = ".")
    @Mapping(target = "contactFrom", ignore = true)
    @Mapping(target = "contactUntil", ignore = true)
    @Mapping(target = "averageResponseTime", ignore = true)
    Host registerRequestDtoToEntity(String username, HostRegisterRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "averageResponseTime", ignore = true)
    void update(HostUpdateRequestDto dto, @MappingTarget Host host);
}
