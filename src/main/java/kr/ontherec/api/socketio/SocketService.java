package kr.ontherec.api.socketio;

import kr.ontherec.api.domain.chat.dto.MessageResponseDto;

public interface SocketService {
    void broadcastMessage (Long roomId, String event, MessageResponseDto message);
}
