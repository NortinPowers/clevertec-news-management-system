package by.clevertec.gateway.security.credentional;//package by.clevertec.gateway.security.credentional;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class GatewaySecurityCredential {
//
//    public boolean canAccess(String author, String role) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String name = authentication.getName();
//        //TODO
//        if (name == null || name.equals("anonymous")) {
//            return false;
//        }
//        return name.equals(author) && hasRole(authentication, role) || isAdmin(authentication);
//    }
//
//    public boolean canAccessNews(String author) {
//        return canAccess(author, "JOURNALIST");
//    }
//
//    public boolean canAccessComment(String author) {
//        return canAccess(author, "SUBSCRIBER");
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
