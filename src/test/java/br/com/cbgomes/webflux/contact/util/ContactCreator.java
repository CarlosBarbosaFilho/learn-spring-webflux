package br.com.cbgomes.webflux.contact.util;

import br.com.cbgomes.webflux.contact.domain.Contact;

public class ContactCreator {

    public static Contact createContactBuilderValid(){
        return Contact.builder()
                .name("Carlos Barbosa Test")
                .email("cbgomestest@gmail.com")
                .phone("83 9 9126-7778")
                .build();
    }

    public static Contact createContactValidCreate(){
        return Contact.builder()
                .id(1L)
                .name("Carlos Barbosa Test")
                .email("cbgomestest@gmail.com")
                .phone("83 9 9126-7778")
                .build();
    }

    public static Contact createContactValidUpdate(){
        return Contact.builder()
                .id(1L)
                .name("Carlos Barbosa Test")
                .email("cbgomestest@gmail.com")
                .phone("83 9 9126-7778")
                .build();
    }
}
