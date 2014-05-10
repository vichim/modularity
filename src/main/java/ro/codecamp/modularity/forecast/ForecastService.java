package ro.codecamp.modularity.forecast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import ro.codecamp.modularity.forecast.skillcoverage_api.Checkpoint;
import ro.codecamp.modularity.forecast.skillcoverage_api.SkillCoverageAlghorithm;
import ro.codecamp.modularity.opportunity.control.OpportunityStore;
import ro.codecamp.modularity.opportunity.entity.Opportunity;
import ro.codecamp.modularity.opportunity.entity.SkillRequirement;
import ro.codecamp.modularity.taxonomy.entity.SkillCategory;

@Stateless
public class ForecastService {

	@Inject
	private OpportunityStore oppStore;

	@Inject
	private Instance<SkillCoverageAlghorithm> algorithms;

	public List<Checkpoint> computeCheckpoints(Long oppId, String algId) {
		Opportunity opportunity = oppStore.findById(Opportunity.class, oppId);
		Map<SkillCategory, Integer> skillsRequiredMap = new HashMap<SkillCategory, Integer>();
		for (SkillRequirement req : opportunity.getSkills()) {
			skillsRequiredMap.put(req.getSkillCategory(), req.getHeadCount());
		}

		List<Checkpoint> checkpoints = selectAlgorithm(algId).computeCoverage(
				skillsRequiredMap, opportunity.getStartDate());

		// mark the first checkpoint with current date
		if (checkpoints.size() > 0) {
			checkpoints.get(0).setDate(new Date());
		}

		return checkpoints;
	}

	public List<String> getAlgIds() {
		List<String> results = new ArrayList<>();
		for (SkillCoverageAlghorithm alg : algorithms) {
			results.add(alg.getId());
		}
		return results;
	}

	private SkillCoverageAlghorithm selectAlgorithm(String algId) {
		if (algId == null) {
			return algorithms.iterator().next();
		}
		for (SkillCoverageAlghorithm alg : algorithms) {
			if (alg.getId().equals(algId)) {
				return alg;
			}
		}
		return algorithms.iterator().next();
	}

}
