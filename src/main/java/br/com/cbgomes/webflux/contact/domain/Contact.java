package br.com.cbgomes.webflux.contact.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Table("contacts")
public class Contact {

    @Id
    private Long id;

    @NotNull
    @NotEmpty(message = "The name of this contact cannot by empty" )
    private String name;

    @NotNull
    @NotEmpty(message = "The email of this contact cannot by empty" )
    private String email;

    @NotNull
    @NotEmpty(message = "The phone of this contact cannot by empty" )
    private String phone;
}
