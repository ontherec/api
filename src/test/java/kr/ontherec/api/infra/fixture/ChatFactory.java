package kr.ontherec.api.infra.fixture;

import kr.ontherec.api.modules.chat.application.ChatCommandService;
import kr.ontherec.api.modules.chat.entity.Chat;
import kr.ontherec.api.modules.chat.entity.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ChatFactory {
    @Autowired private ChatCommandService chatCommandService;

    public Chat create(String title, Set<String> usernames) {
        Set<Participant> participants = usernames.stream()
                .map(username -> Participant.builder()
                        .username(username)
                        .readAt(LocalDateTime.now())
                        .build())
                .collect(Collectors.toSet());

        Chat newChat = Chat.builder()
                .title(title)
                .participants(participants)
                .build();

        return chatCommandService.create(newChat);
    }
}
