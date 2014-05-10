package ro.codecamp.modularity.project;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import ro.codecamp.modularity.RestBaseTest;
import ro.codecamp.modularity.infrastructure.rest.Constants;

import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.api.Representation;

@RunWith(Arquillian.class)
public class ProjectContractTest extends RestBaseTest {

	private WebTarget projectsResource;

	@Before
	public void initResources() {
		projectsResource = client.target(BASE_URL + "/projects");
	}

	@BeforeClass
	public static void createTestData() {
		createDU();
		createSkills();
		createEmps();
		createProjects();
	}
	
	@Test
	public void findProjects() {
		Invocation.Builder builder = projectsResource
				.request(Constants.HAL_JSON_TYPE);
		Response response = builder.get();
		assertEquals(Constants.HAL_JSON_TYPE_STRING,
				response.getHeaderString("Content-Type"));

		ReadableRepresentation representation = response
				.readEntity(ReadableRepresentation.class);
		System.out.println(representation
				.toString(Constants.HAL_JSON_TYPE_STRING));
	}

	@Test
	public void createProject() {
		Representation representation = halFactory.newRepresentation()
				.withProperty("name", "Project A");

		representation.withRepresentation("employees",
				halFactory.newRepresentation("/employees/1"));
		representation.withRepresentation("employees",
				halFactory.newRepresentation("/employees/2"));

		System.out.println("Creating new item: "
				+ representation.toString(Constants.HAL_JSON_TYPE_STRING));

		Invocation.Builder builder = projectsResource.request();
		Response response = builder.post(Entity.entity(representation,
				Constants.HAL_JSON_TYPE_STRING));

		assertEquals(Response.Status.CREATED.getStatusCode(),
				response.getStatus());
		System.out.println("Created item: "
				+ response.getHeaderString("Content-Location"));
	}

	@Test
	public void findById() {
		Invocation.Builder builder = projectsResource.path("1").request(
				Constants.HAL_JSON_TYPE_STRING);
		Response response = builder.get();
		ReadableRepresentation representation = response
				.readEntity(ReadableRepresentation.class);

		System.out.println(representation
				.toString(Constants.HAL_JSON_TYPE_STRING));
	}

	@Test
	public void updateProject() {
		Representation representation = halFactory.newRepresentation()
				.withProperty("name", "Project A updated");

		System.out.println("Updating item: "
				+ representation.toString(Constants.HAL_JSON_TYPE_STRING));

		Invocation.Builder builder = projectsResource.path("1").request(
				Constants.HAL_JSON_TYPE_STRING);
		Response response = builder.put(Entity.entity(representation,
				Constants.HAL_JSON_TYPE_STRING));
		ReadableRepresentation updatedItem = response
				.readEntity(ReadableRepresentation.class);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		System.out.println("Updated item: "
				+ updatedItem.toString(Constants.HAL_JSON_TYPE_STRING));

	}

	@Test
	public void deleteProject() {
		Invocation.Builder builder = projectsResource.path("1").request();
		Response response = builder.delete();

		assertEquals(Response.Status.NO_CONTENT.getStatusCode(),
				response.getStatus());
	}

}
