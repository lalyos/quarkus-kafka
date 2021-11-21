package org.acme;

import org.eclipse.microprofile.reactive.messaging.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import javax.inject.Inject;
import org.apache.kafka.clients.consumer.ConsumerRecord;

@Path("/hello")
public class GreetingResource {

    @Inject
    SomeService service;

    @Inject
    @Channel("foods")
    Emitter<String> foodEmitter;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/greeting/{name}")
    public String greeting(@PathParam String name) {
        foodEmitter.send(name);
        return service.greeting(name);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }

    @Incoming("animals")
public void consume(ConsumerRecord<String, String> record) {
    String key = record.key(); // Can be `null` if the incoming record has no key
    String value = record.value(); // Can be `null` if the incoming record has no value
    String topic = record.topic();
    int partition = record.partition();
    System.out.println(topic+":" + partition + " k: "+key+" v:"+value);
}
}