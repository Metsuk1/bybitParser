package com.parser.bybit.parser;

import com.parser.bybit.entity.dto.MarketDataDto;
import com.parser.bybit.service.MarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class TickerParser implements IMarketDataParser{
    private final MarketDataService marketDataService;
    
    
    @Override
    public void parse(JsonNode node) {
        JsonNode data = node.get("data");

        if(!data.has("lastPrice")) return;

        var dto = MarketDataDto.builder()
                .symbol(data.get("symbol").asText())
                .price(data.get("lastPrice").decimalValue())
                .source("BYBIT")
                .assetType("CRYPTO")
                .timestamp(Instant.now())
                .build();
        marketDataService.process(dto);
    }

    @Override
    public boolean canHandle(String topic) {
        return topic.contains("ticker");
    }
}
