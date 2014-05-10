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
public class ForecastContractTest extends RestBaseTest {

	private WebTarget forecastResource;

	@Before
	public void initResources() {
		forecastResource = client.target(BASE_URL + "/forecast");
	}

	private static String oppId;

	@BeforeClass
	public static void createTestData() {
		createSkills();
		createDU();
		oppId = createForecastData();
	}

	@Test
	public void readAvailableAlgorithms() {
		Invocation.Builder builder = forecastResource.path(
				"/checkpoints/algorithms").request(Constants.HAL_JSON_TYPE);
		Response response = builder.get();

		ReadableRepresentation representation = response
				.readEntity(ReadableRepresentation.class);
		System.out.println(representation
				.toString(Constants.HAL_JSON_TYPE_STRING));
	}

	@Test
	public void forecastDefaultAlg() {
		Invocation.Builder builder = forecastResource.path(
				"/checkpoints/" + oppId).request(Constants.HAL_JSON_TYPE);
		Response response = builder.get();

		ReadableRepresentation representation = response
				.readEntity(ReadableRepresentation.class);
		System.out.println(representation
				.toString(Constants.HAL_JSON_TYPE_STRING));
	}

	@Test
	public void forecastEnhancedAlg() {
		Invocation.Builder builder = forecastResource.path(
				"/checkpoints/enhanced/" + oppId).request(
				Constants.HAL_JSON_TYPE);
		Response response = builder.get();

		ReadableRepresentation representation = response
				.readEntity(ReadableRepresentation.class);
		System.out.println(representation
				.toString(Constants.HAL_JSON_TYPE_STRING));
	}

}
