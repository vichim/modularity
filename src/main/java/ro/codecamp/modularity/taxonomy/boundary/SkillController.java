package ro.codecamp.modularity.taxonomy.boundary;

import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import ro.codecamp.modularity.infrastructure.rest.CRUDHalResource;
import ro.codecamp.modularity.infrastructure.rest.LinkUtil;
import ro.codecamp.modularity.taxonomy.control.SkillStore;
import ro.codecamp.modularity.taxonomy.entity.SkillCategory;

import com.theoryinpractise.halbuilder.api.Link;
import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.api.Representation;

@Stateless
@Path("/skills")
public class SkillController extends CRUDHalResource {

	@Inject
	private SkillStore skillStore;

	@Inject
	private SkillRepresentationBuilder reprBuilder;
	
	@Override
	protected String getCollectionPath() {
		return reprBuilder.getPath();
	}

	@GET
	public Representation getRoot() {
		SkillCategory skill = skillStore.findRoot();
		return reprBuilder.createFullTree(skill, null);
	}

	@Override
	protected Long doCreate(ReadableRepresentation representation) {
		Map<String, Object> props = representation.getProperties();
		String name = (String) props.get("name");

		Link parent = representation.getLinkByRel("parent");
		SkillCategory parentSkill = skillStore.findById(SkillCategory.class,
				LinkUtil.getIdFromLink(parent.getHref()));

		SkillCategory skill = skillStore.persist(new SkillCategory(name,
				parentSkill));
		return skill.getId();
	}

	@Override
	protected Representation doFindById(Long id) {
		SkillCategory skill = skillStore.findById(SkillCategory.class, id);
		return reprBuilder.createFullTree(skill, null);
	}

	@Override
	protected Representation doUpdate(ReadableRepresentation representation,
			Long id) {
		Map<String, Object> props = representation.getProperties();
		String name = (String) props.get("name");

		SkillCategory newVersion = new SkillCategory(name, null);
		newVersion.setId(id);
		SkillCategory updatedVersion = skillStore.update(newVersion);

		return reprBuilder.createWithNameAndParent(updatedVersion, null);
	}

	@Override
	protected boolean doDelete(Long id) {
		return skillStore.delete(SkillCategory.class, id);
	}

}
