package com.parser.bybit.parser;

import com.parser.bybit.entity.dto.OrderBookDto;
import com.parser.bybit.service.MarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderBookParser implements IMarketDataParser{
    private final MarketDataService marketDataService;

    @Override
    public void parse(JsonNode node) {
        JsonNode data = node.get("data");

        var dto = OrderBookDto.builder()
                .symbol(data.get("s").asText())
                .bids(parseLevels(data.get("b")))
                .asks(parseLevels(data.get("a")))
                .source("BYBIT")
                .seq(node.get("cts").asLong())
                .timestamp(Instant.now())
                .build();

        marketDataService.process(dto);
    }

    private List<OrderBookDto.PriceLevel> parseLevels(JsonNode levelsNode) {
        List<OrderBookDto.PriceLevel> levels = new ArrayList<>();

        if (levelsNode != null && levelsNode.isArray()) {
            for (JsonNode level : levelsNode) {
                levels.add(new OrderBookDto.PriceLevel(
                        level.get(0).decimalValue(), // Bid price
                        level.get(1).decimalValue()  // Bid size
                ));
            }
        }

        return levels;
    }

    @Override
    public boolean canHandle(String topic) {
        return topic.contains("orderbook");
    }
}
