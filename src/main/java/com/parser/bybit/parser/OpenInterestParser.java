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
        JsonNode result = node.path("result");
        JsonNode item = result.path("list").path(0);

        var dto = OpenInterestDto.builder()
                .symbol(result.path("symbol").asText())
                .openInterest(item.path("openInterest").decimalValue())
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
