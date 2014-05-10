package ro.codecamp.modularity.taxonomy;

import static org.junit.Assert.assertEquals;

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

@RunWith(Arquillian.class)
public class SkillContractTest extends RestBaseTest {

	private WebTarget skillResource;

	@Before
	public void initResources() {
		skillResource = client.target(BASE_URL + "/skills");
	}
	
	@BeforeClass
	public static void createTestData() {
		createSkills();
	}

	@Test
	public void getTaxonomy() {
		Invocation.Builder builder = skillResource
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
	public void findById() {
		Invocation.Builder builder = skillResource.path("3").request(
				Constants.HAL_JSON_TYPE_STRING);
		Response response = builder.get();
		ReadableRepresentation representation = response
				.readEntity(ReadableRepresentation.class);

		System.out.println(representation
				.toString(Constants.HAL_JSON_TYPE_STRING));
	}

}
