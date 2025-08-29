// src/main/java/config/SocketIOConfig.java
package App.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfig {

    @Value("${socket.host:localhost}")
    private String host;

    @Value("${socket.port:9091}")
    private Integer port;

    @Bean
    public SocketIOServer socketIOServer() {
        // Use fully qualified name to avoid import conflict
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(host);
        config.setPort(port);

        // IMPORTANT: Enable all transports and proper CORS
        config.setOrigin("*"); // Allow all origins for development
        config.setAllowCustomRequests(true);

        // Enable both websocket and polling transports
        config.setTransports(com.corundumstudio.socketio.Transport.WEBSOCKET,
                com.corundumstudio.socketio.Transport.POLLING);

        // Additional configuration for better compatibility
        config.setPingTimeout(60000);
        config.setPingInterval(25000);
        config.setUpgradeTimeout(10000);
        config.setMaxFramePayloadLength(1024 * 1024);
        config.setMaxHttpContentLength(1024 * 1024);

        // Add some debugging
        System.out.println("Socket.IO Configuration:");
        System.out.println("Host: " + host);
        System.out.println("Port: " + port);
        System.out.println("Origin: *");

        return new SocketIOServer(config);
    }
}