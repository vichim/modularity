package ro.codecamp.modularity.forecast;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import ro.codecamp.modularity.ServiceBaseTest;
import ro.codecamp.modularity.employee.EmployeeTestUtil;
import ro.codecamp.modularity.employee.entity.Employee;
import ro.codecamp.modularity.forecast.skillcoverage_api.Checkpoint;
import ro.codecamp.modularity.forecast.skillcoverage_api.SkillCoverage;
import ro.codecamp.modularity.opportunity.OpportunityTestUtil;
import ro.codecamp.modularity.opportunity.entity.Opportunity;
import ro.codecamp.modularity.project.ProjectTestUtil;
import ro.codecamp.modularity.taxonomy.SkillTestUtil;
import ro.codecamp.modularity.taxonomy.control.SkillStore;
import ro.codecamp.modularity.taxonomy.entity.SkillCategory;

@RunWith(Arquillian.class)
public class SkillCoverageDefaultImplTest extends ServiceBaseTest {

	@Inject
	private SkillTestUtil skillTestUtil;

	@Inject
	private EmployeeTestUtil empTestUtil;

	@Inject
	private OpportunityTestUtil oppTestUtil;

	@Inject
	private ProjectTestUtil projectTestUtil;

	@Inject
	private SkillStore skillStore;

	@Inject
	private ForecastService forecastService;

	private static final String FIRST_SKILL = "JPA";
	private static final String SECOND_SKILL = "EJB";

	@Test
	public void validateCheckpoints() {
		skillTestUtil.createTaxonomy();
		empTestUtil.createDeliveryUnits();

		Calendar calendar = Calendar.getInstance();
		Date present = calendar.getTime();
		calendar.add(Calendar.MONTH, 1);
		Date checkpoint1 = calendar.getTime();
		calendar.add(Calendar.MONTH, 1);
		Date checkpoint2 = calendar.getTime();
		calendar.add(Calendar.MONTH, 1);
		Date checkpoint3 = calendar.getTime();

		SkillCategory[] skills = { skillStore.findByName(FIRST_SKILL),
				skillStore.findByName(SECOND_SKILL) };

		empTestUtil.createEmps(1, present, skills[0]);
		List<Employee> allocatedEmps = empTestUtil.createEmps(2, present,
				skills[1]);
		empTestUtil.createEmps(2, checkpoint1, skills[0]);
		empTestUtil.createEmps(2, checkpoint2, skills[0]);
		empTestUtil.createEmps(2, checkpoint2, skills[1]);
		empTestUtil.createEmps(2, checkpoint3, skills[1]);

		projectTestUtil.createProject(allocatedEmps, checkpoint2);

		Opportunity opp = oppTestUtil.createOpps(1, checkpoint1, 4, skills)
				.get(0);

		List<Checkpoint> checkpoints = forecastService.computeCheckpoints(
				opp.getId(), "default");
		assertEquals(4, checkpoints.size());

		Map<SkillCategory, SkillCoverage> momentBefore = checkpoints.get(0)
				.getSkillCoverage();
		assertEquals("1/4", momentBefore.get(skills[0]).getCoverage());
		assertEquals("0/4", momentBefore.get(skills[1]).getCoverage());

		Map<SkillCategory, SkillCoverage> momentAfter1 = checkpoints.get(1)
				.getSkillCoverage();
		assertEquals("3/4", momentAfter1.get(skills[0]).getCoverage());
		assertEquals("0/4", momentAfter1.get(skills[1]).getCoverage());

		Map<SkillCategory, SkillCoverage> momentAfter2 = checkpoints.get(2)
				.getSkillCoverage();
		assertEquals("5/4", momentAfter2.get(skills[0]).getCoverage());
		assertEquals("2/4", momentAfter2.get(skills[1]).getCoverage());

		Map<SkillCategory, SkillCoverage> momentAfter3 = checkpoints.get(3)
				.getSkillCoverage();
		assertEquals("5/4", momentAfter3.get(skills[0]).getCoverage());
		assertEquals("4/4", momentAfter3.get(skills[1]).getCoverage());
	}

}
