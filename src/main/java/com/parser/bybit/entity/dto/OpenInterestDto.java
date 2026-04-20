package com.parser.bybit.entity.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
public record OpenInterestDto(
        String symbol,
        BigDecimal openInterest,
        String source,
        Instant timestamp
) {}
