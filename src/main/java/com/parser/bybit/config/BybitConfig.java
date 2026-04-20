package com.parser.bybit.config;

import com.bybit.api.client.config.BybitApiConfig;
import com.bybit.api.client.service.BybitApiClientFactory;
import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BybitConfig {

    @Value("${bybit.stream.url}")
    private String streamUrl;

    @Bean
    public BybitApiClientFactory bybitClientFactory() {
        return BybitApiClientFactory
                .newInstance(BybitApiConfig.STREAM_MAINNET_DOMAIN, true);
    }
}
