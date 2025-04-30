package com.evaluacion.usuario_api.config;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        Argon2 argon2 = Argon2Factory.create();

        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return argon2.hash(2, 65536, 1, rawPassword.toString().toCharArray());
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return argon2.verify(encodedPassword, rawPassword.toString().toCharArray());
            }
        };
    }
}