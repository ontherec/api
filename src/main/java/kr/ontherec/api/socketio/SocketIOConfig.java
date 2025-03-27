package kr.ontherec.api.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.protocol.JacksonJsonSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SocketIOConfig {
    @Value("${socketio.server.hostname}")
    private String hostname;

    @Value("${socketio.server.port}")
    private int port;

    @Bean
    public SocketIOServer socketIoServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(hostname);
        config.setPort(port);
        config.setJsonSupport(new JacksonJsonSupport(new JavaTimeModule()) {
            @Override
            protected void init(ObjectMapper objectMapper) {
                super.init(objectMapper);
                objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            }
        });
        return new SocketIOServer(config);
    }
}