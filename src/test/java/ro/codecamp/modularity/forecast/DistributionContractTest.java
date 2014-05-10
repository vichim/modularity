package ro.codecamp.modularity.forecast;

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
public class DistributionContractTest extends RestBaseTest {

	private WebTarget distResource;

	@Before
	public void initResources() {
		distResource = client.target(BASE_URL + "/forecast/distribution");
	}

	@BeforeClass
	public static void createTestData() {
		createSkills();
		createDU();
		createEmps();
	}

	@Test
	public void findDistribution() {
		Invocation.Builder builder = distResource
				.request(Constants.HAL_JSON_TYPE_STRING);
		Response response = builder.get();
		ReadableRepresentation representation = response
				.readEntity(ReadableRepresentation.class);

		System.out.println(representation
				.toString(Constants.HAL_JSON_TYPE_STRING));
	}

}
