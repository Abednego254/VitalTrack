package app.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

public abstract class GenericApi<T> {

    protected abstract void saveEntity(T entity) throws Exception;
    protected abstract T findEntity(Long id) throws Exception;
    protected abstract List<T> listEntities() throws Exception;
    protected abstract void deleteEntity(Long id) throws Exception;

    @Path("/save")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(T entity) {
        try {
            saveEntity(entity);
            return Response.ok(new ResponseStatus(SuccessError.SUCCESS, "Record saved successfully")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ResponseStatus(SuccessError.ERROR, e.getMessage())).build();
        }
    }

    @Path("/find/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") Long id) {
        try {
            T entity = findEntity(id);
            if (entity == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ResponseStatus(SuccessError.ERROR, "Record not found")).build();
            }
            return Response.ok(entity).build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity(new ResponseStatus(SuccessError.ERROR, e.getMessage())).build();
        }
    }

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list() {
        try {
            List<T> list = listEntities();
            return Response.ok(list).build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity(new ResponseStatus(SuccessError.ERROR, e.getMessage())).build();
        }
    }

    @Path("/delete/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id) {
        try {
            deleteEntity(id);
            return Response.ok(new ResponseStatus(SuccessError.SUCCESS, "Record deleted successfully")).build();
        } catch (Exception e) {
            return Response.serverError()
                    .entity(new ResponseStatus(SuccessError.ERROR, e.getMessage())).build();
        }
    }
}
