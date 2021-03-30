package br.com.cbgomes.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class LearnSpringWebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnSpringWebfluxApplication.class, args);
        System.out.println(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("cbgomes"));
    }
}
