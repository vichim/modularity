package ro.codecamp.modularity;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.BeforeClass;

import ro.codecamp.modularity.infrastructure.rest.HalReaderAndWriter;

import com.theoryinpractise.halbuilder.json.JsonRepresentationFactory;

public abstract class RestBaseTest {

	public static final String BASE_URL = "http://localhost:8080/modularity";
	protected JsonRepresentationFactory halFactory = new JsonRepresentationFactory();

	protected static WebTarget root;
	protected static Client client;
	private static WebTarget testDataResource;

	@Deployment(testable = false)
	public static Archive<?> createDeployment() {
		return Deployments.rest();
	}

	@BeforeClass
	public static void initClient() {
		client = ClientBuilder.newClient();
		client.register(HalReaderAndWriter.class);
		root = client.target(BASE_URL);

		testDataResource = client.target(BASE_URL + "/testdata");
	}

	protected static void createDU() {
		testDataResource.path("/employees/du").request().post(null);
	}

	protected static void createEmps() {
		testDataResource.path("/employees/emp").request().post(null);
	}

	protected static void createOpportunities() {
		testDataResource.path("/opportunities").request().post(null);
	}

	protected static void createProjects() {
		testDataResource.path("/projects").request().post(null);
	}

	protected static void createSkills() {
		testDataResource.path("/skills").request().post(null);
	}

	protected static String createForecastData() {
		Response response = testDataResource.path("/forecast").request()
				.post(null);
		return response.readEntity(String.class);
	}

}
