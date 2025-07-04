package co.com.bancolombia.dynamodb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import java.net.URI;

@Configuration
public class DynamoDbConfig {

    @Bean
    public SdkAsyncHttpClient sdkAsyncHttpClient() {
        return NettyNioAsyncHttpClient.builder().build();
    }

    @Bean
    public DynamoDbAsyncClient dynamoDbAsyncClient(
            DynamoDbProperties properties,
            SdkAsyncHttpClient httpClient
    ) {
        return DynamoDbAsyncClient.builder()
                .credentialsProvider(AnonymousCredentialsProvider.create())
                .endpointOverride(URI.create(properties.getEndpoint()))
                .region(Region.of(properties.getRegion()))
                .httpClient(httpClient)
                .build();
    }
}