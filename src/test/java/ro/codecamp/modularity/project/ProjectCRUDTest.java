package ro.codecamp.modularity.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import ro.codecamp.modularity.ServiceBaseTest;
import ro.codecamp.modularity.employee.EmployeeTestUtil;
import ro.codecamp.modularity.employee.control.EmployeeStore;
import ro.codecamp.modularity.infrastructure.control.SearchResults;
import ro.codecamp.modularity.project.control.ProjectStore;
import ro.codecamp.modularity.project.entity.Project;
import ro.codecamp.modularity.taxonomy.SkillTestUtil;

@RunWith(Arquillian.class)
public class ProjectCRUDTest extends ServiceBaseTest {

	@Inject
	private ProjectStore projectStore;

	@Inject
	private EmployeeStore empStore;

	@Inject
	private EmployeeTestUtil empTestUtil;
	
	@Inject
	private SkillTestUtil skillTestUtil;
	
	@Test
	@InSequence(1)
	public void create() {
		empTestUtil.createDeliveryUnits();
		skillTestUtil.createTaxonomy();
		empTestUtil.createEmployees();

		Project project = new Project("Project Test");
		project.getEmployees().addAll(empStore.search(1).getItems());

		Project storedEntity = projectStore.persist(project);
		assertNotNull(storedEntity.getId());

		Project foundEntity = projectStore.findById(Project.class,
				storedEntity.getId());
		assertNotNull(foundEntity);
		assertTrue(foundEntity.getEmployees().size() > 0);
	}

	@Test
	@InSequence(2)
	public void search() {
		SearchResults<Project> searchResults = projectStore.search();
		assertEquals(1, searchResults.getTotalItems());
		assertEquals(1, searchResults.getItems().size());
	}

	@Test
	@InSequence(3)
	public void update() {
		Project project = projectStore.search().getItems().get(0);
		String updatedValue = "Project Updated Name";
		project.setName(updatedValue);

		Project updatedEntity = projectStore.update(project);

		assertEquals(updatedValue, updatedEntity.getName());
	}

	@Test
	@InSequence(4)
	public void delete() {
		Project entity = projectStore.search().getItems().get(0);
		boolean deleted = projectStore.delete(Project.class, entity.getId());
		assertTrue(deleted);
	}
}
