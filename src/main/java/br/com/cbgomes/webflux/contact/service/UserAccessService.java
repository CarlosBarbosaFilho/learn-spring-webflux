package br.com.cbgomes.webflux.contact.service;

import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public interface UserAccessService {
    public Mono<UserDetails> findByUsername(String userName);
}
