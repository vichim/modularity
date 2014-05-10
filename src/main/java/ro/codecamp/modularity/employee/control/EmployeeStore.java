package ro.codecamp.modularity.employee.control;

import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import ro.codecamp.modularity.employee.entity.Employee;
import ro.codecamp.modularity.employee.entity.EmployeeSkill;
import ro.codecamp.modularity.infrastructure.control.CRUDService;
import ro.codecamp.modularity.infrastructure.control.SearchResults;
import ro.codecamp.modularity.taxonomy.control.SkillStore;
import ro.codecamp.modularity.taxonomy.entity.SkillCategory;

@Stateless
public class EmployeeStore extends CRUDService<Employee> {

	private static final int ITEMS_PER_PAGE = 5;

	@Inject
	private SkillStore skillStore;

	public SearchResults<Employee> search(int page) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
		Root<Employee> root = query.from(Employee.class);
		root.fetch("skills");

		TypedQuery<Employee> typedQuery = em.createQuery(query);
		typedQuery.setMaxResults(ITEMS_PER_PAGE);
		typedQuery.setFirstResult(page * ITEMS_PER_PAGE);
		List<Employee> employees = typedQuery.getResultList();

		SearchResults<Employee> results = new SearchResults<>(employees.size());
		results.addItems(employees);
		if (page > 0) {
			results.setHasPrevious(true);
		}
		if (employees.size() == ITEMS_PER_PAGE) {
			results.setHasNext(true);
		}

		return results;
	}

	public List<Employee> findEmpsBySkill(SkillCategory skill) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
		Root<Employee> root = query.from(Employee.class);
		Join<Employee, EmployeeSkill> join = root.join("skills");

		query.where(cb.equal(join.get("skillCategory"), skill.getId()));

		List<Employee> resultList = em.createQuery(query).getResultList();
		if (resultList == null) {
			return Collections.emptyList();
		}
		return resultList;
	}

	public Integer countEmpsBySkill(SkillCategory skill) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
		Root<Employee> root = query.from(Employee.class);
		Join<Employee, EmployeeSkill> join = root.join("skills");

		query.where(cb.equal(join.get("skillCategory"), skill.getId()));
		query.select(cb.count(root).as(Integer.class));

		List<Integer> resultList = em.createQuery(query).getResultList();
		if (resultList == null || resultList.isEmpty()) {
			return 0;
		}
		return resultList.get(0);
	}

	@Override
	public Employee update(Employee item) {
		// Ensure all the key elements are managed (Hibernate issue?)
		for (EmployeeSkill skill : item.getSkills()) {
			skill.setSkillCategory(skillStore.findById(SkillCategory.class,
					skill.getSkillCategory().getId()));
			skill.setEmployee(findById(Employee.class, skill.getEmployee()
					.getId()));
		}
		return super.update(item);
	}

	@Override
	public Employee persist(Employee item) {
		for (EmployeeSkill skill : item.getSkills()) {
			skill.setSkillCategory(skillStore.findById(SkillCategory.class,
					skill.getSkillCategory().getId()));
		}
		return super.persist(item);
	}

}
