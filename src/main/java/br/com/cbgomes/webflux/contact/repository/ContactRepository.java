package br.com.cbgomes.webflux.contact.repository;

import br.com.cbgomes.webflux.contact.domain.Contact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ContactRepository extends ReactiveCrudRepository<Contact, Long> {

    Mono<Contact> findByEmail(String email);

    Mono<Contact> findById(Long id);
}
