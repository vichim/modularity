package ro.codecamp.modularity.skillcoverage.default_impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import ro.codecamp.modularity.employee.entity.Employee;
import ro.codecamp.modularity.employee.entity.EmployeeSkill;
import ro.codecamp.modularity.forecast.skillcoverage_api.EmployeeEntries;
import ro.codecamp.modularity.forecast.skillcoverage_api.SkillCoverageAlghorithm;
import ro.codecamp.modularity.project.entity.Project;
import ro.codecamp.modularity.taxonomy.entity.SkillCategory;

public class DefaultSkillCoverageAlg extends SkillCoverageAlghorithm {

	@PersistenceContext
	private EntityManager em;

	@Override
	public String getId() {
		return "default";
	}
	
	@Override
	protected EmployeeEntries getInitialStatus(
			Collection<SkillCategory> skills, Date startDate) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> query = cb.createTupleQuery();

		Root<Employee> root = query.from(Employee.class);
		Join<Employee, EmployeeSkill> join = root.join("skills");

		query.select(cb.tuple(cb.count(root), join.get("skillCategory")));
		query.groupBy(join.get("skillCategory"));

		In<Object> in = cb.in(join.get("skillCategory"));
		for (SkillCategory bean : skills) {
			in.value(bean);
		}
		query.having(in);

		Predicate dateCondition = cb.lessThan(
				root.get("startDate").as(Date.class), startDate);
		Predicate excludeEmpInProjects = cb.not(cb
				.exists(createProjectsSubquery(query, root, startDate)));
		query.where(cb.and(dateCondition, excludeEmpInProjects));

		TypedQuery<Tuple> typedQuery = em.createQuery(query);

		List<Tuple> resultList = typedQuery.getResultList();
		if (resultList == null || resultList.isEmpty()) {
			return new EmployeeEntries(new Date());
		}

		Tuple firstResult = resultList.get(0);
		Long noEntries = firstResult.get(0, Long.class);
		SkillCategory skill = firstResult.get(1, SkillCategory.class);

		EmployeeEntries result = new EmployeeEntries(new Date());
		result.getSkillEntries().put(skill, noEntries.intValue());

		return result;
	}

	@Override
	protected List<EmployeeEntries> getEntriesFrom(
			Collection<SkillCategory> skills, Date startDate) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> query = cb.createTupleQuery();

		Root<Employee> root = query.from(Employee.class);
		Join<Employee, EmployeeSkill> join = root.join("skills");

		query.select(cb.tuple(cb.count(root), root.get("startDate"),
				join.get("skillCategory")));
		query.groupBy(root.get("startDate"), join.get("skillCategory"));
		query.orderBy(cb.asc(root.get("startDate")));

		In<Object> in = cb.in(join.get("skillCategory"));
		for (SkillCategory bean : skills) {
			in.value(bean);
		}
		query.having(in);

		Predicate dateCondition = cb.greaterThanOrEqualTo(root.get("startDate")
				.as(Date.class), startDate);
		Predicate excludeEmpInProjects = cb.not(cb
				.exists(createProjectsSubquery(query, root, startDate)));
		query.where(cb.and(dateCondition, excludeEmpInProjects));

		TypedQuery<Tuple> typedQuery = em.createQuery(query);

		List<Tuple> resultList = typedQuery.getResultList();
		if (resultList == null || resultList.isEmpty()) {
			return Collections.emptyList();
		}

		return getEmployeeEntriesFromTuple(resultList);
	}

	private Subquery<Project> createProjectsSubquery(
			CriteriaQuery<Tuple> parentQuery, Root<Employee> parentRoot,
			Date startDate) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		Subquery<Project> subquery = parentQuery.subquery(Project.class);
		Root<Project> root = subquery.from(Project.class);
		Join<Project, Employee> join = root.join("employees");

		Predicate idCondition = cb.equal(parentRoot.get("id"), join.get("id"));
		subquery.where(idCondition);

		return subquery.select(root);
	}

	private List<EmployeeEntries> getEmployeeEntriesFromTuple(
			List<Tuple> resultList) {
		Map<Date, EmployeeEntries> entriesMap = new LinkedHashMap<>();
		for (Tuple tuple : resultList) {
			Long noEntries = tuple.get(0, Long.class);
			Date date = tuple.get(1, Date.class);
			SkillCategory skill = tuple.get(2, SkillCategory.class);

			EmployeeEntries entries = entriesMap.get(date);
			if (entries == null) {
				entries = new EmployeeEntries(date);
				entriesMap.put(date, entries);
			}
			entries.getSkillEntries().put(skill, noEntries.intValue());
		}

		return new ArrayList<>(entriesMap.values());
	}

}
