package com.parser.bybit.entity.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Builder
public record OrderBookDto(
        String symbol,
        List<PriceLevel> bids,
        List<PriceLevel> asks,
        String source,
        long seq,               // Serial number of the update from the exchange
        Instant timestamp
) {
    @Builder
    public record PriceLevel(
            String price,
            String size
    ) {}
}
