package br.com.cbgomes.webflux.contact.integration;

import br.com.cbgomes.webflux.contact.domain.Contact;
import br.com.cbgomes.webflux.contact.repository.ContactRepository;
import br.com.cbgomes.webflux.contact.service.ContactServiceImpl;
import br.com.cbgomes.webflux.contact.util.ContactCreator;
import br.com.cbgomes.webflux.security.ClientWeb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@ExtendWith ( SpringExtension.class )
@WebFluxTest
@Import( ContactServiceImpl.class )
public class ContactResourceIt {

    @MockBean
    private ContactRepository repository;

    @Autowired

    private ClientWeb clientWeb;

    private WebTestClient webTestClientUser;

    private WebTestClient webTestClientAdmin;

    //private WebTestClient webTestClientUserInvalid;

    private final Contact contact = ContactCreator.createContactValidCreate();

    @BeforeEach
    public void setUp() {

        this.webTestClientUser = this.clientWeb.webClientAuthenticated("user", "cbgomes");

        this.webTestClientAdmin = this.clientWeb.webClientAuthenticated("admin", "cbgomes");

       // this.webTestClientUserInvalid = this.clientWeb.webClientAuthenticated("invalid", "invalid");

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
        webTestClientAdmin
                .get()
                .uri("/api/reactive/contacts")
                .exchange()
                .expectBodyList(Contact.class)
                .hasSize(1)
                .contains(contact);

    }

    @Test
    @Description("save -> return the contact exists")
    public void save_ReturnMonoOfContacts_WhenSuccessful(){
        Contact contactSaved = ContactCreator.createContactBuilderValid();
        webTestClientUser
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
    public void saveBatch_ReturnMonoOfContacts_WhenSuccessful(){
        Contact contactSaved = ContactCreator.createContactBuilderValid();
        webTestClientUser
                .post()
                .uri("/api/reactive/contacts/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(List.of(contactSaved,contactSaved)))
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(Contact.class)
                .hasSize(2)
                .contains(contact);

    }

    @Test
    @Description("findById -> return the contact exists by id")
    public void findById_ReturnContactMono_WhenSuccessful() {
        webTestClientUser
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

        webTestClientUser
                .get()
                .uri("/api/reactive/contacts/{id}", 1)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404);

    }

    @Test
    @DisplayName("delete removes the contact when successful")
    public void delete_RemovesContact_WhenSuccessful() {
        webTestClientUser
                .delete()
                .uri("/api/reactive/contacts/{id}", 1)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("delete returns Mono error when contact does not exist")
    public void delete_ReturnMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(this.repository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.empty());

        webTestClientUser
                .delete()
                .uri("/api/reactive/contacts/{id}", 1)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404);

    }

    @Test
    @DisplayName("update save updated contact and returns empty mono when successful")
    public void update_SaveUpdatedContact_WhenSuccessful() {
        webTestClientUser
                .put()
                .uri("/api/reactive/contacts/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(contact))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("update returns Mono error when contact does not exist")
    public void update_ReturnMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(this.repository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.empty());

        webTestClientUser.put()
                .uri("/api/reactive/contacts/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(contact))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404);
    }
}
