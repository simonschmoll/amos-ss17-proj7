package de.fau.amos.virtualledger.server.jetty_test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldService {

    @Value("${name:User}")
    private String name;

    public String getHelloMessage() {
        return name;
    }

}
