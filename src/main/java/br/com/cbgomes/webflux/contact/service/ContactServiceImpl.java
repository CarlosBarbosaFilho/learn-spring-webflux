package br.com.cbgomes.webflux.contact.service;

import br.com.cbgomes.webflux.contact.domain.Contact;
import br.com.cbgomes.webflux.contact.repository.ContactRepository;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository repository;

    @Override
    public Flux<Contact> findAll() {
        return this.repository.findAll();
    }

    @Override
    public Mono<Contact> findById(Long id) {
        return this.repository.findById(id)
                .switchIfEmpty(entityNotFoundException());
    }

    @Override
    public Mono<Contact> findByEmail(String email) {
        return this.repository.findByEmail(email);
    }

    @Override
    public Mono<Contact> create(Contact contact) {
        return this.repository.save(contact);
    }

    @Override
    public Mono<Contact> update(Contact contact) {

        return this.findById(contact.getId())
                .map(contactBD -> contact.withId(contactBD.getId()))
                .flatMap(this.repository::save);

    }

    @Override
    public Mono<Void> delete(Long id) {
        return findById(id).flatMap(this.repository::delete);
    }

    @Override
    @Transactional
    public Flux<Contact> createBatch(List<Contact> contacts) {
        return this.repository.saveAll(contacts).doOnNext(this::throwsResponseStatusExceptionWhenEmptyName);
    }

    /**
     * TODO
     * Improve this method to get something more generic, but this not is the focus this project example.
     * @param contact
     */
    private void throwsResponseStatusExceptionWhenEmptyName(Contact contact){
        if(StringUtil.isNullOrEmpty(contact.getName()) || StringUtil.isNullOrEmpty(contact.getEmail())
        || StringUtil.isNullOrEmpty(contact.getPhone())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is not empty or null");
        }
    }

    /**
     * This exception is used to valid if entity not will found.
     * Improve this exception, to be generic in all exceptions this type
     * @param <T>
     * @return
     */
    private <T> Mono<T> entityNotFoundException() {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact no found"));
    }
}
