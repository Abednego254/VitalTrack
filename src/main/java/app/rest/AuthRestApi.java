package app.rest;

import app.ejb.UserEJB;
import app.model.User;
import app.utility.JwtUtility;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;

@Path("/auth")
public class AuthRestApi {

    @EJB
    private UserEJB userEJB;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest request) {
        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Email and password are required\"}")
                .build();
        }

        User user = userEJB.authenticate(request.getEmail(), request.getPassword());
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\":\"Invalid credentials\"}")
                .build();
        }

        String token = JwtUtility.generateToken(user.getEmail(), user.getName(), user.getRole());
        return Response.ok("{\"token\":\"" + token + "\",\"role\":\"" + user.getRole() + "\",\"name\":\"" + user.getName() + "\"}").build();
    }

    public static class LoginRequest implements Serializable {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
