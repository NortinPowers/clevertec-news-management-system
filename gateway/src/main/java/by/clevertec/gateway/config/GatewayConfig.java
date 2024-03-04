package by.clevertec.gateway.config;//package by.clevertic.gateway.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.cloud.gateway.route.RouteLocator;
//
//@Configuration
//public class GatewayConfig {
//
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("news", r -> r
//                        .path("/api/news/**")
//                        .uri("http://localhost:8001"))
//                .route("comment", r -> r
//                        .path("/api/comment/**")
//                        .uri("http://localhost:8002"))
//                .build();
//    }
//}
