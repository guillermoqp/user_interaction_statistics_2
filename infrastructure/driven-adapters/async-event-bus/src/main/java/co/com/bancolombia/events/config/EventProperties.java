package co.com.bancolombia.events.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.events")
public class EventProperties {
    private String exchange = "amq.direct";
    private String routingKey = "event.stats.validated";
}