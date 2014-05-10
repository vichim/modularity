package ro.codecamp.modularity.taxonomy;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import ro.codecamp.modularity.ServiceBaseTest;
import ro.codecamp.modularity.taxonomy.control.SkillStore;
import ro.codecamp.modularity.taxonomy.entity.SkillCategory;

@RunWith(Arquillian.class)
public class SkillServiceTest extends ServiceBaseTest {

	@Inject
	private SkillStore skillStore;

	private static final String ROOT = "Taxonomy";

	@Test
	@InSequence(1)
	public void create() {
		SkillCategory root = new SkillCategory(ROOT, null);
		root.getSubCategories().add(new SkillCategory("Development", root));

		skillStore.persist(root);

		root = skillStore.findRoot();
		assertNotNull(root);
		assertTrue(root.getSubCategories().size() > 0);
	}

	@Test
	@InSequence(2)
	public void findByName() {
		SkillCategory root = skillStore.findByName(ROOT);
		assertNotNull(root);
	}

}
