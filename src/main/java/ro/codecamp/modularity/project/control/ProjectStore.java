package ro.codecamp.modularity.project.control;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import ro.codecamp.modularity.infrastructure.control.CRUDService;
import ro.codecamp.modularity.infrastructure.control.SearchResults;
import ro.codecamp.modularity.project.entity.Project;

@Stateless
public class ProjectStore extends CRUDService<Project> {

	public SearchResults<Project> search() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Project> query = cb.createQuery(Project.class);
		query.from(Project.class);
		
		List<Project> projects = em.createQuery(query).getResultList();

		SearchResults<Project> results = new SearchResults<>(projects.size());
		results.addItems(projects);

		return results;
	}

}
