package kr.ontherec.api.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import kr.ontherec.api.modules.chat.dto.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SocketServiceImpl implements SocketService {
    private final SocketIOServer server;

    public void broadcastMessage(Long roomId, String event, MessageResponseDto message) {
        server.getRoomOperations(roomId.toString()).sendEvent(event, message);
    }
}
