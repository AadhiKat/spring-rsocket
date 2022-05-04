package com.aadhikat.springrsocket.security;

import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;
import org.springframework.security.rsocket.metadata.SimpleAuthenticationEncoder;

@Configuration
@EnableRSocketSecurity
public class RSocketSecurityConfig {

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    // This is the bean which helps in encoding/decoding the MimeType
    // By default, spring comes with all encoders and decoders and documentation explicitly says not to modify it and go with defaults.
    // For eg: You send request to RSocket server and the server responds you back. For us everything is a Java object, but behind the scenes, they use Jackson2CborEncoder / Decoder.
    // Now, let's say if you want to use protobuf, then you need to provide your own encoder / decoder.
    @Bean
    public RSocketStrategiesCustomizer strategiesCustomizer() {
        return c -> c.encoder(new SimpleAuthenticationEncoder());
    }

    @Bean
    public PayloadSocketAcceptorInterceptor interceptor(RSocketSecurity security) {
        return security
                .simpleAuthentication( Customizer.withDefaults())
                .authorizePayload(
                        authorize -> authorize
                                .setup().hasRole("TRUSTED_CLIENT")
                                .route("*.*.*.table").hasRole("ADMIN")
                                .route("math.service.secured.square").hasRole("USER")
                                .anyRequest().denyAll()
                ).build();
    }
}
