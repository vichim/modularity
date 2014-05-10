package ro.codecamp.modularity;

import java.io.File;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public class Deployments {

	public static WebArchive domain() {
		WebArchive archive = base();

		// generateArchive(archive);

		return archive;
	}

	public static WebArchive rest() {
		WebArchive archive = base();
		archive.setWebXML(new File("src/main/webapp", "WEB-INF/web.xml"));

		// generateArchive(archive);

		return archive;
	}

	public static WebArchive base() {
		WebArchive archive = ShrinkWrap.create(WebArchive.class,
				"modularity.war").addAsResource(
				"META-INF/persistence-test.xml", "META-INF/persistence.xml");
		archive.addPackages(true, "ro");
		
		archive.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
		archive.addAsWebInfResource("arquillian-ds.xml");

		File[] libs = Maven.resolver().loadPomFromFile("pom.xml")
				.importRuntimeDependencies().resolve().withTransitivity()
				.asFile();
		archive.addAsLibraries(libs);
		
		return archive;
	}

	public static void generateArchive(WebArchive archive) {
		System.out.println(archive.toString(true));
		archive.as(ZipExporter.class).exportTo(new File("arq_mod.war"), true);
	}
}
