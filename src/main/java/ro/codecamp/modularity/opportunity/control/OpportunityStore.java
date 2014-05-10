package ro.codecamp.modularity.opportunity.control;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import ro.codecamp.modularity.infrastructure.control.CRUDService;
import ro.codecamp.modularity.infrastructure.control.SearchResults;
import ro.codecamp.modularity.opportunity.entity.Opportunity;
import ro.codecamp.modularity.opportunity.entity.SkillRequirement;
import ro.codecamp.modularity.taxonomy.control.SkillStore;
import ro.codecamp.modularity.taxonomy.entity.SkillCategory;

@Stateless
public class OpportunityStore extends CRUDService<Opportunity> {

	@Inject
	private SkillStore skillStore;

	public SearchResults<Opportunity> search() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Opportunity> query = cb.createQuery(Opportunity.class);
		query.from(Opportunity.class);

		List<Opportunity> opportunities = em.createQuery(query).getResultList();

		SearchResults<Opportunity> results = new SearchResults<>(
				opportunities.size());
		results.addItems(opportunities);

		return results;
	}

	public Integer getTotalHeadCountBySkill(SkillCategory skill) {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
		Root<Opportunity> root = query.from(Opportunity.class);
		Join<Opportunity, SkillRequirement> joinSkill = root.join("skills");

		query.where(cb.equal(joinSkill.get("skillCategory"), skill.getId()));

		query.select(cb.sum(joinSkill.get("headCount").as(Integer.class)));

		List<Integer> resultList = em.createQuery(query).getResultList();
		if (resultList == null || resultList.isEmpty()
				|| resultList.get(0) == null) {
			return 0;
		}
		return resultList.get(0);
	}

	@Override
	public Opportunity update(Opportunity item) {
		// Ensure all the key elements are managed (Hibernate issue?)
		for (SkillRequirement skill : item.getSkills()) {
			skill.setSkillCategory(skillStore.findById(SkillCategory.class,
					skill.getSkillCategory().getId()));
			skill.setOpportunity(findById(Opportunity.class, skill
					.getOpportunity().getId()));
		}
		return super.update(item);
	}

	@Override
	public Opportunity persist(Opportunity item) {
		for (SkillRequirement skill : item.getSkills()) {
			skill.setSkillCategory(skillStore.findById(SkillCategory.class,
					skill.getSkillCategory().getId()));
		}
		return super.persist(item);
	}

}
