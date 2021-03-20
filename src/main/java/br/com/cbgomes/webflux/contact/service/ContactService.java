package br.com.cbgomes.webflux.contact.service;

import br.com.cbgomes.webflux.contact.domain.Contact;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


public interface ContactService {

    Flux<Contact> findAll();

    Mono<Contact> findById(Long id);

    Mono<Contact> findByEmail(String email);

    Mono<Contact> create(Contact contact);

    Mono<Contact> update(Contact contact);

    Mono<Void> delete(Long id);

    Flux<Contact> createBatch(List<Contact> contacts);
}
