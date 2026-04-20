package com.parser.bybit.service;

import com.parser.bybit.entity.dto.MarketDataDto;
import com.parser.bybit.entity.dto.OpenInterestDto;
import com.parser.bybit.entity.dto.OrderBookDto;
import com.parser.bybit.kafka.MarketDataProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketDataService {
    private final MarketDataProducer producer;

    public void process(MarketDataDto dto) {
        if(dto.price() == null || dto.symbol() == null) {
            log.warn("Skipping invalid data: {}", dto);
            return;
        }
        log.info("Processing data: {}", dto);

        producer.send(dto);
    }

    public void process(OpenInterestDto dto) {
        if(dto.symbol() == null) {
            log.warn("Skipped symbol: {}", dto);
        }
        log.info("New Open Interest for {}: {}", dto.symbol(), dto.openInterest());

        producer.sendOI(dto);
    }

    public void process(OrderBookDto dto){
        if(dto.symbol() == null) {
            log.warn("Skipped symbol: {}", dto);
        }
        log.debug("Routing OrderBook for {}", dto.symbol());

        producer.sendOrderBook(dto);
    }
}
