package coop.magnesium.sulfur.api;


import coop.magnesium.sulfur.db.entities.SulfurUser;
import coop.magnesium.sulfur.utils.KeyGenerator;
import coop.magnesium.sulfur.utils.PasswordUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Logger;


import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

/**
 * Created by rsperoni on 05/05/17.
 */
@Path("/users")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
public class UserService {

    @Inject
    private KeyGenerator keyGenerator;
    @Context
    private UriInfo uriInfo;
    @Inject
    private Logger logger;

    @POST
    @Path("/login")
    @Consumes(APPLICATION_FORM_URLENCODED)
    public Response authenticateUser(@FormParam("email") String email,
                                     @FormParam("password") String password) {
        try {

            // Authenticate the sulfurUser using the credentials provided
            SulfurUser sulfurUser = authenticate(email, password);

            //Info que quiero guardar en token
            Map<String, Object> map = new HashMap<>();
            map.put("role", sulfurUser.getRole());

            // Issue a token for the sulfurUser
            String token = issueToken(email, map);

            // Return the token on the response
            String json = "{\"token\":" + "\"Bearer " + token + "\"}";
            //return Response.ok(json).build();
            return Response.ok(json).header(AUTHORIZATION, "Bearer " + token).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(UNAUTHORIZED).build();
        }
    }

    @POST
    public Response create(SulfurUser sulfurUser) {
        sulfurUser.setPassword(PasswordUtils.digestPassword(sulfurUser.getPassword()));
        //TODO:insert sulfurUser
        return Response.created(uriInfo.getAbsolutePathBuilder().path(sulfurUser.getEmail()).build()).build();
    }


    @GET
    public Response findAllUsers() {
        List<SulfurUser> allSulfurUsers = new ArrayList<>(); //TODO: fetch users
        if (allSulfurUsers == null)
            return Response.status(NOT_FOUND).build();
        return Response.ok(allSulfurUsers).build();
    }


    private SulfurUser authenticate(String email, String password) throws Exception {
        SulfurUser sulfurUser = null; //TODO: find sulfurUser

        if (sulfurUser == null)
            throw new SecurityException("Invalid sulfurUser/password");

        return sulfurUser;
    }

    private String issueToken(String login, Map<String, Object> claims) {
        Key key = keyGenerator.generateKey();
        String jwtToken = Jwts.builder()
                .setSubject(login)
                .setClaims(claims)
                .setIssuer(uriInfo.getAbsolutePath().toString())
                .setIssuedAt(new Date())
                .setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        return jwtToken;
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
