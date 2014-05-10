package ro.codecamp.modularity.forecast;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import ro.codecamp.modularity.employee.control.DeliveryUnitStore;
import ro.codecamp.modularity.employee.entity.DeliveryUnit;
import ro.codecamp.modularity.forecast.skillcoverage_api.Checkpoint;
import ro.codecamp.modularity.forecast.skillcoverage_api.SkillCoverage;
import ro.codecamp.modularity.infrastructure.DateUtil;
import ro.codecamp.modularity.taxonomy.boundary.NodeEnricher;
import ro.codecamp.modularity.taxonomy.boundary.SkillRepresentationBuilder;
import ro.codecamp.modularity.taxonomy.control.SkillStore;
import ro.codecamp.modularity.taxonomy.entity.SkillCategory;

import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.json.JsonRepresentationFactory;

@Stateless
@Path("/forecast")
public class ForecastController {

	protected JsonRepresentationFactory halFactory = new JsonRepresentationFactory();

	@Inject
	private ForecastService forecastService;

	@Inject
	private DistributionService distributionService;

	@Inject
	private DeliveryUnitStore duStore;

	@Inject
	private SkillStore skillStore;

	@Inject
	private SkillRepresentationBuilder skillReprBuilder;

	@GET
	@Path("/checkpoints/algorithms")
	public Representation getAlgorithms() {
		Representation result = halFactory.newRepresentation();
		for (String algId : forecastService.getAlgIds()) {
			result.withRepresentation("algorithms", halFactory
					.newRepresentation().withProperty("algId", algId));
		}
		return result;
	}

	@GET
	@Path("/checkpoints/{oppId}")
	public Representation getCheckpoints(@PathParam("oppId") Long oppId) {
		return getCheckpoints(null, oppId);
	}

	@GET
	@Path("/checkpoints/{algId}/{oppId}")
	public Representation getCheckpoints(@PathParam("algId") String algId,
			@PathParam("oppId") Long oppId) {
		List<Checkpoint> checkpoints = forecastService.computeCheckpoints(
				oppId, algId);

		Representation result = halFactory.newRepresentation();
		for (Checkpoint bean : checkpoints) {
			Representation checkpoint = halFactory.newRepresentation();
			checkpoint.withProperty("date",
					DateUtil.formatJSDate(bean.getDate()));

			for (SkillCategory skill : bean.getSkillCoverage().keySet()) {
				SkillCoverage coverage = bean.getSkillCoverage().get(skill);
				Representation skillRep = skillReprBuilder.createWithName(
						skill, null);
				skillRep.withProperty("actual", coverage.getActual());
				skillRep.withProperty("expected", coverage.getExpected());
				checkpoint.withRepresentation("skills", skillRep);
			}

			result.withRepresentation("checkpoints", checkpoint);
		}
		return result;
	}

	@GET
	@Path("/distribution")
	public Representation getFullEmpDistribution() {
		SkillCategory skill = skillStore.findRoot();
		return skillReprBuilder.createFullTree(skill,
				new EmpDistributionEnricher());
	}

	@GET
	@Path("/distribution/{id}")
	public Representation getEmpDistributionBySkill(@PathParam("id") Long id) {
		SkillCategory skill = skillStore.findById(SkillCategory.class, id);
		return skillReprBuilder.createFullTree(skill,
				new EmpDistributionEnricher());
	}

	private class EmpDistributionEnricher implements NodeEnricher {

		@Override
		public void enrich(SkillCategory skill, Representation representation) {
			if (!skill.isLeaf()) {
				return;
			}
			Map<String, Integer> distribution = distributionService
					.computeDistribution(skill);
			int totalEmpsAllDUs = 0;
			Representation empInfo = halFactory.newRepresentation();
			for (DeliveryUnit du : duStore.findAll(DeliveryUnit.class)) {
				Integer count = distribution.get(du.getCode());
				if (count == null) {
					count = 0;
				}
				empInfo.withProperty("total" + du.getCode(), count);
				totalEmpsAllDUs += count;
			}
			empInfo.withProperty("total", totalEmpsAllDUs);
			empInfo.withProperty("delta",
					distributionService.computeAvailableMinusRequired(skill));
			representation.withRepresentation("empInfo", empInfo);
		}
	}

}
