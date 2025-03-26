package kr.ontherec.api.domain.chat.application;

import kr.ontherec.api.domain.chat.domain.Message;
import kr.ontherec.api.domain.chat.dto.MessageResponseDto;
import kr.ontherec.api.global.config.MapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@Mapper(
        config = MapperConfig.class,
        imports = LocalDateTime.class
)
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    MessageResponseDto entityToResponseDto(Message message);

}
