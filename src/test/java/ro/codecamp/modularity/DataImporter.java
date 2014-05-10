package ro.codecamp.modularity;

import java.io.File;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import ro.codecamp.modularity.infrastructure.rest.HalReaderAndWriter;

@RunWith(Arquillian.class)
public class DataImporter {

	@Deployment(testable = false)
	public static Archive<?> createDeployment() {
		WebArchive archive = ShrinkWrap.create(WebArchive.class,
				"modularity.war").addPackages(true, "ro");
		archive.addAsResource("META-INF/persistence.xml",
				"META-INF/persistence.xml");
		archive.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
		archive.setWebXML(new File("src/main/webapp", "WEB-INF/web.xml"));
		File[] libs = Maven.resolver().loadPomFromFile("pom.xml")
				.importRuntimeDependencies().resolve().withTransitivity()
				.asFile();
		archive.addAsLibraries(libs);

		//archive.as(ZipExporter.class)
			//	.exportTo(new File("modularity.war"), true);

		return archive;
	}

	private static WebTarget testDataResource;

	@BeforeClass
	public static void initClient() {
		Client client = ClientBuilder.newClient();
		client.register(HalReaderAndWriter.class);
		testDataResource = client.target(RestBaseTest.BASE_URL + "/testdata");
	}

	@Test
	public void insertTestData() {
		testDataResource.path("/employees/du").request().post(null);
		testDataResource.path("/skills").request().post(null);
		testDataResource.path("/opportunities").request().post(null);
		testDataResource.path("/projects").request().post(null);
	}

}
