package kr.ontherec.api.modules.chat.application;

import kr.ontherec.api.infra.config.MapperConfig;
import kr.ontherec.api.modules.chat.dto.ChatCreateRequestDto;
import kr.ontherec.api.modules.chat.dto.ChatResponseDto;
import kr.ontherec.api.modules.chat.entity.Chat;
import kr.ontherec.api.modules.chat.entity.Message;
import kr.ontherec.api.modules.chat.entity.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        config = MapperConfig.class,
        imports = LocalDateTime.class
)
public interface ChatMapper {
    ChatMapper INSTANCE = Mappers.getMapper(ChatMapper.class);

    @Mapping(target = "participants", qualifiedByName = "deserializeParticipants")
    Chat createRequestDtoToEntity(ChatCreateRequestDto dto);

    ChatResponseDto entityToResponseDto(Chat chat, List<Message> messages);

    @Named("deserializeParticipants")
    default Set<Participant> deserializeParticipants(Set<String> participants) {
        if(participants == null) return new HashSet<>();

        return participants.stream()
                .map(username -> Participant.builder()
                        .username(username)
                        .readAt(LocalDateTime.now())
                        .build()
                ).collect(Collectors.toSet());
    }
}
