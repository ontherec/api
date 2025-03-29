package kr.ontherec.api.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import kr.ontherec.api.domain.chat.application.ChatQueryService;
import kr.ontherec.api.domain.chat.application.MessageMapper;
import kr.ontherec.api.domain.chat.application.MessageService;
import kr.ontherec.api.domain.chat.domain.Chat;
import kr.ontherec.api.domain.chat.domain.Message;
import kr.ontherec.api.domain.chat.dto.MessageCreateRequestDto;
import kr.ontherec.api.domain.chat.dto.MessageResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SocketIOController {
   public static final String EVENT_MESSAGE = "message";

   private final SocketService socketService;
   private final ChatQueryService chatQueryService;
   private final MessageService messageService;
   private final MessageMapper messageMapper = MessageMapper.INSTANCE;

   public SocketIOController(SocketIOServer server, SocketService socketService, ChatQueryService chatQueryService, MessageService messageService) {
      this.socketService = socketService;
      this.chatQueryService = chatQueryService;
      this.messageService = messageService;

      server.addConnectListener(onConnected());
      server.addDisconnectListener(onDisconnected());
      server.addEventListener(EVENT_MESSAGE, MessageCreateRequestDto.class, onMessage());
   }

   private ConnectListener onConnected() {
      return (client) -> {
         String roomId = client.getHandshakeData().getSingleUrlParam("roomId");
         client.joinRoom(roomId);
         log.info("Socket ID[{}] - Connected to chat module through", client.getSessionId());
      };
   }

   private DisconnectListener onDisconnected() {
      return (client) -> {
         String roomId = client.getHandshakeData().getSingleUrlParam("roomId");
         client.leaveRoom(roomId);
         log.info("Socket ID[{}] - Disconnected to chat module through", client.getSessionId());
      };
   }

   private DataListener<MessageCreateRequestDto> onMessage() {
      return (client, data, ack) -> {
         String roomId = client.getHandshakeData().getSingleUrlParam("roomId");

         Chat chat = chatQueryService.get(Long.valueOf(roomId));
         Message newMessage = messageMapper.createRequestDtoToEntity(data);
         Message message = messageService.add(chat, newMessage);
         MessageResponseDto dto = messageMapper.entityToResponseDto(message);

         socketService.broadcastMessage(Long.parseLong(roomId), EVENT_MESSAGE, dto);
      };
   }
}