package kr.ontherec.api.modules.chat.application;

import kr.ontherec.api.modules.chat.entity.Chat;
import kr.ontherec.api.infra.UnitTest;
import kr.ontherec.api.infra.fixture.ChatFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@UnitTest
class ChatQueryServiceTest {

    @Autowired private ChatFactory chatFactory;
    @Autowired private ChatQueryService chatQueryService;

    @DisplayName("내 채팅방 목록 조회 성공")
    @Test
    void getAllByUsername() {
        // given
        Chat chat = chatFactory.create("chat", Set.of("test"));

        // when
        List<Chat> chats = chatQueryService.getAllByUsername("test");

        // then
        assertThat(chats.contains(chat)).isTrue();
    }

    @DisplayName("채팅방 조회 성공")
    @Test
    void get() {
        // given
        Chat chat = chatFactory.create("chat", Set.of("test"));

        // when
        Chat foundChat = chatQueryService.get(chat.getId());

        // then
        assertThat(foundChat.getId()).isEqualTo(chat.getId());
    }

    @DisplayName("채팅방 참가 확인 성공 - 참가한 사용자")
    @Test
    void isParticipant() {
        // given
        Chat chat = chatFactory.create("chat", Set.of("test"));

        // when
        boolean isParticipant = chatQueryService.isParticipant(chat.getId(), "test");

        // then
        assertThat(isParticipant).isTrue();
    }

    @DisplayName("채팅방 참가 확인 성공 - 참가하지 않은 사용자")
    @Test
    void isParticipantWithNotParticipatedUsername() {
        // given
        Chat chat = chatFactory.create("chat", Set.of("host"));

        // when
        boolean isParticipant = chatQueryService.isParticipant(chat.getId(), "test");

        // then
        assertThat(isParticipant).isFalse();
    }
}