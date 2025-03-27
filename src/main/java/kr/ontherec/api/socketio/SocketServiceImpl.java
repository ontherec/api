package kr.ontherec.api.socketio;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import kr.ontherec.api.domain.chat.dto.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SocketServiceImpl implements SocketService {
    private final SocketIOServer server;

    public void sendMessage(SocketIOClient client, Long roomId, String event, MessageResponseDto message) {
        for (SocketIOClient c : client.getNamespace().getRoomOperations(roomId.toString()).getClients()) {
            if (!c.getSessionId().equals(client.getSessionId())) {
                c.sendEvent(event, message);
            }
        }
    }

    public void broadcastMessage(Long roomId, String event, MessageResponseDto message) {
        server.getRoomOperations(roomId.toString()).sendEvent(event, message);
    }
}
