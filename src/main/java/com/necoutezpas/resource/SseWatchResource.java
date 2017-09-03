package com.necoutezpas.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import java.io.IOException;
import java.util.concurrent.*;

@Path("/sse")
public class SseWatchResource {
    private static ObjectMapper objectMapper = new ObjectMapper();

    private static final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(10000);
    private static ExecutorService executorService =  new ThreadPoolExecutor(100, 100,
            0L, TimeUnit.MILLISECONDS,
            queue);
    private static ConcurrentMap<EventOutput,Boolean> watchers = new ConcurrentHashMap<>();
    private static Logger logger = LoggerFactory.getLogger(SseWatchResource.class);

    @GET
    @Path("watch")
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    public EventOutput watch() {
        final EventOutput watcher = new EventOutput();
        watchers.put(watcher, true);
        return watcher;
    }

    public static void emit(String name, Object object) {
        executorService.submit(() -> {
            try {
                OutboundEvent event = new OutboundEvent.Builder()
                        .name(name)
                        .data(String.class, objectMapper.writeValueAsString(object))
                        .build();
                for (EventOutput watcher : watchers.keySet()) {
                    try {
                        watcher.write(event);
                    } catch (Exception e) {
                        if (e instanceof IOException) {
                            logger.warn(e.getMessage());
                        } else {
                            logger.error(e.getMessage(), e);
                        }
                        watchers.remove(watcher);
                        try {
                            watcher.close();
                        } catch (IOException e1) {
                            logger.warn(e1.getMessage(), e);
                        }
                    }
                }
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage(), e);
            }
        });
    }
}
