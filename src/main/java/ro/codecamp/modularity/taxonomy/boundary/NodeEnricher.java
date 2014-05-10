package ro.codecamp.modularity.taxonomy.boundary;

import ro.codecamp.modularity.taxonomy.entity.SkillCategory;

import com.theoryinpractise.halbuilder.api.Representation;

public interface NodeEnricher {

	public void enrich(SkillCategory entity, Representation representation);
	
}
