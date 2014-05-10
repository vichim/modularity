package ro.codecamp.modularity.employee.boundary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import ro.codecamp.modularity.employee.control.EmployeeStore;
import ro.codecamp.modularity.employee.entity.Employee;
import ro.codecamp.modularity.infrastructure.control.SearchResults;
import ro.codecamp.modularity.infrastructure.rest.CRUDHalResource;

import com.theoryinpractise.halbuilder.api.Link;
import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.api.Representation;

@Stateless
@Path("/employees")
public class EmployeeController extends CRUDHalResource {

	@Inject
	private EmployeeStore empStore;

	@Inject
	private EmpRepresentationBuilder reprBuilder;

	@Override
	protected String getCollectionPath() {
		return reprBuilder.getPath();
	}

	@GET
	public Representation find() {
		return getCollection(0);
	}

	@GET
	@Path("/page/{pageNo}")
	public Representation findPaginated(@PathParam("pageNo") int pageNo) {
		return getCollection(pageNo);
	}

	private Representation getCollection(int page) {
		SearchResults<Employee> searchResults = empStore.search(page);

		Representation representation = halFactory.newRepresentation("/"
				+ getCollectionPath() + "/page/" + page);
		Link previousLink = getPreviousLink(page, searchResults);
		if (previousLink != null) {
			representation.withLink(previousLink.getRel(),
					previousLink.getHref());
		}
		Link nextLink = getNextLink(page, searchResults);
		if (nextLink != null) {
			representation.withLink(nextLink.getRel(), nextLink.getHref());
		}

		for (Employee bean : searchResults.getItems()) {
			representation.withRepresentation("items",
					reprBuilder.createRepresentation(bean));
		}

		return representation;
	}

	private Link getPreviousLink(int currentPage,
			SearchResults<Employee> searchItems) {
		if (!searchItems.isHasPrevious()) {
			return null;
		}
		return new Link(halFactory, "previous", "/" + getCollectionPath()
				+ "/page/" + (currentPage - 1));
	}

	private Link getNextLink(int currentPage,
			SearchResults<Employee> searchItems) {
		if (!searchItems.isHasNext()) {
			return null;
		}
		return new Link(halFactory, "next", "/" + getCollectionPath()
				+ "/page/" + (currentPage + 1));
	}

	@Override
	protected Long doCreate(ReadableRepresentation representation) {
		Employee emp = empStore.persist(reprBuilder
				.createEntity(representation));
		return emp.getId();
	}

	@Override
	protected Representation doFindById(Long id) {
		Employee emp = empStore.findById(Employee.class, id);
		return reprBuilder.createRepresentation(emp);
	}

	@Override
	protected Representation doUpdate(ReadableRepresentation representation,
			Long id) {
		Employee emp = reprBuilder.createEntity(representation);
		emp.setId(id);
		empStore.update(emp);
		return reprBuilder.createRepresentation(emp);
	}

	@Override
	protected boolean doDelete(Long id) {
		return empStore.delete(Employee.class, id);
	}

}
