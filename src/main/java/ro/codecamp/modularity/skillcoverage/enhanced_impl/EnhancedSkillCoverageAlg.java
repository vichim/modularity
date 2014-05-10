package ro.codecamp.modularity.skillcoverage.enhanced_impl;

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

public class EnhancedSkillCoverageAlg extends SkillCoverageAlghorithm {

	@PersistenceContext
	private EntityManager em;

	@Override
	public String getId() {
		return "enhanced";
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

		Subquery<Project> subquery = query.subquery(Project.class);
		Root<Project> subqueryRoot = subquery.from(Project.class);
		Join<Project, Employee> subqueryJoin = subqueryRoot.join("employees");

		Predicate idCondition = cb
				.equal(root.get("id"), subqueryJoin.get("id"));
		subquery.where(idCondition);
		Predicate subqueryDateCondition = cb.greaterThan(
				subqueryRoot.get("endDate").as(Date.class), startDate);
		subquery.where(idCondition, subqueryDateCondition);

		Predicate excludeEmpInProjects = cb.not(cb.exists(subquery
				.select(subqueryRoot)));
		query.where(dateCondition, excludeEmpInProjects);

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
		List<EmployeeEntries> newEntries = getCandidates(skills, startDate);
		List<EmployeeEntries> entriesFromProjects = getFreeEmpsFromProjects(
				skills, startDate);

		// merge
		Map<Date, EmployeeEntries> entriesMap = new LinkedHashMap<>();
		for (EmployeeEntries bean : newEntries) {
			entriesMap.put(bean.getDate(), bean);
		}
		for (EmployeeEntries bean : entriesFromProjects) {
			EmployeeEntries existing = entriesMap.get(bean.getDate());
			if (existing != null) {
				Map<SkillCategory, Integer> existingMap = existing
						.getSkillEntries();
				for (SkillCategory skill : bean.getSkillEntries().keySet()) {
					if (existingMap.containsKey(skill)) {
						Integer currentValue = existingMap.get(skill);
						existingMap.put(skill, currentValue
								+ bean.getSkillEntries().get(skill));
					} else {
						existingMap.put(skill, bean.getSkillEntries()
								.get(skill));
					}
				}
			} else {
				entriesMap.put(bean.getDate(), bean);
			}
		}

		return new ArrayList<>(entriesMap.values());
	}

	private List<EmployeeEntries> getCandidates(
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

		Subquery<Project> subquery = query.subquery(Project.class);
		Root<Project> subqueryRoot = subquery.from(Project.class);
		Join<Project, Employee> subqueryJoin = subqueryRoot.join("employees");

		Predicate idCondition = cb
				.equal(root.get("id"), subqueryJoin.get("id"));
		subquery.where(idCondition);
		Predicate subqueryDateCondition = cb.greaterThan(
				subqueryRoot.get("endDate").as(Date.class), startDate);
		subquery.where(idCondition, subqueryDateCondition);

		Predicate excludeEmpInProjects = cb.not(cb.exists(subquery
				.select(subqueryRoot)));
		query.where(dateCondition, excludeEmpInProjects);

		TypedQuery<Tuple> typedQuery = em.createQuery(query);

		List<Tuple> resultList = typedQuery.getResultList();
		if (resultList == null || resultList.isEmpty()) {
			return Collections.emptyList();
		}

		return getEmployeeEntriesFromTuple(resultList);
	}

	private List<EmployeeEntries> getFreeEmpsFromProjects(
			Collection<SkillCategory> skills, Date startDate) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> query = cb.createTupleQuery();

		Root<Project> root = query.from(Project.class);
		Join<Project, Employee> joinEmp = root.join("employees");
		Join<Employee, EmployeeSkill> joinSkill = joinEmp.join("skills");

		query.select(cb.tuple(cb.count(joinEmp), root.get("endDate"),
				joinSkill.get("skillCategory")));
		query.groupBy(root.get("endDate"), joinSkill.get("skillCategory"));
		query.orderBy(cb.asc(root.get("endDate")));

		In<Object> in = cb.in(joinSkill.get("skillCategory"));
		for (SkillCategory bean : skills) {
			in.value(bean);
		}
		query.having(in);

		Predicate dateCondition = cb.greaterThanOrEqualTo(root.get("endDate")
				.as(Date.class), startDate);

		query.where(dateCondition);

		TypedQuery<Tuple> typedQuery = em.createQuery(query);

		List<Tuple> resultList = typedQuery.getResultList();
		if (resultList == null || resultList.isEmpty()) {
			return Collections.emptyList();
		}

		return getEmployeeEntriesFromTuple(resultList);
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
