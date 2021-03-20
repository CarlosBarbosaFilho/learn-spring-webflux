package br.com.cbgomes.webflux.contact.resource;

import br.com.cbgomes.webflux.contact.domain.Contact;
import br.com.cbgomes.webflux.contact.repository.ContactRepository;
import br.com.cbgomes.webflux.contact.service.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/reactive/contacts")
@Slf4j
public class ContactResource {

    private final ContactService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Contact> getAllContacts(){
        return this.service.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Contact> getContactById(@PathVariable("id") Long id){
        return this.service.findById(id);
    }

    @GetMapping("/by-email/{email}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Contact> getContactByEmail(@PathVariable("email") String email){
        return this.service.findByEmail(email);
    }

    @PostMapping
    @ResponseStatus( HttpStatus.CREATED)
    public Mono<Contact> createContract(@Valid @RequestBody Contact contact) {
        return this.service.create(contact);
    }

    @PostMapping("/batch")
    @ResponseStatus( HttpStatus.CREATED)
    public Flux<Contact> createBatch(@RequestBody List<Contact> contacts) {
        return this.service.createBatch(contacts);
    }


    @PutMapping("/{id}")
    public Mono<Contact> updateContact(@Valid @PathVariable("id") Long id,  @RequestBody Contact contact){
        return this.service.update(contact.withId(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteContact(@PathVariable("id") Long id){
        return this.service.delete(id);
    }
}
