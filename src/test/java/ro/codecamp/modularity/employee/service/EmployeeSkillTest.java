package ro.codecamp.modularity.employee.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import ro.codecamp.modularity.ServiceBaseTest;
import ro.codecamp.modularity.employee.EmployeeTestUtil;
import ro.codecamp.modularity.employee.control.EmployeeStore;
import ro.codecamp.modularity.taxonomy.SkillTestUtil;
import ro.codecamp.modularity.taxonomy.control.SkillStore;
import ro.codecamp.modularity.taxonomy.entity.SkillCategory;

@RunWith(Arquillian.class)
public class EmployeeSkillTest extends ServiceBaseTest {

	@Inject
	private EmployeeStore empStore;

	@Inject
	private SkillStore skillStore;

	@Inject
	private EmployeeTestUtil empTestUtil;

	@Inject
	private SkillTestUtil skillTestUtil;
	
	private static final String FIRST_SKILL = "JPA";
	private static final String SECOND_SKILL = "EJB";

	@Test
	public void findEmpBySkill() {
		skillTestUtil.createTaxonomy();
		empTestUtil.createDeliveryUnits();

		int expectedNoEmps = 5;

		SkillCategory[] skills = { skillStore.findByName(FIRST_SKILL),
				skillStore.findByName(SECOND_SKILL) };
		empTestUtil.createEmps(expectedNoEmps, new Date(), skills);

		int actualNoEmps = empStore.countEmpsBySkill(skills[0]);

		assertEquals(expectedNoEmps, actualNoEmps);
	}

}
