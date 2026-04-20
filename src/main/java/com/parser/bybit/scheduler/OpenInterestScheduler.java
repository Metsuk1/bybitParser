package com.parser.bybit.scheduler;

import com.parser.bybit.parser.OpenInterestParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenInterestScheduler {
    private final OpenInterestParser oiParser;
    private final ObjectMapper mapper;
    private final WebClient webClient;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://api.bybit.com")
                .build();
    }

    @Scheduled(fixedRate = 300000)
    public void fetchBybitOpenInterest() {
        log.info("Fetching Open Interest from Bybit...");

        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v5/market/open-interest")
                        .queryParam("category", "linear")
                        .queryParam("symbol", "BTCUSDT")
                        .queryParam("intervalTime", "5min")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> log.error("Error fetching OI: {}", e.getMessage()))
                .subscribe(response -> {
                    try {
                        JsonNode json = mapper.readTree(response);
                        oiParser.parse(json);
                        log.debug("OI data successfully pushed to parser");
                    } catch (Exception e) {
                        log.error("Failed to parse OI REST response", e);
                    }
                });
    }
}
