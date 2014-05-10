package ro.codecamp.modularity.forecast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import ro.codecamp.modularity.employee.control.EmployeeStore;
import ro.codecamp.modularity.employee.entity.Employee;
import ro.codecamp.modularity.opportunity.control.OpportunityStore;
import ro.codecamp.modularity.taxonomy.entity.SkillCategory;

@Stateless
public class DistributionService {

	@Inject
	private EmployeeStore empStore;

	@Inject
	private OpportunityStore oppStore;

	/**
	 * @return map of (du, no. of employees)
	 */
	public Map<String, Integer> computeDistribution(SkillCategory skill) {
		Map<String, Integer> totals = new HashMap<String, Integer>();
		List<Employee> empList = empStore.findEmpsBySkill(skill);
		for (Employee emp : empList) {
			String du = emp.getDeliveryUnit().getCode();
			if (!totals.containsKey(du)) {
				totals.put(du, 1);
			} else {
				totals.put(du, totals.get(du) + 1);
			}
		}

		return totals;
	}

	public Integer computeAvailableMinusRequired(SkillCategory skill) {
		return empStore.countEmpsBySkill(skill)
				- oppStore.getTotalHeadCountBySkill(skill);
	}
}
