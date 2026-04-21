package com.parser.bybit.entity.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
public record MarketDataDto(
        String symbol,      // BTCUSDT, XAUUSD
        String price,
        String volume,
        String source,      // BYBIT
        String assetType,   // CRYPTO, COMMODITY
        Instant timestamp
) {}
