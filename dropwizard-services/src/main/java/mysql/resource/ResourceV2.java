package mysql.resource;


import mysql.entity.ResponseMessage;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/arts")
public class ResourceV2 {

    @GET
    @Path("/hello")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllEmployees()  {
        return Response.ok( new ResponseMessage("ARTS SAYS HELLO !!!")).build();

    }

}
