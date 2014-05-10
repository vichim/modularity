package ro.codecamp.modularity.opportunity.boundary;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import ro.codecamp.modularity.infrastructure.DateUtil;
import ro.codecamp.modularity.infrastructure.rest.LinkUtil;
import ro.codecamp.modularity.opportunity.entity.Opportunity;
import ro.codecamp.modularity.opportunity.entity.SkillRequirement;
import ro.codecamp.modularity.taxonomy.boundary.SkillRepresentationBuilder;
import ro.codecamp.modularity.taxonomy.control.SkillStore;
import ro.codecamp.modularity.taxonomy.entity.SkillCategory;
import ro.codecamp.modularity.taxonomy.entity.SkillLevel;

import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.json.JsonRepresentationFactory;

public class OppRepresentationBuilder {

	private JsonRepresentationFactory halFactory = new JsonRepresentationFactory();

	@Inject
	private SkillStore skillStore;

	@Inject
	private SkillRepresentationBuilder skillReprBuilder;

	public String getPath() {
		return "opportunities";
	}

	public Representation createRepresentation(Opportunity opportunity) {
		Representation result = halFactory
				.newRepresentation("/" + getPath() + "/" + opportunity.getId())
				.withProperty("name", opportunity.getName())
				.withProperty("startDate",
						DateUtil.formatJSDate(opportunity.getStartDate()));
		for (SkillRequirement bean : opportunity.getSkills()) {
			Representation category = skillReprBuilder.createWithName(
					bean.getSkillCategory(), null);
			Representation skill = halFactory.newRepresentation()
					.withRepresentation("category", category)
					.withProperty("level", bean.getLevel().getDescription())
					.withProperty("headCount", bean.getHeadCount());

			result.withRepresentation("skills", skill);
		}
		return result;
	}

	public Opportunity createEntity(ReadableRepresentation representation) {
		Map<String, Object> props = representation.getProperties();
		String name = (String) props.get("name");
		Date startDate = DateUtil.parseJSDate((String) props.get("startDate"));
		List<? extends ReadableRepresentation> skillReps = representation
				.getResourcesByRel("skills");
		List<SkillRequirement> skills = new ArrayList<SkillRequirement>();
		for (ReadableRepresentation bean : skillReps) {
			SkillRequirement skill = new SkillRequirement();
			String categoryHref = bean.getResourceMap().get("category")
					.iterator().next().getResourceLink().getHref();
			skill.setSkillCategory(skillStore.findById(SkillCategory.class,
					LinkUtil.getIdFromLink(categoryHref)));
			skill.setLevel(SkillLevel.fromDescription((String) bean
					.getValue("level")));
			skill.setHeadCount(Integer.valueOf((String) bean
					.getValue("headCount")));

			skills.add(skill);
		}

		return new Opportunity(name, startDate, skills);
	}

}
