package com.parser.bybit.websocket;

import com.parser.bybit.parser.IMarketDataParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BybitMessageHandler {
    private final List<IMarketDataParser> parsers;
    private final ObjectMapper mapper;

    public void handle(String rawMessage) {
        try {
            JsonNode json = mapper.readTree(rawMessage);
            if (!json.has("topic")) return;

            String topic = json.get("topic").asText();

            parsers.stream()
                    .filter(parser -> parser.canHandle(topic))
                    .findFirst()
                    .ifPresent(parser -> parser.parse(json));

        } catch (Exception e) {
            log.error("Error with message: {}", rawMessage, e);
        }
    }
}
