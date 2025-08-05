package mg.apprologic.apprologic.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");  // Préfixe pour les messages sortants
        config.setApplicationDestinationPrefixes("/app");  // Préfixe pour les messages entrants
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")  // Endpoint pour la connexion
                .setAllowedOriginPatterns("*")  // Autorise toutes les origines (à ajuster en production)
                .withSockJS();  // Active le fallback SockJS
    }
}