package com.necoutezpas.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Path("/sse")
public class SseResource {
    private static ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static Logger logger = LoggerFactory.getLogger(SseResource.class);
    private static ObjectMapper mapper = new ObjectMapper();
    private static volatile EventOutput eventOutput = new EventOutput();

    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput getMessageQueue() {
        return eventOutput;
    }

    @POST
    public void addMessage(final String message) throws IOException {
        final EventOutput localOutput = eventOutput;
        if (localOutput != null) {
            eventOutput.write(new OutboundEvent.Builder().name("custom-message").data(String.class, message).build());
        }
    }

    @DELETE
    public void close() throws IOException {
        final EventOutput localOutput = eventOutput;
        if (localOutput != null) {
            eventOutput.close();
        }
        setEventOutput(new EventOutput());
    }

    @POST
    @Path("domains/{id}")
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput startDomain(@PathParam("id") final String id) {
        final EventOutput seq = new EventOutput();

        executorService.submit(() -> {
            try {
                seq.write(new OutboundEvent.Builder().name("domain-progress")
                        .data(String.class, mapper.writeValueAsString("starting domain " + id + " ...")).build());
                Thread.sleep(200);
                seq.write(new OutboundEvent.Builder().name("domain-progress").data(String.class, mapper.writeValueAsString("50%")).build());
                Thread.sleep(200);
                seq.write(new OutboundEvent.Builder().name("domain-progress").data(String.class, mapper.writeValueAsString("60%")).build());
                Thread.sleep(200);
                seq.write(new OutboundEvent.Builder().name("domain-progress").data(String.class, mapper.writeValueAsString("70%")).build());
                Thread.sleep(200);
                seq.write(new OutboundEvent.Builder().name("domain-progress").data(String.class, mapper.writeValueAsString("99%")).build());
                Thread.sleep(200);
                seq.write(new OutboundEvent.Builder().name("domain-progress").data(String.class, mapper.writeValueAsString("done")).build());
                seq.close();

            } catch (final InterruptedException | IOException e) {
                logger.warn(e.getMessage(), e);
            }
        });

        return seq;
    }

    private static void setEventOutput(final EventOutput eventOutput) {
        SseResource.eventOutput = eventOutput;
    }
}
