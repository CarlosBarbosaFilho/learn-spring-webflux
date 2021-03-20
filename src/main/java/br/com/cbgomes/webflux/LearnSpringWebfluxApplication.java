package br.com.cbgomes.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class LearnSpringWebfluxApplication {

//    static {
//        BlockHound.install(builder -> builder.allowBlockingCallsInside("java.util.UUID", "randomUUID"));
//    }
//
    public static void main(String[] args) {
        SpringApplication.run(LearnSpringWebfluxApplication.class, args);
    }

}
