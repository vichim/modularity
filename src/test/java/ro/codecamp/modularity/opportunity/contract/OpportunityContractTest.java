package ro.codecamp.modularity.opportunity.contract;

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
public class OpportunityContractTest extends RestBaseTest {

	private WebTarget oppResource;

	@Before
	public void initResources() {
		oppResource = client.target(BASE_URL + "/opportunities");
	}
	
	@BeforeClass
	public static void createTestData() {
		createSkills();
		createOpportunities();
	}

	@Test
	public void findOpportunities() {
		Invocation.Builder builder = oppResource
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
	public void createOpp() {
		Representation representation = halFactory.newRepresentation()
				.withProperty("name", "Opportunity Z")
				.withProperty("startDate", DateUtil.formatJSDate(new Date()));

		Representation skill = halFactory
				.newRepresentation()
				.withRepresentation("category",
						halFactory.newRepresentation("/skills/2"))
				.withProperty("level", "Proficient in")
				.withProperty("headCount", 5);
		representation.withRepresentation("skills", skill);

		skill = halFactory
				.newRepresentation()
				.withRepresentation("category",
						halFactory.newRepresentation("/skills/3"))
				.withProperty("level", "Aware of").withProperty("headCount", 1);
		representation.withRepresentation("skills", skill);

		System.out.println("Creating new item: "
				+ representation.toString(Constants.HAL_JSON_TYPE_STRING));

		Invocation.Builder builder = oppResource.request();
		Response response = builder.post(Entity.entity(representation,
				Constants.HAL_JSON_TYPE_STRING));

		assertEquals(Response.Status.CREATED.getStatusCode(),
				response.getStatus());
		System.out.println("Created item: "
				+ response.getHeaderString("Content-Location"));
	}

	@Test
	public void findById() {
		Invocation.Builder builder = oppResource.path("1").request(
				Constants.HAL_JSON_TYPE_STRING);
		Response response = builder.get();
		ReadableRepresentation representation = response
				.readEntity(ReadableRepresentation.class);

		System.out.println(representation
				.toString(Constants.HAL_JSON_TYPE_STRING));
	}

	@Test
	public void updateOpp() {
		Representation representation = halFactory.newRepresentation()
				.withProperty("name", "Opportunity X updated")
				.withProperty("startDate", DateUtil.formatJSDate(new Date()));

		Representation skill = halFactory
				.newRepresentation()
				.withRepresentation("category",
						halFactory.newRepresentation("/skills/2"))
				.withProperty("level", "Proficient in")
				.withProperty("headCount", 5);
		representation.withRepresentation("skills", skill);

		System.out.println("Updating item: "
				+ representation.toString(Constants.HAL_JSON_TYPE_STRING));

		Invocation.Builder builder = oppResource.path("1").request(
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
	public void deleteOpp() {
		Invocation.Builder builder = oppResource.path("2").request();
		Response response = builder.delete();

		assertEquals(Response.Status.NO_CONTENT.getStatusCode(),
				response.getStatus());
	}

}
