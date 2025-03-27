package kr.ontherec.api.socketio;

import com.corundumstudio.socketio.SocketIOClient;
import kr.ontherec.api.domain.chat.dto.MessageResponseDto;

public interface SocketService {
    void sendMessage (SocketIOClient client, Long roomId, String event, MessageResponseDto message);
    void broadcastMessage (Long roomId, String event, MessageResponseDto message);
}
