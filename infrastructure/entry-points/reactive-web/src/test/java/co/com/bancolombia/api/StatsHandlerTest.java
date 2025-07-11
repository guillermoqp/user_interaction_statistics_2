package co.com.bancolombia.api;

import co.com.bancolombia.model.stats.Stats;
import co.com.bancolombia.usecase.stats.StatsUseCase;
import co.com.bancolombia.MainApplication;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import java.time.Instant;
import co.com.bancolombia.api.StatsHandler;
import co.com.bancolombia.api.StatsRouter;
import static org.mockito.ArgumentMatchers.any;

@WebFluxTest(controllers = StatsHandler.class)
@Import(StatsRouter.class)
@ContextConfiguration(classes = co.com.bancolombia.MainApplication.class)
class StatsHandlerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private StatsUseCase statsUseCase;

    @Test
    void shouldReturn200WhenValid() {
        Stats input = Stats.builder()
                .totalContactoClientes(1)
                .motivoReclamo(2)
                .motivoGarantia(3)
                .motivoDuda(4)
                .motivoCompra(5)
                .motivoFelicitaciones(6)
                .motivoCambio(7)
                .hash("hashValido")
                .build();
        Mockito.when(statsUseCase.processStats(any())).thenReturn(Mono.just(input));
        webTestClient.post()
                .uri("/stats")
                .bodyValue(input)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldReturn400WhenStatsInvalid() {
        Stats input = Stats.builder()
                .totalContactoClientes(1)
                .motivoReclamo(2)
                .motivoGarantia(3)
                .motivoDuda(4)
                .motivoCompra(5)
                .motivoFelicitaciones(6)
                .motivoCambio(7)
                .hash("incorrecto")
                .build();

        Mockito.when(statsUseCase.processStats(any()))
                .thenReturn(Mono.error(new IllegalArgumentException("Hash invalido")));

        webTestClient.post()
                .uri("/stats")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(input)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.error").isEqualTo("Hash invalido");
    }
}