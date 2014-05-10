package ro.codecamp.modularity.forecast.skillcoverage_api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ro.codecamp.modularity.taxonomy.entity.SkillCategory;

public abstract class SkillCoverageAlghorithm {

	/**
	 * @return an identifier for this algorithm 
	 */
	public abstract String getId();
	
	/**
	 * Calculates the relevant points in time when new staff entries have impact
	 * on the specified skill requirements
	 * 
	 * @param skills
	 *            a taxonomy subset to be tracked
	 * @param startDate
	 *            the date from which the analysis begin
	 * 
	 * @return a list of checkpoints that represent the future staff
	 *         availability relative to the provided skills
	 */
	public List<Checkpoint> computeCoverage(
			Map<SkillCategory, Integer> expectedCountsPerSkill, Date startDate) {
		Set<SkillCategory> skills = expectedCountsPerSkill.keySet();

		List<Checkpoint> results = new ArrayList<>();
		CumulativeCheckpoint initialStatus = new CumulativeCheckpoint(
				getInitialStatus(skills, startDate), null,
				expectedCountsPerSkill);
		if (initialStatus != null) {
			results.add(initialStatus);
		}
		CumulativeCheckpoint previousCheckpoint = initialStatus;
		for (EmployeeEntries bean : getEntriesFrom(skills, startDate)) {
			CumulativeCheckpoint currentCheckpoint = new CumulativeCheckpoint(
					bean, previousCheckpoint, expectedCountsPerSkill);
			results.add(currentCheckpoint);
			previousCheckpoint = currentCheckpoint;
		}

		return results;
	}

	protected abstract EmployeeEntries getInitialStatus(
			Collection<SkillCategory> skills, Date startDate);

	protected abstract List<EmployeeEntries> getEntriesFrom(
			Collection<SkillCategory> skills, Date startDate);

}
