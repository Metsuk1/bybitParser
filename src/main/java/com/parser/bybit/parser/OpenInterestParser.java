package com.parser.bybit.parser;

import com.parser.bybit.entity.dto.OpenInterestDto;
import com.parser.bybit.service.MarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class OpenInterestParser implements IMarketDataParser {
    private final MarketDataService marketDataService;


    @Override
    public void parse(JsonNode node) {
        JsonNode data = node.has("result") ? node.get("result").get("list").get(0) : node.get("data");

        var dto = OpenInterestDto.builder()
                .symbol(data.get("symbol").asText())
                .openInterest(data.get("openInterest").decimalValue())
                .source("BYBIT")
                .timestamp(Instant.now())
                .build();

        marketDataService.process(dto);
    }

    @Override
    public boolean canHandle(String topic) {
        return topic.contains("openinterest") || topic.contains("oi");
    }
}
