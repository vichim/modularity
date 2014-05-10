package ro.codecamp.modularity.taxonomy.control;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import ro.codecamp.modularity.infrastructure.control.CRUDService;
import ro.codecamp.modularity.taxonomy.entity.SkillCategory;

@Stateless
public class SkillStore extends CRUDService<SkillCategory> {

	public SkillCategory findRoot() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<SkillCategory> query = cb
				.createQuery(SkillCategory.class);
		Root<SkillCategory> root = query.from(SkillCategory.class);

		query.where(cb.isNull(root.get("parent")));

		List<SkillCategory> resultList = em.createQuery(query).getResultList();
		if (resultList.isEmpty()) {
			return null;
		}
		return resultList.get(0);
	}

	public SkillCategory findByName(String name) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<SkillCategory> query = cb
				.createQuery(SkillCategory.class);
		Root<SkillCategory> root = query.from(SkillCategory.class);

		query.where(cb.equal(root.get("name"), name));

		List<SkillCategory> resultList = em.createQuery(query).getResultList();
		if (resultList.isEmpty()) {
			return null;
		}
		return resultList.get(0);
	}

}
