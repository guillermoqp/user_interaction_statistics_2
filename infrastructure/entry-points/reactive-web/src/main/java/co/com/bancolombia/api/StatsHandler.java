package co.com.bancolombia.api;

import co.com.bancolombia.model.stats.Stats;
import co.com.bancolombia.usecase.stats.StatsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatsHandler {
    private final StatsUseCase statsUseCase;

    public Mono<ServerResponse> handleStats(ServerRequest request) {
        return request.bodyToMono(Stats.class)
                .flatMap(statsUseCase::processStats)
                .flatMap(validStats -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(validStats))
                .onErrorResume(e -> {
                    log.error("Error procesando estadística: {}", e.getMessage());
                    return ServerResponse.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue("{\"error\":\"Hash inválido\"}");
                });
    }
}