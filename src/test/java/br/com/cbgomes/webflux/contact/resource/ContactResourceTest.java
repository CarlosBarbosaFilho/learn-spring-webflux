package br.com.cbgomes.webflux.contact.resource;

import br.com.cbgomes.webflux.contact.domain.Contact;
import br.com.cbgomes.webflux.contact.repository.ContactRepository;
import br.com.cbgomes.webflux.contact.service.ContactServiceImpl;
import br.com.cbgomes.webflux.contact.util.ContactCreator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith ( SpringExtension.class )
class ContactResourceTest {

    @InjectMocks
    private ContactResource resource;

    @Mock
    private ContactServiceImpl service;

    private final Contact contact = ContactCreator.createContactValidCreate();

    @BeforeEach
    public void setUp(){
        BDDMockito.when(this.service.findAll())
                .thenReturn(Flux.just(contact));

        BDDMockito.when(this.service.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.just(contact));

        BDDMockito.when(this.service.findByEmail(ArgumentMatchers.any()))
                .thenReturn(Mono.just(contact));

        BDDMockito.when(this.service.create(ContactCreator.createContactBuilderValid()))
                .thenReturn(Mono.just(contact));

        BDDMockito.when(this.service
                .createBatch(List.of(ContactCreator.createContactBuilderValid(),ContactCreator.createContactBuilderValid())))
                .thenReturn(Flux.just(contact, contact));

        BDDMockito.when(this.service.delete(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.empty());

        BDDMockito.when(this.service.update(ContactCreator.createContactValidCreate()))
                .thenReturn(Mono.just(contact));
    }

    @Test
    @DisplayName ("findAll -> return Flux contacts with success")
    void findAllContacts_ReturnAllContactsSuccessful() {
        StepVerifier.create(this.resource.getAllContacts())
                .expectSubscription()
                .expectNext(contact)
                .verifyComplete();
    }

    @Test
    @DisplayName ("findById -> return Mono contacts with success")
    void findContactById_ReturnContactByIdSuccessful() {
        StepVerifier.create(this.resource.getContactById(1L))
                .expectSubscription()
                .expectNext(contact)
                .verifyComplete();
    }

    @Test
    @DisplayName ("findByEmail -> return Mono contacts with success")
    void findByEmail_ReturnContactByEmailSuccessful() {
        StepVerifier.create(this.resource.getContactByEmail("cbgomes@gmail.com"))
                .expectSubscription()
                .expectNext(contact)
                .verifyComplete();
    }

    @Test
    @DisplayName ("create -> return Mono contacts created with success")
    void createContract_ReturnContactSuccessful() {
        StepVerifier.create(this.resource.createContract(ContactCreator.createContactBuilderValid()))
                .expectSubscription()
                .expectNext(contact)
                .verifyComplete();
    }

    @Test
    @DisplayName("saveBatch -> create and return on list of contact when success")
    public void createBatch_ReturnFlux_ContactsCreatedSuccess(){
        final Contact contactValidCreate = ContactCreator.createContactBuilderValid();
        StepVerifier.create(this.resource.createBatch(List.of(contactValidCreate, contactValidCreate )))
                .expectSubscription()
                .expectNext(contact,contact)
                .verifyComplete();
    }

    @Test
    @DisplayName ("update -> return Mono contacts updated with success")
    void updateContact_ReturnContactSuccessful() {
        StepVerifier.create(this.resource.updateContact(1L, ContactCreator.createContactValidCreate()))
                .expectSubscription()
                .expectNext(contact)
                .verifyComplete();
    }

    @Test
    @DisplayName ("delete -> return Mono<VOID> with success")
    void deleteContact_ReturnVoidSuccessful() {
        StepVerifier.create(this.resource.deleteContact(1L))
                .expectSubscription()
                .verifyComplete();
    }
}