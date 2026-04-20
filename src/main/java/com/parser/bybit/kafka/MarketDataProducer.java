package com.parser.bybit.kafka;

import com.parser.bybit.entity.dto.MarketDataDto;
import com.parser.bybit.entity.dto.OpenInterestDto;
import com.parser.bybit.entity.dto.OrderBookDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MarketDataProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(MarketDataDto dto) {
        kafkaTemplate.send("market.tickers", dto.symbol(), dto)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send Ticker for {}: {}", dto.symbol(), ex.getMessage());
                    } else {
                        log.debug("Ticker sent to Kafka for {}", dto.symbol());
                    }
                });
    }

    public void sendOI(OpenInterestDto dto) {
        kafkaTemplate.send("market.open-interest", dto.symbol(), dto)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send OI for {}: {}", dto.symbol(), ex.getMessage());
                    }
                });
    }

    public void sendOrderBook(OrderBookDto dto) {
        kafkaTemplate.send("market.order-book", dto.symbol(), dto)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send OrderBook for {}: {}", dto.symbol(), ex.getMessage());
                    }
                });
    }
}
