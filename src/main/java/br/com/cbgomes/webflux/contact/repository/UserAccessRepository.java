package br.com.cbgomes.webflux.contact.repository;

import br.com.cbgomes.webflux.contact.domain.UserAccess;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserAccessRepository extends ReactiveCrudRepository<UserAccess, Long> {

    Mono<UserAccess> findByUsername(String userName);
}
