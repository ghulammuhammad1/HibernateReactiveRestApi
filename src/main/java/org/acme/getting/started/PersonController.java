package org.acme.getting.started;

import java.net.URI;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;

@Path("/person")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PersonController {

    @POST
    public Uni<Response> create(Person person) {
        return Panache.withTransaction(person::persist)
                .replaceWith(Response.ok(person).status(201)::build);
    }

    @GET
    public Uni<List<Person>> getAll(){
        return Person.listAll();
    }

    @POST
    @Path("/search/{id}")
    public Uni<Person> findById(@PathParam("id") Long id){
        return Person.findById(id);
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> delete(@PathParam("id") Long id) {
         return Panache.withTransaction(()->Person.deleteById(id))
                .map(delete-> delete?Response.ok().status(201).build()
                : Response.ok().status(404).build()
                );
         
    }

   

    @PUT
    @Path("/{id}")
    public Uni<Response> update(@PathParam("id") Long id, Person person) {
        if (person == null)
            throw new WebApplicationException("Details were not updated on request", 422);
        return Panache.withTransaction( () -> Person.<Person>findById(id).
                        onItem().
                        ifNotNull().
                        invoke(
                            (p) -> {
                                p.setName(person.getName());
                                p.setEmail(person.getEmail());
                            }
                            )
                )
                .onItem().ifNotNull().
                transform(car -> Response.ok(car).build())
                .onItem().ifNull()
                .continueWith(Response.ok().status(404)::build);

    }
}