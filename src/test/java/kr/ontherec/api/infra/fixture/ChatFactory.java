package kr.ontherec.api.infra.fixture;

import kr.ontherec.api.domain.chat.application.ChatCommandService;
import kr.ontherec.api.domain.chat.application.MessageService;
import kr.ontherec.api.domain.chat.domain.Chat;
import kr.ontherec.api.domain.chat.domain.Message;
import kr.ontherec.api.domain.chat.domain.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static kr.ontherec.api.domain.chat.domain.MessageType.TEXT;

@Component
public class ChatFactory {
    @Autowired private ChatCommandService chatCommandService;
    @Autowired private MessageService messageService;

    public Chat create(String title, String... usernames) {
        Set<Participant> participants = Arrays.stream(usernames)
                .map(username -> Participant.builder()
                        .username(username)
                        .readAt(LocalDateTime.now())
                        .build())
                .collect(Collectors.toSet());

        Chat newChat = Chat.builder()
                .title(title)
                .participants(participants)
                .build();

        Chat chat = chatCommandService.create(newChat);

        Arrays.stream(usernames)
                .map(username -> Message.builder()
                        .type(TEXT)
                        .username(username)
                        .content("Hello, " + username)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build())
                .forEach(message -> messageService.add(chat, message));

        return chat;
    }
}
