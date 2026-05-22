package app.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

import app.model.BaseEntity;

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


    @Path("/update/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, T entity) {
    try {
        // 1. Double check that the record exists first
        T existing = findEntity(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ResponseStatus(SuccessError.ERROR, "Record not found")).build();
        }
        
        // 2. Ensure we are updating the correct record by setting the ID
        // (Typically, our base entities have an id field, we can cast or call a setter)
        if (entity instanceof BaseEntity) {
            ((BaseEntity) entity).setId(id);
        }
        // 3. Delegate to save/update method
        saveEntity(entity);
        
        return Response.ok(new ResponseStatus(SuccessError.SUCCESS, "Record updated successfully")).build();
    } catch (Exception e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ResponseStatus(SuccessError.ERROR, e.getMessage())).build();
    }
}
}