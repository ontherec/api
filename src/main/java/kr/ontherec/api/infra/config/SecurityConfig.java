package kr.ontherec.api.infra.config;

import kr.ontherec.api.infra.filter.ApiKeyAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;

@Configuration
public class SecurityConfig {
    public static final String API_KEY_HEADER = "X-API-KEY";

    @Value("${spring.security.api-key}")
    private String API_KEY;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new JwtRoleConverter());

        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .headers(hc -> hc.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .cors(corsConfig -> corsConfig.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    // TODO: 배포시 수정
                    config.setAllowedOrigins(List.of(
                            "http://localhost:3000",
                            "https://localhost:3000",
                            "http://docs.ontherec.live",
                            "https://docs.ontherec.live",
                            "http://ontherec.kr",
                            "https://ontherec.kr"
                    ));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setExposedHeaders(List.of("Authorization"));
                    config.setAllowCredentials(true);
                    config.setMaxAge(3600L);
                    return config;
                }))
                .authorizeHttpRequests(arc -> arc
                        .requestMatchers(GET, "/v1/hosts/**").permitAll()
                        .requestMatchers(GET, "/v1/places/**").permitAll()
                        .requestMatchers(GET, "/v1/stages/**").permitAll()
                        .requestMatchers(GET, "/v1/posts/**").permitAll()
                        .requestMatchers("/static/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new ApiKeyAuthenticationFilter(API_KEY_HEADER, API_KEY), UsernamePasswordAuthenticationFilter.class)
                .oauth2ResourceServer(orc -> orc.jwt(jc -> jc.jwtAuthenticationConverter(jwtAuthenticationConverter)));

        return http.build();
    }
}
