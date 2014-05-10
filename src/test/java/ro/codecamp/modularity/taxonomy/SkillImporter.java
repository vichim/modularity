package ro.codecamp.modularity.taxonomy;

import java.io.File;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import ro.codecamp.modularity.infrastructure.rest.LinkUtil;
import ro.codecamp.modularity.taxonomy.entity.SkillCategory;

import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.json.JsonRepresentationFactory;

@RunWith(Arquillian.class)
public class SkillImporter {

	@Deployment(testable = true)
	public static Archive<?> createDeployment() {
		WebArchive archive = ShrinkWrap.create(WebArchive.class,
				"modularity.war").addPackages(true, "ro");
		archive.addAsResource("taxonomy.json");
		archive.addAsResource("META-INF/persistence.xml",
				"META-INF/persistence.xml");
		archive.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
		archive.setWebXML(new File("src/main/webapp", "WEB-INF/web.xml"));
		File[] libs = Maven.resolver().loadPomFromFile("pom.xml")
				.importRuntimeDependencies().resolve().withTransitivity()
				.asFile();
		archive.addAsLibraries(libs);

		return archive;
	}

	protected JsonRepresentationFactory halFactory = new JsonRepresentationFactory();

	@Resource(mappedName = "java:jboss/datasources/modularity")
	private DataSource dataSource;

	@Test
	public void insertTaxonomyTree() {
		ReadableRepresentation representation = halFactory
				.readRepresentation(new InputStreamReader(Thread
						.currentThread().getContextClassLoader()
						.getResourceAsStream("taxonomy.json")));

		insertBulk(createEntityTree(representation, null));
	}

	public SkillCategory createEntityTree(
			ReadableRepresentation representation, SkillCategory parent) {
		SkillCategory skill = createEntity(representation, parent);

		List<? extends ReadableRepresentation> skillReps = representation
				.getResourcesByRel("children");
		for (ReadableRepresentation bean : skillReps) {
			skill.getSubCategories().add(createEntityTree(bean, skill));
		}

		return skill;
	}

	private SkillCategory createEntity(ReadableRepresentation representation,
			SkillCategory parent) {
		Map<String, Object> props = representation.getProperties();
		String name = (String) props.get("name");
		Long id = LinkUtil.getIdFromLink(representation.getResourceLink()
				.getHref());

		return new SkillCategory(id, name, parent);
	}

	private void insertBulk(SkillCategory skillCategory) {
		try {
			Connection connection = dataSource.getConnection();
			insertRecursive(connection, skillCategory);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private void insertRecursive(Connection connection, SkillCategory skill)
			throws Exception {
		insertSkillWithJDBC(connection, skill);
		for (SkillCategory bean : skill.getSubCategories()) {
			insertRecursive(connection, bean);
		}
	}

	private void insertSkillWithJDBC(Connection connection, SkillCategory skill)
			throws Exception {
		String sql = "insert into SkillCategory values (?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, skill.getId());
		statement.setString(2, skill.getName());
		if (skill.getParent() != null) {
			statement.setLong(3, skill.getParent().getId());
		} else {
			statement.setNull(3, Types.BIGINT);
		}
		statement.executeUpdate();
	}

}
