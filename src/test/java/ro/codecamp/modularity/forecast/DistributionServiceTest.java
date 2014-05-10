package ro.codecamp.modularity.forecast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Map;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import ro.codecamp.modularity.ServiceBaseTest;
import ro.codecamp.modularity.employee.EmployeeTestUtil;
import ro.codecamp.modularity.employee.entity.Employee;
import ro.codecamp.modularity.taxonomy.SkillTestUtil;
import ro.codecamp.modularity.taxonomy.control.SkillStore;
import ro.codecamp.modularity.taxonomy.entity.SkillCategory;

@RunWith(Arquillian.class)
public class DistributionServiceTest extends ServiceBaseTest {

	@Inject
	private DistributionService distributionService;

	@Inject
	private SkillStore skillStore;
	
	@Inject
	private SkillTestUtil skillTestUtil;
	
	@Inject
	private EmployeeTestUtil empTestUtil;

	private static final String SKILL_NAME = "JPA";

	@Test
	public void distribution() {
		empTestUtil.createDeliveryUnits();
		skillTestUtil.createTaxonomy();

		SkillCategory skill = skillStore.findByName(SKILL_NAME);
		Employee storedEmp = empTestUtil.createEmps(1, new Date(), skill)
				.get(0);
		String duCode = storedEmp.getDeliveryUnit().getCode();

		Map<String, Integer> distribution = distributionService
				.computeDistribution(skillStore.findByName(SKILL_NAME));
		assertEquals(1, distribution.size());
		assertTrue(distribution.containsKey(duCode));
		assertEquals(new Integer(1), distribution.get(duCode));
	}

}
