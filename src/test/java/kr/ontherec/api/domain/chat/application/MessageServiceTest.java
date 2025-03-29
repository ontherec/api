package kr.ontherec.api.domain.chat.application;

import kr.ontherec.api.domain.chat.domain.Chat;
import kr.ontherec.api.domain.chat.domain.Message;
import kr.ontherec.api.infra.UnitTest;
import kr.ontherec.api.infra.fixture.ChatFactory;
import kr.ontherec.api.infra.fixture.MessageFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static kr.ontherec.api.domain.chat.domain.MessageType.TEXT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@UnitTest
class MessageServiceTest {

    @Autowired private ChatFactory chatFactory;
    @Autowired private MessageFactory messageFactory;

    @Autowired private MessageService messageService;

    @DisplayName("메시지 목록 조회 성공 - 첫 페이지")
    @Test
    void getPage() {
        // given
        Chat chat = chatFactory.create("chat", Set.of("test"));
        for(int i = 0 ; i < 20 ; i++) {
            messageFactory.create(chat, TEXT, "test", "message" + i);
        }

        // when
        List<Message> messages = messageService.getPage(chat, null, 10);

        // then
        assertThat(messages.size()).isEqualTo(10);
    }

    @DisplayName("메시지 목록 조회 성공 - 커서 페이지네이션")
    @Test
    void getPageWithLastMessageId() {
        // given
        Chat chat = chatFactory.create("chat", Set.of("test"));
        for(int i = 0 ; i < 20 ; i++) {
            messageFactory.create(chat, TEXT, "test", "message" + i);
        }

        // when
        List<Message> messages = messageService.getPage(chat, null, 10);

        // then
        assertThat(messages.size()).isEqualTo(10);
    }

    @DisplayName("메시지 생성 성공")
    @Test
    void create() {
        // given
        Chat chat = chatFactory.create("chat", Set.of("test"));
        Message newMessage = Message.builder()
                .type(TEXT)
                .username("test")
                .content("message")
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        // when
        Message message = messageService.create(chat, newMessage);

        // then
        assertThat(message.getType()).isEqualTo(newMessage.getType());
        assertThat(message.getUsername()).isEqualTo(newMessage.getUsername());
        assertThat(message.getContent()).isEqualTo(newMessage.getContent());
    }
}