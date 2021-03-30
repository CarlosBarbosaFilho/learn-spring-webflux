package br.com.cbgomes.webflux.contact.integration;

import br.com.cbgomes.webflux.contact.domain.Contact;
import br.com.cbgomes.webflux.contact.repository.ContactRepository;
import br.com.cbgomes.webflux.contact.service.ContactService;
import br.com.cbgomes.webflux.contact.service.ContactServiceImpl;
import br.com.cbgomes.webflux.contact.util.ContactCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import( ContactServiceImpl.class )
public class ContactResourceIt {

    private static final String ADMIN_USER = "admin";
    private static final String REGULAR_USER = "user";

    @MockBean
    private ContactRepository repository;

    @Autowired
    private WebTestClient webTestClient;

    private final Contact contact = ContactCreator.createContactValidCreate();

    @BeforeEach
    public void setUp() {
        BDDMockito.when(this.repository.findAll())
                .thenReturn(Flux.just(contact));

        BDDMockito.when(this.repository.save(ContactCreator.createContactBuilderValid()))
                .thenReturn(Mono.just(contact));

        BDDMockito.when(this.repository.save(ContactCreator.createContactValidUpdate()))
                .thenReturn(Mono.just(contact));

        BDDMockito.when(this.repository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.just(contact));

        BDDMockito.when(this.repository
                .saveAll(List.of(ContactCreator.createContactBuilderValid(),ContactCreator.createContactBuilderValid())))
                .thenReturn(Flux.just(contact, contact));

        BDDMockito.when(this.repository.delete(ArgumentMatchers.any(Contact.class)))
                .thenReturn(Mono.empty());
    }

    @Test
    @Description("getAll -> return all contact")
    public void getAll_ReturnFluxOfContacts_WhenSuccessful(){

        webTestClient
                .get()
                .uri("/api/reactive/contacts")
                .exchange()
                .expectBodyList(Contact.class)
                .hasSize(1)
                .contains(contact);

    }

    @Test
    @Description("save -> return the contact exists")
    @WithUserDetails (ADMIN_USER)
    public void save_ReturnMonoOfContacts_WhenSuccessful(){
        Contact contactSaved = ContactCreator.createContactBuilderValid();
        webTestClient
                .post()
                .uri("/api/reactive/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(contactSaved))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Contact.class)
                .isEqualTo(contact);

    }

    @Test
    @Description("saveBatch -> return all contacts saved in batch")
    @WithUserDetails (ADMIN_USER)
    public void saveBatch_ReturnMonoOfContacts_WhenSuccessful(){
        Contact contactSaved = ContactCreator.createContactBuilderValid();

        webTestClient
                .post()
                .uri("/api/reactive/contacts/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(List.of(contactSaved, contactSaved)))
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(Contact.class)
                .hasSize(2)
                .contains(contact,contact);

    }

    @Test
    @Description("findById -> return the contact exists by id")
    public void findById_ReturnContactMono_WhenSuccessful() {
        webTestClient
                .get()
                .uri("/api/reactive/contacts/{id}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Contact.class)
                .isEqualTo(contact);
    }


    @Test
    @DisplayName ("findById returns Mono error when contact does not exist")
    public void findById_ReturnMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(this.repository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.empty());

        webTestClient
                .get()
                .uri("/api/reactive/contacts/{id}", 1)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404);

    }

    @Test
    @DisplayName("delete removes the contact when successful")
    @WithUserDetails (ADMIN_USER)
    public void delete_RemovesContact_WhenSuccessful() {
        webTestClient
                .delete()
                .uri("/api/reactive/contacts/{id}", 1)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("delete returns Mono error when contact does not exist")
    @WithUserDetails (ADMIN_USER)
    public void delete_ReturnMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(this.repository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.empty());

        webTestClient
                .delete()
                .uri("/api/reactive/contacts/{id}", 1)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404);

    }

    @Test
    @DisplayName("update save updated contact and returns empty mono when successful")
    @WithUserDetails (ADMIN_USER)
    public void update_SaveUpdatedContact_WhenSuccessful() {
        webTestClient
                .put()
                .uri("/api/reactive/contacts/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(contact))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("update returns Mono error when contact does not exist")
    @WithUserDetails (ADMIN_USER)
    public void update_ReturnMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(this.repository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.empty());

        webTestClient.put()
                .uri("/api/reactive/contacts/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(contact))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404);
    }
}
