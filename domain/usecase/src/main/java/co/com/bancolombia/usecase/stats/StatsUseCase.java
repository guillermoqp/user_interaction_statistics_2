package co.com.bancolombia.usecase.stats;

import co.com.bancolombia.model.stats.Stats;
import co.com.bancolombia.model.stats.gateways.EventPublisher;
import co.com.bancolombia.model.stats.gateways.StatsRepository;
import java.math.BigInteger;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

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
                    if (calculatedHash.equals(stats.getHash())) {
                        return stats.withTimestamp(Instant.now().toEpochMilli());
                    } else {
                        throw new IllegalArgumentException("Hash invalido");
                    }
                })
                .flatMap(validStats ->
                        statsRepository.saveStats(validStats)
                                .then(eventPublisher.publish(validStats))
                                .thenReturn(validStats)
                );
    }

    private static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] digest = md.digest();
            BigInteger no = new BigInteger(1, digest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar hash MD5", e);
        }
    }
}