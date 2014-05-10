package ro.codecamp.modularity.taxonomy.boundary;

import ro.codecamp.modularity.taxonomy.entity.SkillCategory;

import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.json.JsonRepresentationFactory;

public class SkillRepresentationBuilder {

	private JsonRepresentationFactory halFactory = new JsonRepresentationFactory();

	public String getPath() {
		return "skills";
	}

	public Representation createFullTree(SkillCategory skill,
			NodeEnricher nodeEnricher) {
		Representation representation = createWithNameAndParent(skill,
				nodeEnricher);
		addEmbeddedLinks(representation, skill, nodeEnricher);
		return representation;
	}

	public Representation createWithNameAndParent(SkillCategory skill,
			NodeEnricher nodeEnricher) {
		Representation representation = createWithName(skill, nodeEnricher);
		if (skill.getParent() != null) {
			representation.withLink("parent", "/" + getPath() + "/"
					+ skill.getParent().getId());
		}
		return representation;
	}

	public Representation createWithName(SkillCategory skill,
			NodeEnricher nodeEnricher) {
		Representation representation = halFactory.newRepresentation("/"
				+ getPath() + "/" + skill.getId());
		representation.withProperty("name", skill.getName());
		if (nodeEnricher != null) {
			nodeEnricher.enrich(skill, representation);
		}
		return representation;
	}

	public void addEmbeddedLinks(Representation representation,
			SkillCategory skill, NodeEnricher nodeEnricher) {
		for (SkillCategory child : skill.getSubCategories()) {
			Representation embedded = createWithName(child, nodeEnricher);
			addEmbeddedLinks(embedded, child, nodeEnricher);
			representation.withRepresentation("children", embedded);
		}
	}

}
