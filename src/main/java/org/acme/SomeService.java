package org.acme;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.kafka.Record;

import javax.annotation.PostConstruct;

@ApplicationScoped
public class SomeService {

    Random random = new Random();

    @ConfigProperty(name = "service.name")
    String serviceName;

    @ConfigProperty(name = "service.cities")
    String cities;
    
    List<String> convertedCities;

    @PostConstruct
    public void init() {
        convertedCities = Arrays.asList(cities.split(",", -1));
    }
    
    private String randomCity() {
        return convertedCities.get(random.nextInt(convertedCities.size()));
    }

    public String greeting(String name) {
        return "[" + serviceName + "] Greeting from " + randomCity() + ": " + name;
    }

    @Outgoing("weather")
    public Multi<Record<String, Integer>> generate() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(1))
            .map(x -> Record.of(randomCity(), random.nextInt(-10,30)));
    }

}