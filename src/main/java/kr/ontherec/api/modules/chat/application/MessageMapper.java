package kr.ontherec.api.modules.chat.application;

import kr.ontherec.api.modules.chat.entity.Message;
import kr.ontherec.api.modules.chat.dto.MessageCreateRequestDto;
import kr.ontherec.api.modules.chat.dto.MessageResponseDto;
import kr.ontherec.api.infra.config.MapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@Mapper(
        config = MapperConfig.class,
        imports = LocalDateTime.class
)
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "modifiedAt", expression = "java(LocalDateTime.now())")
    Message createRequestDtoToEntity(MessageCreateRequestDto createRequestDto);

    MessageResponseDto entityToResponseDto(Message message);

}
