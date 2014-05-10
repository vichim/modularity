package ro.codecamp.modularity.employee.contract;

import static org.junit.Assert.assertEquals;

import java.util.Date;

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
import ro.codecamp.modularity.infrastructure.DateUtil;
import ro.codecamp.modularity.infrastructure.rest.Constants;

import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.api.Representation;

@RunWith(Arquillian.class)
public class EmployeeContractTest extends RestBaseTest {

	private WebTarget empResource;

	@Before
	public void initResources() {
		empResource = client.target(BASE_URL + "/employees");
	}

	@BeforeClass
	public static void createTestData() {
		createDU();
		createSkills();
		createEmps();
	}

	@Test
	public void readEmployees() {
		Invocation.Builder builder = empResource
				.request(Constants.HAL_JSON_TYPE);
		Response response = builder.get();
		assertEquals(Constants.HAL_JSON_TYPE_STRING,
				response.getHeaderString("Content-Type"));
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

		ReadableRepresentation representation = response
				.readEntity(ReadableRepresentation.class);
		System.out.println(representation.getResources().size());
		System.out.println(representation
				.toString(Constants.HAL_JSON_TYPE_STRING));
	}

	@Test
	public void createEmp() {
		Representation representation = halFactory.newRepresentation()
				.withProperty("name", "Emp Test")
				.withProperty("startDate", DateUtil.formatJSDate(new Date()))
				.withProperty("du", "ISD");

		Representation skill = halFactory
				.newRepresentation()
				.withRepresentation("category",
						halFactory.newRepresentation("/skills/2"))
				.withProperty("level", "Proficient in");
		representation.withRepresentation("skills", skill);

		System.out.println("Sending new item: "
				+ representation.toString(Constants.HAL_JSON_TYPE_STRING));

		Invocation.Builder builder = empResource.request();
		Response response = builder.post(Entity.entity(representation,
				Constants.HAL_JSON_TYPE_STRING));

		assertEquals(Response.Status.CREATED.getStatusCode(),
				response.getStatus());
		System.out.println("Created item: "
				+ response.getHeaderString("Content-Location"));
	}

	@Test
	public void findById() {
		Invocation.Builder builder = empResource.path("1").request(
				Constants.HAL_JSON_TYPE_STRING);
		Response response = builder.get();
		ReadableRepresentation representation = response
				.readEntity(ReadableRepresentation.class);

		System.out.println(representation
				.toString(Constants.HAL_JSON_TYPE_STRING));
	}

	@Test
	public void updateEmp() {
		Representation representation = halFactory.newRepresentation()
				.withProperty("name", "Emp Updated")
				.withProperty("startDate", DateUtil.formatJSDate(new Date()))
				.withProperty("du", "ISD");
		System.out.println("Updating item: "
				+ representation.toString(Constants.HAL_JSON_TYPE_STRING));

		Invocation.Builder builder = empResource.path("1").request(
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
	public void deleteExistingEmp() {
		Invocation.Builder builder = empResource.path("/page/1").request(
				Constants.HAL_JSON_TYPE_STRING);
		Response response = builder.get();
		assertEquals(Constants.HAL_JSON_TYPE_STRING,
				response.getHeaderString("Content-Type"));

		ReadableRepresentation collection = response
				.readEntity(ReadableRepresentation.class);
		ReadableRepresentation firstItem = collection.getResources().iterator()
				.next().getValue();

		builder = root.path(firstItem.getResourceLink().getHref()).request();
		response = builder.delete();

		assertEquals(Response.Status.NO_CONTENT.getStatusCode(),
				response.getStatus());
	}

	@Test
	public void deleteInexistentEmp() {
		Invocation.Builder builder = empResource.path("999999").request(
				Constants.HAL_JSON_TYPE_STRING);
		Response response = builder.delete();
		ReadableRepresentation errorMessage = response
				.readEntity(ReadableRepresentation.class);

		assertEquals(Response.Status.NOT_FOUND.getStatusCode(),
				response.getStatus());
		System.out.println("Error received: "
				+ errorMessage.toString(Constants.HAL_JSON_TYPE_STRING));
	}

}
