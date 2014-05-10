package ro.codecamp.modularity.project.boundary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import ro.codecamp.modularity.infrastructure.control.SearchResults;
import ro.codecamp.modularity.infrastructure.rest.CRUDHalResource;
import ro.codecamp.modularity.project.control.ProjectStore;
import ro.codecamp.modularity.project.entity.Project;

import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.api.Representation;

@Stateless
@Path("/projects")
public class ProjectController extends CRUDHalResource {

	@Inject
	private ProjectStore projectStore;

	@Inject
	private ProjectRepresentationBuilder reprBuilder;

	@Override
	protected String getCollectionPath() {
		return reprBuilder.getPath();
	}

	@GET
	public Representation search() {
		SearchResults<Project> searchResults = projectStore.search();

		Representation representation = halFactory.newRepresentation("/"
				+ getCollectionPath());
		for (Project bean : searchResults.getItems()) {
			representation.withRepresentation("items",
					reprBuilder.createRepresentation(bean));
		}

		return representation;
	}

	@Override
	protected Representation doFindById(Long id) {
		Project project = projectStore.findById(Project.class, id);
		return reprBuilder.createRepresentation(project);
	}

	@Override
	protected Long doCreate(ReadableRepresentation representation) {
		Project project = projectStore.persist(reprBuilder
				.createEntity(representation));
		return project.getId();
	}

	@Override
	protected Representation doUpdate(ReadableRepresentation representation,
			Long id) {
		Project project = reprBuilder.createEntity(representation);
		project.setId(id);
		projectStore.update(project);
		return reprBuilder.createRepresentation(project);
	}

	@Override
	protected boolean doDelete(Long id) {
		return projectStore.delete(Project.class, id);
	}

}
