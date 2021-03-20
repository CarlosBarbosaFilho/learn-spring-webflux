package br.com.cbgomes.webflux.contact.service;

import br.com.cbgomes.webflux.contact.domain.Contact;
import br.com.cbgomes.webflux.contact.repository.ContactRepository;
import br.com.cbgomes.webflux.contact.util.ContactCreator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;


@ExtendWith( SpringExtension.class )
class ContactServiceImplTest {

    @InjectMocks
    private ContactServiceImpl  service;

    @Mock
    private ContactRepository repository;

    private final Contact contact = ContactCreator.createContactValidCreate();

    @BeforeEach
    public void setUp(){
        BDDMockito.when(this.repository.findAll())
                .thenReturn(Flux.just(contact));

        BDDMockito.when(this.repository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.just(contact));

        BDDMockito.when(this.repository.save(ContactCreator.createContactValidCreate()))
                .thenReturn(Mono.just(contact));

        BDDMockito.when(this.repository
                .saveAll(List.of(ContactCreator.createContactBuilderValid(),ContactCreator.createContactBuilderValid())))
                .thenReturn(Flux.just(contact, contact));

        BDDMockito.when(this.repository.save(ContactCreator.createContactBuilderValid()))
                .thenReturn(Mono.just(contact));

        BDDMockito.when(this.repository.delete(ArgumentMatchers.any(Contact.class)))
                .thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("findAll -> return Flux contacts saved")
    public void findAll_ReturnFlux_ContactsSuccess(){
        StepVerifier.create(this.service.findAll())
                .expectSubscription()
                .expectNext(contact)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById -> return  Mono contact when it exists")
    public void findById_ReturnMono_ContactsSuccess(){
        StepVerifier.create(this.service.findAll())
                .expectSubscription()
                .expectNext(contact)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById -> return  Mono erro, doesn't exists")
    public void findByIdReturn_MonoError_ContactsNotExists(){
        BDDMockito.when(this.repository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.empty());

        StepVerifier.create(this.service.findById(1L))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }


    @Test
    @DisplayName("save -> create and return an Mono contact when success")
    public void create_ReturnMono_ContactsCreatedSuccess(){
        final Contact contactValidCreate = ContactCreator.createContactBuilderValid();
        StepVerifier.create(this.service.create(contactValidCreate))
                .expectSubscription()
                .expectNext(contact)
                .verifyComplete();
    }

    @Test
    @DisplayName("saveAll -> create and return on list of contact when success")
    public void createAll_ReturnFlux_ContactsCreatedSuccess(){
        final Contact contactValidCreate = ContactCreator.createContactBuilderValid();
        StepVerifier.create(this.service.createBatch(List.of(contactValidCreate, contactValidCreate )))
                .expectSubscription()
                .expectNext(contact,contact)
                .verifyComplete();
    }

    @Test
    @DisplayName("saveAll Error -> create and return mono erro to data invalid in the list")
    public void createAll_ReturnMono_ContactsCreateError(){

        BDDMockito.when(this.repository
                .saveAll(ArgumentMatchers.anyIterable()))
                .thenReturn(Flux.just(contact, contact.withName(null)));

        final Contact contactValidCreate = ContactCreator.createContactBuilderValid();
        StepVerifier.create(this.service.createBatch(List.of(contactValidCreate, contactValidCreate.withName(""))))
                .expectSubscription()
                .expectNext(contact)
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("delete -> remove an Mono contact when success")
    public void delete_ReturnMonoVoid_ContactsDeletedSuccess(){
        StepVerifier.create(this.service.delete(1L))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("delete -> return error Mono contact not exists")
    public void delete_ReturnError_ContactsDeletedNotExists(){

        BDDMockito.when(this.repository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.empty());

        StepVerifier.create(this.service.delete(1L))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("update -> update and return an Mono contact when success")
    public void update_ReturnMono_ContactsUpdatedSuccess(){
        StepVerifier.create(this.service.update(ContactCreator.createContactValidUpdate()))
                .expectSubscription()
                .expectNext(contact)
                .verifyComplete();
    }
}