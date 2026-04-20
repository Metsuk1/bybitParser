package com.parser.bybit.websocket;

import com.parser.bybit.config.BybitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class BybitWebSocketClient {
    private final BybitMessageHandler handler;
    private final BybitConfig config;

    @EventListener(ApplicationReadyEvent.class)
    public void connect() {
        log.info("Starting Bybit WebSocket connection...");

        var client = config.bybitClientFactory().newWebsocketClient();

        client.setMessageHandler(message -> {
            try {
                handler.handle(message);
            } catch (Exception e) {
                log.error("Error routing message: {}", e.getMessage());
            }
        });

        List<String> args = List.of(
                "tickers.BTCUSDT",
                "orderbook.50.BTCUSDT"
        );
        client.getPublicChannelStream(args, "v5.public.linear");

        log.info("Subscribed to topics: {}", args);
    }
}
