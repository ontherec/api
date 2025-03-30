package kr.ontherec.api.modules.chat.presentation;

import kr.ontherec.api.modules.chat.application.*;
import kr.ontherec.api.modules.chat.entity.Chat;
import kr.ontherec.api.modules.chat.entity.Message;
import kr.ontherec.api.modules.chat.dto.ChatCreateRequestDto;
import kr.ontherec.api.modules.chat.dto.ChatResponseDto;
import kr.ontherec.api.modules.chat.dto.MessageResponseDto;
import kr.ontherec.api.modules.chat.exception.ChatException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static kr.ontherec.api.modules.chat.entity.ChatConfig.CHAT_CREATED;
import static kr.ontherec.api.modules.chat.entity.ChatConfig.SYSTEM_USERNAME;
import static kr.ontherec.api.modules.chat.entity.MessageType.NOTICE;
import static kr.ontherec.api.modules.chat.exception.ChatExceptionCode.FORBIDDEN;

@RestController
@RequestMapping("/v1/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatQueryService chatQueryService;
    private final ChatCommandService chatCommandService;
    private final MessageService messageService;
    private final ChatMapper chatMapper = ChatMapper.INSTANCE;
    private final MessageMapper messageMapper = MessageMapper.INSTANCE;

    @GetMapping("/me")
    ResponseEntity<List<ChatResponseDto>> getMyChats(Authentication authentication) {
        List<Chat> chats = chatQueryService.getAllByUsername(authentication.getName());
        List<ChatResponseDto> dtos = chats.stream()
                .map(c -> {
                    List<Message> messages = messageService.getPage(c, null, 100);
                    return chatMapper.entityToResponseDto(c, messages);
                }).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/messages")
    ResponseEntity<List<MessageResponseDto>> getMessages(
            Authentication authentication,
            @PathVariable Long id,
            @RequestParam(required = false) Long lastMessageId,
            @RequestParam(defaultValue = "100") int size
    ) {
        if (!chatQueryService.isParticipant(id, authentication.getName()))
            throw new ChatException(FORBIDDEN);

        Chat chat = chatQueryService.get(id);
        List<Message> messages = messageService.getPage(chat, lastMessageId, size);
        List<MessageResponseDto> dtos = messages.stream()
                .map(messageMapper::entityToResponseDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    ResponseEntity<Long> create(Authentication authentication, @RequestBody ChatCreateRequestDto dto) {
        if (!dto.participants().contains(authentication.getName()))
            throw new ChatException(FORBIDDEN);

        Chat newChat = chatMapper.createRequestDtoToEntity(dto);
        Chat chat = chatCommandService.create(newChat);
        Message createdMessage = Message.builder()
                .chat(chat)
                .username(SYSTEM_USERNAME)
                .type(NOTICE)
                .content(CHAT_CREATED)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        messageService.create(chat, createdMessage);
        return ResponseEntity.created(URI.create("/v1/chats/" + chat.getId())).body(chat.getId());
    }

    @PatchMapping("/{id}/read")
    ResponseEntity<Void> read(Authentication authentication, @PathVariable Long id) {
        if (!chatQueryService.isParticipant(id, authentication.getName()))
            throw new ChatException(FORBIDDEN);

        Chat chat = chatQueryService.get(id);
        chatCommandService.read(chat, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> exit(Authentication authentication, @PathVariable Long id) {
        if (!chatQueryService.isParticipant(id, authentication.getName()))
            throw new ChatException(FORBIDDEN);

        Chat chat = chatQueryService.get(id);
        chatCommandService.exit(chat, authentication.getName());
        return ResponseEntity.ok().build();
    }
}
