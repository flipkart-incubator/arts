package mysql.resource;

import mysql.dao.MySqlDao;
import mysql.entity.Employee;
import mysql.entity.ResponseMessage;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/employee")
public class MySqlResource {

    private MySqlDao mySqlDao;
    public MySqlResource(MySqlDao myMySqlDao){
        this.mySqlDao = myMySqlDao;
    }

    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllEmployees()  {
        return Response.ok( mySqlDao.getEmployeeList()).build();

    }


    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response addEmployee(Employee employee){
        mySqlDao.addNewEmp(employee);
        return Response.ok(employee).build();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getEmployeeById(@PathParam("id") String id){
        Employee employee = mySqlDao.getEmpById(id);
        if(employee!=null)
            return Response.ok(employee).build();
        else
            return Response.ok(new Error("Employee with id "+id+ " does not exist")).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response updateEmployee(@PathParam("id") String id, Employee employee){
        if(id.equalsIgnoreCase(employee.getId())){
            mySqlDao.updateEmp(id,employee.getName(),employee.getDepartment());
            return Response.ok(employee).build();
        }
        else return Response.serverError().entity(new Error("id in the path and payload must match")).build();

    }

    @DELETE
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response deleteEmployee(@PathParam("id") String id){
        mySqlDao.deleteEmp(id);
        Response response= Response.ok(new ResponseMessage("Deleted")).build();
        return response;
    }
}
