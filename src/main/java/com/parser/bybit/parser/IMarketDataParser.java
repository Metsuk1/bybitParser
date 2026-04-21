package com.parser.bybit.parser;

import com.fasterxml.jackson.databind.JsonNode;

public interface IMarketDataParser {
    void parse(JsonNode node);
    boolean canHandle(String topic);
}
