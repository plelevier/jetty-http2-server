package com.necoutezpas.resource;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Path("/sse")
public class SseResource {
    private static ExecutorService executorService = Executors.newFixedThreadPool(20);
    private static ConcurrentMap<EventOutput,Boolean> watchers = new ConcurrentHashMap<>();
    private static Logger logger = LoggerFactory.getLogger(SseResource.class);

    @POST
    @Path("watch")
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput watch() {
        final EventOutput watcher = new EventOutput();
        watchers.put(watcher, true);
        return watcher;
    }

    public static void write(OutboundEvent event) {
        executorService.submit(() -> {
            for (EventOutput watcher : watchers.keySet()) {
                try {
                    watcher.write(event);
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                    watchers.remove(watcher);
                    try {
                        watcher.close();
                    } catch (IOException e1) {
                        logger.warn(e1.getMessage(), e);
                    }
                }
            }
        });
    }
}
