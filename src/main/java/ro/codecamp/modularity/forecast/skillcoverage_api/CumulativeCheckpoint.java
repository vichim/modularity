package ro.codecamp.modularity.forecast.skillcoverage_api;

import java.util.Map;

import ro.codecamp.modularity.taxonomy.entity.SkillCategory;

class CumulativeCheckpoint extends Checkpoint {

	public CumulativeCheckpoint(EmployeeEntries entries,
			CumulativeCheckpoint previousCheckpoint,
			Map<SkillCategory, Integer> expectedCounts) {
		setDate(entries.getDate());
		for (SkillCategory skill : expectedCounts.keySet()) {
			Integer actual = entries.getSkillEntries().get(skill);
			if (actual == null) {
				actual = 0;
			}
			if (previousCheckpoint != null) {
				SkillCoverage previousCoverage = previousCheckpoint
						.getSkillCoverage().get(skill);
				if (previousCoverage != null) {
					actual += previousCoverage.getActual();
				}
			}
			getSkillCoverage().put(skill,
					new SkillCoverage(actual, expectedCounts.get(skill)));
		}
	}

}
