package by.clevertec.gateway.security.expression;//package by.clevertec.gateway.security.expression;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//import java.util.Map;
//
//@RequiredArgsConstructor
//@Component("gatewaySecurityExpression")
//public class GatewaySecurityExpression {
//
//
//    public boolean canAccess(Long id, String url, String role) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String name = authentication.getName();
//        if (name == null) {
//            return false;
//        }
//        RestTemplate restTemplate = new RestTemplate();
//        Map<String, Object> newsMap = restTemplate.getForObject(url, Map.class, id);
//        String author = (String) (newsMap != null ? newsMap.get("author") : null);
//
//        if (author != null) {
//            return name.equals(author) && hasRole(authentication, role) || isAdmin(authentication);
//        } else {
//            return false;
//        }
//    }
//
//    public boolean canAccessNews(Long id) {
//        String newsUrl = "http://localhost:8001/news/{newsId}";
//        return canAccess(id, newsUrl, "JOURNALIST");
//    }
//
//    public boolean canAccessComment(Long id) {
//        String newsUrl = "http://localhost:8001/comment/{Id}";
//        return canAccess(id, newsUrl, "SUBSCRIBER");
//    }
//
//    private boolean isAdmin(Authentication authentication) {
//        return authentication.getAuthorities().stream()
//                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
//    }
//
//    private boolean hasRole(Authentication authentication, String role) {
//        return authentication.getAuthorities().stream()
//                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
//    }
//}
