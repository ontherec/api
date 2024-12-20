package kr.ontherec.backend.global.config;

import static org.springframework.security.config.http.SessionCreationPolicy.*;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import jakarta.servlet.http.HttpServletRequest;
import lombok.val;

@Configuration
public class SpringSecurityConfiguration {
	@Value("${cors.allowed-origins}")
	private List<String> allowedOrigins;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.sessionManagement(session -> session.sessionCreationPolicy(STATELESS));
		http.csrf(AbstractHttpConfigurer::disable);

		http.formLogin(AbstractHttpConfigurer::disable);
		http.httpBasic(AbstractHttpConfigurer::disable);

		http.authorizeHttpRequests(
			requests -> requests.anyRequest().permitAll()
		);

		// cors
		http.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration config = new CorsConfiguration();
				config.setAllowedOrigins(allowedOrigins);
				config.setAllowedMethods(Collections.singletonList("*"));
				config.setAllowedHeaders(Collections.singletonList("*"));
				config.setExposedHeaders(List.of("Authorization"));
				config.setAllowCredentials(true);
				config.setMaxAge(3600L);
				return config;
			}
		}));

		// jwt
		http.oauth2ResourceServer(
			oauth2 -> oauth2.jwt(Customizer.withDefaults())
		);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public CompromisedPasswordChecker compromisedPasswordChecker() {
		return new HaveIBeenPwnedRestApiPasswordChecker();
	}

	@Bean
	public KeyPair keyPair() throws NoSuchAlgorithmException {
		try {
			val keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			return keyPairGenerator.generateKeyPair();
		} catch (Exception ex) {
			// TODO: 에러 처리
			throw new RuntimeException(ex);
		}
	}

	@Bean
	public RSAKey rsaKey(KeyPair keyPair) {
		return new RSAKey.Builder((RSAPublicKey)keyPair.getPublic())
			.privateKey(keyPair.getPrivate())
			.keyID(UUID.randomUUID().toString())
			.build();
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
		val jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, ctx) -> jwkSelector.select(jwkSet);
	}

	@Bean
	public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
		return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
	}

	@Bean
	public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
		return new NimbusJwtEncoder(jwkSource);
	}
}
