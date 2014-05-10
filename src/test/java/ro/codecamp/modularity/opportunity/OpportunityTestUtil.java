package ro.codecamp.modularity.opportunity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import ro.codecamp.modularity.infrastructure.DateUtil;
import ro.codecamp.modularity.opportunity.control.OpportunityStore;
import ro.codecamp.modularity.opportunity.entity.Opportunity;
import ro.codecamp.modularity.opportunity.entity.SkillRequirement;
import ro.codecamp.modularity.taxonomy.control.SkillStore;
import ro.codecamp.modularity.taxonomy.entity.SkillCategory;
import ro.codecamp.modularity.taxonomy.entity.SkillLevel;

@Path("/testdata/opportunities")
public class OpportunityTestUtil {

	@Inject
	private OpportunityStore oppStore;

	@Inject
	private SkillStore skillStore;

	public List<Opportunity> createOpps(int noOpps, Date startDate,
			int headCountPerSkill, SkillCategory... skills) {
		List<Opportunity> results = new ArrayList<>();
		for (int i = 0; i < noOpps; i++) {
			Opportunity opportunity = new Opportunity("Opportunity "
					+ DateUtil.formatUKDate(startDate), startDate);
			for (SkillCategory skill : skills) {
				opportunity.getSkills().add(
						new SkillRequirement(opportunity, skill,
								SkillLevel.FamiliarWith, headCountPerSkill));
			}
			results.add(oppStore.persist(opportunity));
		}
		return results;
	}

	@POST
	public void createOpportunities() {
		SkillCategory angular = skillStore.findByName("AngularJS");
		SkillCategory require = skillStore.findByName("RequireJS");
		SkillCategory jpa = skillStore.findByName("JPA");
		SkillCategory ejb = skillStore.findByName("EJB");

		Calendar calendar = Calendar.getInstance();
		Date present = calendar.getTime();
		calendar.add(Calendar.MONTH, 1);
		Date future = calendar.getTime();

		createOpps(1, present, 4, angular, require);
		createOpps(1, future, 4, angular, require, jpa, ejb);
	}

}
