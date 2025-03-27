package kr.ontherec.api.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class SocketIOServerRunner {
    private final SocketIOServer server;

    public SocketIOServerRunner(SocketIOServer server) {
        this.server = server;
    }

    @PostConstruct
    public void start() {
        server.start();
    }

    @PreDestroy
    public void stop() {
        server.stop();
    }
}