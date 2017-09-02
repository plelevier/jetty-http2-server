package com.necoutezpas.resource;

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
    private static final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(10000);
    private static ExecutorService executorService =  new ThreadPoolExecutor(100, 100,
            0L, TimeUnit.MILLISECONDS,
            queue);
    private static ConcurrentMap<EventOutput,Boolean> watchers = new ConcurrentHashMap<>();
    private static Logger logger = LoggerFactory.getLogger(SseWatchResource.class);

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
