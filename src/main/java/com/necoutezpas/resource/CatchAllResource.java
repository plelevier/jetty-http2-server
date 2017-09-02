package com.necoutezpas.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.glassfish.jersey.media.sse.OutboundEvent;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Map;

@Path("/")
public class CatchAllResource {
    private static ObjectMapper objectMapper = new ObjectMapper();

    @GET
    @Path("{seg:.*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLog(@Context UriInfo uriInfo, @Context HttpHeaders headers, @Context HttpServletRequest request)
            throws JsonProcessingException {
        Map<String,Object> result = ImmutableMap.of(
                "protocol", request.getProtocol(),
                "method", request.getMethod(),
                "path", uriInfo.getPath(),
                "headers", headers.getRequestHeaders()
        );
        SseWatchResource.write(new OutboundEvent.Builder()
                .name("log")
                .data(String.class, objectMapper.writeValueAsString(result))
                .build());
        return Response.ok().build();
    }

    @POST
    @Path("{seg:.*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postLog(@Context UriInfo uriInfo, @Context HttpHeaders headers, @Context HttpServletRequest request,
                        String body)
            throws JsonProcessingException {
        Map<String,Object> result = ImmutableMap.of(
                "protocol", request.getProtocol(),
                "method", request.getMethod(),
                "path", uriInfo.getPath(),
                "headers", headers.getRequestHeaders(),
                "body", body
        );
        SseWatchResource.write(new OutboundEvent.Builder()
                .name("log")
                .data(String.class, objectMapper.writeValueAsString(result))
                .build());
        return Response.ok().build();
    }

}