package com.necoutezpas.resource;

import com.google.common.collect.ImmutableMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

@Path("/")
public class CatchAllResource {

    @GET
    @Path("{seg:.*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response log(@Context UriInfo uriInfo, @Context HttpHeaders headers, @Context HttpServletRequest request) {
        request.getProtocol();
        return Response.ok().entity(ImmutableMap.of(
            "protocol", request.getProtocol(),
            "method", request.getMethod(),
            "path", uriInfo.getPath(),
            "headers", headers.getRequestHeaders()
        )).build();
    }

}