package kr.ontherec.api.global.config;

import kr.ontherec.api.global.filter.ApiKeyAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Value("${spring.security.api-key}")
    private String API_KEY;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new JwtRoleConverter());

        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(corsConfig -> corsConfig.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    // TODO: 배포시 수정
                    config.setAllowedOrigins(List.of(
                            "http://localhost:3000",
                            "https://localhost:3000",
                            "http://docs.ontherec.live",
                            "https://docs.ontherec.live"));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setExposedHeaders(List.of("Authorization"));
                    config.setAllowCredentials(true);
                    config.setMaxAge(3600L);
                    return config;
                }))
                .authorizeHttpRequests(arc -> arc.anyRequest().authenticated())
                .addFilterBefore(new ApiKeyAuthenticationFilter(API_KEY), UsernamePasswordAuthenticationFilter.class)
                .oauth2ResourceServer(orc -> orc.jwt(jc -> jc.jwtAuthenticationConverter(jwtAuthenticationConverter)));

        return http.build();
    }
}
