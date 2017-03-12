package com.example;

import com.example.model.Request;
import com.example.model.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Bean;
import rx.Observable;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableBinding(Processor.class)
@EnableConfluentSchemaRegistryClient
public class SpringCloudStreamKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudStreamKafkaApplication.class, args);
    }

    @Value("${valid-counterparties}")
    String validCounterparties;


    @StreamListener
    @Output(Processor.OUTPUT)
    public Observable<Response> processor(@Input(Processor.INPUT) Observable<Request> requests) {
        return requests.map(request -> {
            Response response = responseFactory().generateResponse(request);
            processContainer().process(request, response);
            return response;
        });
    }

    @Bean
    Set<String> validCounterparties() {
        return Arrays.stream(validCounterparties.split(",")).collect(Collectors.toSet());
    }

    @Bean
    ResponseFactory<Request, Response> responseFactory() {
        return request -> {
            Response response = new Response();
            response.setCorrelationId(request.getCorrelationId());
            return response;
        };
    }

    @Bean
    DTPProcessContainer<Request, Response> processContainer() {
        return new DTPProcessContainerImpl(validCounterparties());
    }
}
