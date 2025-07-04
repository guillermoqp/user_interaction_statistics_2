package co.com.bancolombia.model.stats.gateways;

import co.com.bancolombia.model.stats.Stats;
import reactor.core.publisher.Mono;

public interface EventPublisher {
    Mono<Void> publish(Stats stats);
}