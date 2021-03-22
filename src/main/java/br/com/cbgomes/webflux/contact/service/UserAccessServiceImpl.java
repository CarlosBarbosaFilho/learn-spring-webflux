package br.com.cbgomes.webflux.contact.service;

import br.com.cbgomes.webflux.contact.repository.UserAccessRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserAccessServiceImpl implements  UserAccessService, ReactiveUserDetailsService {

    private final UserAccessRepository repository;

    @Override
    public Mono<UserDetails> findByUsername(String userName) {
        return this.repository.findByUsername(userName).cast(UserDetails.class);
    }
}
