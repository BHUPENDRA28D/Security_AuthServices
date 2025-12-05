package UberAuthService.auth.Filter;

import UberAuthService.auth.Services.JWTService;
import UberAuthService.auth.Services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private final RequestMatcher uriMatcher = PathPatternRequestMatcher
            .withDefaults()
            .matcher(HttpMethod.GET, "/api/v1/auth/validate");

    private final JWTService jwtService;
    public JWTAuthFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }


    // Methode to extrat token from client requestes and cookies.
    private  String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("JwtToken".equals(c.getName())) {
                    return c.getValue();
                }
            }
        }
        return null;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Extract JWT from Cookie
        String token = extractTokenFromCookies(request);

        if (token == null) {
            // No token → user not authenticated → let SecurityConfig decide (403/401)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        System.out.println("Incoming token : " + token);

        // 2. Extract email from token
        String email = jwtService.extractEmail(token);
        System.out.println("Incoming email : " + email);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (jwtService.validateToken(token, userDetails.getUsername())) {

                // ✅ AUTHENTICATED token (3-arg ctor)
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        System.out.println("Forwarding req");
        filterChain.doFilter(request, response);
    }



    @Override
protected boolean shouldNotFilter(HttpServletRequest request) {
    // ✅ Filter ALL paths EXCEPT login/signup
    return request.getServletPath().startsWith("/api/v1/auth/signin") ||
            request.getServletPath().startsWith("/api/v1/auth/signup") ||
            request.getServletPath().startsWith("/api/v1/auth/refresh");
}


}
