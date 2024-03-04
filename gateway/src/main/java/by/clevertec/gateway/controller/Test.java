package by.clevertec.gateway.controller;//package by.clevertec.gateway.controller;
//
//import by.clevertec.auth.JwtRequest;
//import by.clevertec.auth.JwtResponse;
//import by.clevertec.gateway.client.AuthServiceClient;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//
//@RestController
//@RequiredArgsConstructor
//@Tag(name = "Gate", description = "Gate management API")
//public class Test {
//
//    private final ObjectMapper mapper;
//
////    private final AuthServiceClient authServiceClient;
//
//    @GetMapping("/api/test/{id}")
//    public ResponseEntity<Object> getSome(@PathVariable Long id) {
//        ResponseEntity<Object> responseEntity =
//                new RestTemplate().getForEntity(
//                        "http://localhost:8001/news/{id}", Object.class, id);
//        return responseEntity;
//    }
//
//    @GetMapping("/news/{newsId}/comments")
//    public ResponseEntity<Object> getSomeValue(@PathVariable Long newsId) {
//        RestTemplate restTemplate = new RestTemplate();
//        String newsUrl = "http://localhost:8001/news/{newsId}";
//        Map<String, Object> newsMap = restTemplate.getForObject(newsUrl, Map.class, newsId);
//        String commentUrl = "http://localhost:8002/comments/news/{newsId}";
//        List<Map<String, Object>> comments = restTemplate.getForObject(commentUrl, List.class, newsId);
//        Objects.requireNonNull(newsMap).put("comments", comments);
//        return ResponseEntity.ok(newsMap);
//    }
//
//    @GetMapping("/api/news")
//    @PreAuthorize("hasAuthority('ADMIN')")
////    @PreAuthorize("hasRole('ROLE_ADMIN')")   от @EnableGlobalMethodSecurity88
////    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<String> getAuthorName() {
//        return ResponseEntity.ok("Success admin");
//    }
//
////    @PostMapping("/api/auth")
//////    @PreAuthorize("isAnonymous()")
////    public ResponseEntity<JwtResponse> createAuthToken(@RequestBody @Valid JwtRequest request) {
////        return authServiceClient.createAuthToken(request);
////    }
//
//
//
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
////
//
//
//
////        if (authorities.contains(new SimpleGrantedAuthority("ADMIN"))) {
////            System.out.println("admin");
////            return ResponseEntity.ok("Success admin");
////        } else {
////            System.out.println("not admin");
////            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
////        }
////    @GetMapping("/api/news/{newsId}")
////    public ResponseEntity<Object> getRegister(@PathVariable Long newsId) {
////
////        String authorRole = userServiceClient.getUserRoleByName(authorName);
////        return null;
//////    }
////}
//
//    //TODO
////    @FeignClient(name = "comments", url = "http://localhost:8002/comments/{id}")
////    @FeignClient(name = "comments", url = "http://localhost:8002")
////    public interface ServiceNameClient {
////        @GetMapping("/comments/{id}")
////        ResponseEntity<String> getEndpointData(@PathVariable Long id);
////    }
////        String title = (String) Objects.requireNonNull(commentsMap).get("title");
//}
