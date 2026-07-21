package pe.edu.upc.smartdrive.platform.iam.infrastructure.authorization.sfs.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pe.edu.upc.smartdrive.platform.iam.infrastructure.authorization.sfs.pipeline.BearerAuthorizationRequestFilter;
import pe.edu.upc.smartdrive.platform.iam.infrastructure.tokens.jwt.BearerTokenService;

import java.util.List;

/**
 * Spring Security configuration for a stateless, token-based API.
 *
 * <p>Sign-in validates the password directly in the application layer (see
 * {@code UserCommandServiceImpl}) and every protected request is authorized by the
 * {@link BearerAuthorizationRequestFilter} that reads the JWT. As a result no
 * {@code AuthenticationManager}, {@code AuthenticationProvider} or extra
 * {@code PasswordEncoder} bean is required here, which keeps the wiring simple and
 * avoids duplicate-bean ambiguities. CORS is opened for the Angular client and an
 * allowlist permits the authentication, TOTP, documentation and H2 console routes.</p>
 */
@Configuration
@EnableMethodSecurity
public class WebSecurityConfiguration {

    @Value("${spring.h2.console.enabled:false}")
    private boolean h2ConsoleEnabled;

    private final UserDetailsService userDetailsService;
    private final BearerTokenService tokenService;
    private final AuthenticationEntryPoint unauthorizedRequestHandler;

    public WebSecurityConfiguration(UserDetailsService userDetailsService,
                                    BearerTokenService tokenService,
                                    AuthenticationEntryPoint authenticationEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
        this.unauthorizedRequestHandler = authenticationEntryPoint;
    }

    @Bean
    public BearerAuthorizationRequestFilter authorizationRequestFilter() {
        return new BearerAuthorizationRequestFilter(tokenService, userDetailsService);
    }

    /**
     * CORS origins are read from the {@code CORS_ORIGIN} environment variable (comma-separated),
     * falling back to the local dev origins. This lets the deployed frontend (e.g. the Vercel URL)
     * talk to the API without recompiling: set CORS_ORIGIN in the hosting provider.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @org.springframework.beans.factory.annotation.Value(
                "${cors.allowed-origins:http://localhost:4200,http://localhost:3000}") String allowedOrigins) {
        var cors = new CorsConfiguration();
        cors.setAllowedOrigins(java.util.Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(o -> !o.isBlank())
                .toList());
        cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cors.setAllowedHeaders(List.of("*"));
        cors.setAllowCredentials(true);
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http.cors(configurer -> configurer.configurationSource(corsConfigurationSource));
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedRequestHandler))
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(
                            "/authentication/**",
                            "/advisor/**",
                            "/totp-setup",
                            "/verify-totp-setup",
                            "/verify-totp",
                            "/v3/api-docs/**",
                            "/swagger-ui.html",
                            "/swagger-ui/**",
                            "/swagger-resources/**",
                            "/webjars/**").permitAll();
                    if (h2ConsoleEnabled) {
                        authorize.requestMatchers("/h2-console/**").permitAll();
                    }
                    authorize.anyRequest().authenticated();
                });
        http.headers(headers -> {
            if (h2ConsoleEnabled) {
                headers.frameOptions(frame -> frame.disable());
            }
        });
        http.addFilterBefore(authorizationRequestFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
