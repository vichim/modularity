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
public class SkillCoverageEnhancedImplTest extends ServiceBaseTest {

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
		calendar.add(Calendar.MONTH, 1);
		Date checkpoint4 = calendar.getTime();

		SkillCategory[] skills = { skillStore.findByName(FIRST_SKILL),
				skillStore.findByName(SECOND_SKILL) };

		List<Employee> allocatedEmpsProj1 = empTestUtil.createEmps(1, present,
				skills[0]);
		List<Employee> allocatedEmpsProj2 = empTestUtil.createEmps(2, present,
				skills[1]);
		List<Employee> allocatedEmpsProj3 = empTestUtil.createEmps(2,
				checkpoint4, skills[0]);
		empTestUtil.createEmps(2, checkpoint1, skills[0]);
		empTestUtil.createEmps(2, checkpoint2, skills[0]);
		empTestUtil.createEmps(2, checkpoint2, skills[1]);
		empTestUtil.createEmps(2, checkpoint3, skills[1]);

		projectTestUtil.createProject(allocatedEmpsProj1, present);
		projectTestUtil.createProject(allocatedEmpsProj2, checkpoint2);
		projectTestUtil.createProject(allocatedEmpsProj3, checkpoint4);

		Opportunity opp = oppTestUtil.createOpps(1, checkpoint1, 4, skills)
				.get(0);

		List<Checkpoint> checkpoints = forecastService.computeCheckpoints(
				opp.getId(), "enhanced");
		assertEquals(5, checkpoints.size());

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
		assertEquals("4/4", momentAfter2.get(skills[1]).getCoverage());

		Map<SkillCategory, SkillCoverage> momentAfter3 = checkpoints.get(3)
				.getSkillCoverage();
		assertEquals("5/4", momentAfter3.get(skills[0]).getCoverage());
		assertEquals("6/4", momentAfter3.get(skills[1]).getCoverage());

		Map<SkillCategory, SkillCoverage> momentAfter4 = checkpoints.get(4)
				.getSkillCoverage();
		assertEquals("7/4", momentAfter4.get(skills[0]).getCoverage());
		assertEquals("6/4", momentAfter4.get(skills[1]).getCoverage());
	}

}
