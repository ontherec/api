package kr.ontherec.api.domain.chat.application;

import kr.ontherec.api.domain.chat.domain.Chat;
import kr.ontherec.api.domain.chat.dto.ChatCreateRequestDto;
import kr.ontherec.api.infra.UnitTest;
import kr.ontherec.api.infra.fixture.ChatFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@UnitTest
class ChatCommandServiceTest {

    @Autowired private ChatFactory chatFactory;
    @Autowired private ChatQueryService chatQueryService;
    @Autowired private ChatCommandService chatCommandService;

    private final ChatMapper chatMapper = ChatMapper.INSTANCE;

    @DisplayName("채팅방 생성 성공")
    @Test
    void create() {
        // given
        ChatCreateRequestDto dto = new ChatCreateRequestDto("chat", Set.of("test", "host"));
        Chat newChat = chatMapper.createRequestDtoToEntity(dto);

        // when
        Chat chat = chatCommandService.create(newChat);

        // then
        assertThat(chat.getTitle()).isEqualTo(newChat.getTitle());
        assertThat(chat.getParticipants().size()).isEqualTo(newChat.getParticipants().size());
    }

    @DisplayName("채팅방 읽기 성공")
    @Test
    void read() {
        // given
        Chat chat = chatFactory.create("chat", Set.of("test"));
        LocalDateTime readAt = chat.getParticipant("test").getReadAt();

        // when
        chatCommandService.read(chat, "test");

        // then
        LocalDateTime updatedReadAt = chat.getParticipant("test").getReadAt();
        assertThat(updatedReadAt).isAfter(readAt);
    }

    @DisplayName("채팅방 나가기 성공")
    @Test
    void exit() {
        // given
        Chat chat = chatFactory.create("chat", Set.of("test"));

        // when
        chatCommandService.exit(chat, "test");

        // then
        List<Chat> chats = chatQueryService.getAllByUsername("test");
        assertThat(chats.contains(chat)).isFalse();
    }
}