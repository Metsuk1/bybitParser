# Bybit Market Data Publisher

Spring Boot service that consumes market data from Bybit, normalizes it into DTOs, and publishes the result to Kafka.

At the moment the application is focused on a single instrument, `BTCUSDT`, and collects:

- real-time ticker updates over WebSocket
- real-time order book updates over WebSocket
- open interest snapshots over REST every 5 minutes

## Tech Stack

- Java 21
- Spring Boot 3
- Spring WebFlux
- Spring Kafka
- Maven
- Docker Compose
- Kafka UI

## How It Works

The application starts a Bybit public WebSocket connection after startup and subscribes to:

- `tickers.BTCUSDT`
- `orderbook.50.BTCUSDT`

In parallel, a scheduler calls the Bybit REST API every 5 minutes to fetch open interest for `BTCUSDT`.

Parsed messages are routed through `MarketDataService` and published to Kafka as JSON.

## Kafka Topics

The service publishes to three topics:

- `market.tickers`
- `market.order-book`
- `market.open-interest`

Kafka message keys use the instrument symbol, for example `BTCUSDT`.

## AsyncAPI Contract

The project includes an AsyncAPI specification in [asyncapi.yaml](C:/ex.proj/bybit/asyncapi.yaml).

Use this file as the source of truth for:

- available Kafka topics
- message payload schemas
- Kafka message key format
- example events for consumers

## Project Structure

```text
src/main/java/com/parser/bybit
|-- config        # Kafka, Jackson, Bybit, and WebClient configuration
|-- controller    # Placeholder for HTTP endpoints
|-- entity/dto    # DTOs published to Kafka
|-- kafka         # Kafka producer
|-- parser        # Message parsers for ticker, order book, and open interest
|-- scheduler     # Periodic REST polling for open interest
|-- service       # Validation and routing logic
`-- websocket     # WebSocket connection and message dispatching
```

## Running With Docker Compose

This is the recommended way to run the project because Kafka is configured in code with the hostname `kafka`.

### Start

```bash
docker compose up --build
```

### Exposed Services

- Application: `http://localhost:8081`
- Kafka broker: `localhost:9092`
- Kafka UI: `http://localhost:8090`

## Running With Maven

### Requirements

- Java 21
- Maven 3.9+
- A reachable Kafka broker available as `kafka:9092`

### Start

On Linux/macOS:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

## Message Models

### Ticker

Published as `MarketDataDto` with:

- `symbol`
- `price`
- `volume`
- `source`
- `assetType`
- `timestamp`

### Order Book

Published as `OrderBookDto` with:

- `symbol`
- `bids`
- `asks`
- `source`
- `seq`
- `timestamp`

### Open Interest

Published as `OpenInterestDto` with:

- `symbol`
- `openInterest`
- `source`
- `timestamp`

## Current Limitations

- The subscribed symbol is hardcoded to `BTCUSDT`.
- The WebSocket subscriptions are hardcoded in the client.
- Kafka bootstrap server is hardcoded to `kafka:9092` in `KafkaProducerConfig`.
- `MarketDataController` exists but does not expose any REST endpoints yet.
- Reconnect logic is not implemented yet.

## Build

```bash
./mvnw clean package
```

On Windows:

```powershell
.\mvnw.cmd clean package
```

## Test

```bash
./mvnw test
```

On Windows:

```powershell
.\mvnw.cmd test
```

## Next Improvements

- Move symbols and topic names to configuration
- Use `spring.kafka.bootstrap-servers` consistently instead of hardcoded Kafka settings
- Add reconnect handling for the WebSocket client
- Add REST endpoints or health/status endpoints
- Support multiple instruments and categories
