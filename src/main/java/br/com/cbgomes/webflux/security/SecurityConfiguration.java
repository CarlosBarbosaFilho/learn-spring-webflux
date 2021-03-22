package br.com.cbgomes.webflux.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        //@formatter:off
            return
                http
                    .csrf()
                    .disable()
                .authorizeExchange()
                    .pathMatchers(HttpMethod.POST, "/api/reactive/contacts/**").hasAnyRole("ADMIN")
                    .pathMatchers(HttpMethod.PUT, "/api/reactive/contacts/**").hasAnyRole("ADMIN")
                    .pathMatchers(HttpMethod.DELETE, "/api/reactive/contacts/**").hasAnyRole("ADMIN")
                    .pathMatchers(HttpMethod.GET, "/api/reactive/contacts/**").hasAnyRole("USER")
                    .anyExchange().authenticated()
                .and()
                    .formLogin()
                .and()
                    .httpBasic()
                .and()
                    .build();
        //@formatter:on
    }

    @Bean
    public MapReactiveUserDetailsService mapReactiveUserDetailsService(){
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("cbgomes"))
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("cbgomes"))
                .roles("ADMIN","USER")
                .build();

        return new MapReactiveUserDetailsService(user,admin);
    }
}
