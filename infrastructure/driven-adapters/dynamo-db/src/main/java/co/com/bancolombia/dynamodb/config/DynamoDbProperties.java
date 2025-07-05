package co.com.bancolombia.dynamodb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.dynamodb")
public class DynamoDbProperties {
    private String endpoint;
    private String region;
    private String tableName;
    private String accessKey;
    private String secretKey;
}