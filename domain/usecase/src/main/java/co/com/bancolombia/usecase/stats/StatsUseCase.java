package co.com.bancolombia.usecase.stats;

import co.com.bancolombia.model.stats.Stats;
import co.com.bancolombia.model.stats.gateways.EventPublisher;
import co.com.bancolombia.model.stats.gateways.StatsRepository;
import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

//@Slf4j
@RequiredArgsConstructor
public class StatsUseCase {

    private final StatsRepository statsRepository;
    private final EventPublisher eventPublisher;

    public Mono<Stats> processStats(Stats stats) {
        return Mono.fromSupplier(() -> {
                    String input = String.format("%d,%d,%d,%d,%d,%d,%d",
                            stats.getTotalContactoClientes(),
                            stats.getMotivoReclamo(),
                            stats.getMotivoGarantia(),
                            stats.getMotivoDuda(),
                            stats.getMotivoCompra(),
                            stats.getMotivoFelicitaciones(),
                            stats.getMotivoCambio());

                    String calculatedHash = md5(input);
                    if (!calculatedHash.equalsIgnoreCase(stats.getHash())) {
                        throw new IllegalArgumentException("Hash inválido");
                    }

                    return stats.withTimestamp(Instant.now().toEpochMilli());
                })
                .flatMap(validStats ->
                        statsRepository.saveStats(validStats)
                                .then(eventPublisher.publish(validStats))
                                .thenReturn(validStats)
                );
    }

    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar hash MD5", e);
        }
    }
}