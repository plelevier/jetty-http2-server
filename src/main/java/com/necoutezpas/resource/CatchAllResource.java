package com.necoutezpas.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.necoutezpas.dto.Request;
import org.joda.time.DateTime;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/")
public class CatchAllResource {
    private static final String REQUEST_EVENT = "REQUEST";

    @HEAD
    @Path("{seg:.*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response headLog(@Context UriInfo uriInfo, @Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest, String body)
            throws JsonProcessingException {
        Request request = Request.builder()
                .timestamp(DateTime.now().getMillis())
                .protocol(httpServletRequest.getProtocol())
                .isSecure(httpServletRequest.isSecure())
                .method(httpServletRequest.getMethod())
                .path(uriInfo.getPath())
                .query(uriInfo.getRequestUri().getQuery())
                .headers(headers.getRequestHeaders())
                .body(body)
                .build();
        SseWatchResource.emit(REQUEST_EVENT, request);
        return Response.ok().build();
    }

    @OPTIONS
    @Path("{seg:.*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response optionsLog(@Context UriInfo uriInfo, @Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest, String body)
            throws JsonProcessingException {
        Request request = Request.builder()
                .timestamp(DateTime.now().getMillis())
                .protocol(httpServletRequest.getProtocol())
                .isSecure(httpServletRequest.isSecure())
                .method(httpServletRequest.getMethod())
                .path(uriInfo.getPath())
                .query(uriInfo.getRequestUri().getQuery())
                .headers(headers.getRequestHeaders())
                .body(body)
                .build();
        SseWatchResource.emit(REQUEST_EVENT, request);
        return Response.ok().build();
    }

    @GET
    @Path("{seg:.*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLog(@Context UriInfo uriInfo, @Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest)
            throws JsonProcessingException {
        if ("favicon.ico".equals(uriInfo.getPath())) {
            return Response.noContent().build();
        }
        Request request = Request.builder()
                .timestamp(DateTime.now().getMillis())
                .protocol(httpServletRequest.getProtocol())
                .isSecure(httpServletRequest.isSecure())
                .method(httpServletRequest.getMethod())
                .path(uriInfo.getPath())
                .query(uriInfo.getRequestUri().getQuery())
                .headers(headers.getRequestHeaders())
                .build();
        SseWatchResource.emit(REQUEST_EVENT, request);
        return Response.ok().build();
    }

    @POST
    @Path("{seg:.*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postLog(@Context UriInfo uriInfo, @Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
                        String body)
            throws JsonProcessingException {
        Request request = Request.builder()
                .timestamp(DateTime.now().getMillis())
                .protocol(httpServletRequest.getProtocol())
                .isSecure(httpServletRequest.isSecure())
                .method(httpServletRequest.getMethod())
                .path(uriInfo.getPath())
                .query(uriInfo.getRequestUri().getQuery())
                .headers(headers.getRequestHeaders())
                .body(body)
                .build();
        SseWatchResource.emit(REQUEST_EVENT, request);
        return Response.ok().build();
    }


    @PUT
    @Path("{seg:.*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putLog(@Context UriInfo uriInfo, @Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
                           String body)
            throws JsonProcessingException {
        Request request = Request.builder()
                .timestamp(DateTime.now().getMillis())
                .protocol(httpServletRequest.getProtocol())
                .isSecure(httpServletRequest.isSecure())
                .method(httpServletRequest.getMethod())
                .path(uriInfo.getPath())
                .query(uriInfo.getRequestUri().getQuery())
                .headers(headers.getRequestHeaders())
                .body(body)
                .build();
        SseWatchResource.emit(REQUEST_EVENT, request);
        return Response.ok().build();
    }

    @DELETE
    @Path("{seg:.*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteLog(@Context UriInfo uriInfo, @Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
                            String body)
            throws JsonProcessingException {
        Request request = Request.builder()
                .timestamp(DateTime.now().getMillis())
                .protocol(httpServletRequest.getProtocol())
                .isSecure(httpServletRequest.isSecure())
                .method(httpServletRequest.getMethod())
                .path(uriInfo.getPath())
                .query(uriInfo.getRequestUri().getQuery())
                .headers(headers.getRequestHeaders())
                .body(body)
                .build();
        SseWatchResource.emit(REQUEST_EVENT, request);
        return Response.ok().build();
    }

}