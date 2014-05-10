package ro.codecamp.modularity.forecast;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import ro.codecamp.modularity.employee.EmployeeTestUtil;
import ro.codecamp.modularity.opportunity.OpportunityTestUtil;
import ro.codecamp.modularity.opportunity.entity.Opportunity;
import ro.codecamp.modularity.taxonomy.control.SkillStore;
import ro.codecamp.modularity.taxonomy.entity.SkillCategory;

@Path("/testdata/forecast")
public class ForecastTestUtil {

	@Inject
	private SkillStore skillStore;

	@Inject
	private EmployeeTestUtil empTestUtil;

	@Inject
	private OpportunityTestUtil oppTestUtil;

	@POST
	public String createForecastData() {
		Calendar calendar = Calendar.getInstance();
		Date present = calendar.getTime();
		calendar.add(Calendar.MONTH, 1);
		Date checkpoint1 = calendar.getTime();
		calendar.add(Calendar.MONTH, 1);
		Date checkpoint2 = calendar.getTime();
		calendar.add(Calendar.MONTH, 1);
		Date checkpoint3 = calendar.getTime();

		SkillCategory[] skills = { skillStore.findByName("JPA"),
				skillStore.findByName("EJB") };

		empTestUtil.createEmps(1, present, skills[0]);
		empTestUtil.createEmps(2, checkpoint1, skills[0]);
		empTestUtil.createEmps(2, checkpoint2, skills[0]);
		empTestUtil.createEmps(2, checkpoint2, skills[1]);
		empTestUtil.createEmps(2, checkpoint3, skills[1]);

		Opportunity opp = oppTestUtil.createOpps(1, checkpoint1, 4, skills)
				.get(0);
		return String.valueOf(opp.getId());
	}

}
