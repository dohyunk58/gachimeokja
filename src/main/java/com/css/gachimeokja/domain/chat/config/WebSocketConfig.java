package com.css.gachimeokja.domain.chat.config;

import com.css.gachimeokja.security.jwt.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private static final String ENDPOINT = "/ws";
    private static final String BROKER = "/sub";
    private static final String PUBLISH = "/pub";

    private final StompHandler stompHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(BROKER); // 메세지를 브로커에게 전달해 수신자들에게 뿌림
        registry.setApplicationDestinationPrefixes(PUBLISH); // 가공이 필요한 데이터를 핸들러로 처리
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 웹소켓 연결(핸드세이크) 주소
        registry.addEndpoint(ENDPOINT)
                .setAllowedOriginPatterns("*");
    }

    // JWT 인터셉터
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}
