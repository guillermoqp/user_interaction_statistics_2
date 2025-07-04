package co.com.bancolombia.api;

import co.com.bancolombia.api.StatsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class StatsRouter {

    @Bean
    public RouterFunction<?> statsRoutes(StatsHandler handler) {
        return route(POST("/stats"), handler::handleStats);
    }
}