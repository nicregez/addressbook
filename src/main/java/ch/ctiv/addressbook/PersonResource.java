package ch.ctiv.addressbook;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import io.swagger.annotations.Api;

/**
 * @author Nicolas Regez
 * @since 23 Jan 2018
 */
@Path("/person")
@Api(value = "Person")
@Component
public class PersonResource {

	private PersonRepository repository;

	@Inject
	public PersonResource(PersonRepository repository) {
		this.repository = repository;
	}

	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<Person> list() {

		return Collections.unmodifiableList(repository.findAll());
	}

	@Path("/{id}")
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Person read(@PathParam("id") String id) {

		return repository.findOne(id);
	}

	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Person create(Person input) {

		input.setId(UUID.randomUUID().toString());
		return repository.save(input);
	}

	@Path("/{id}")
	@PUT
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Person update(@PathParam("id") String id, Person input) {

		input.setId(id);
		return repository.save(input);
	}

	@Path("/{id}")
	@DELETE
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public void delete(@PathParam("id") String id) {

		repository.delete(id);
	}

}
