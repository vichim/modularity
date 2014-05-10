package ro.codecamp.modularity.project.boundary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import ro.codecamp.modularity.employee.boundary.EmpRepresentationBuilder;
import ro.codecamp.modularity.employee.control.EmployeeStore;
import ro.codecamp.modularity.employee.entity.Employee;
import ro.codecamp.modularity.infrastructure.rest.LinkUtil;
import ro.codecamp.modularity.project.entity.Project;

import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.json.JsonRepresentationFactory;

public class ProjectRepresentationBuilder {

	private JsonRepresentationFactory halFactory = new JsonRepresentationFactory();

	@Inject
	private EmployeeStore empStore;

	@Inject
	private EmpRepresentationBuilder empBuilder;

	public String getPath() {
		return "projects";
	}

	public Representation createRepresentation(Project project) {
		Representation result = halFactory.newRepresentation(
				"/" + getPath() + "/" + project.getId()).withProperty("name",
				project.getName());

		for (Employee bean : project.getEmployees()) {
			Representation emp = empBuilder.nameOnlyRepresentation(bean);
			result.withRepresentation("employees", emp);
		}
		return result;
	}

	public Project createEntity(ReadableRepresentation representation) {
		Map<String, Object> props = representation.getProperties();
		String name = (String) props.get("name");

		List<? extends ReadableRepresentation> empReps = representation
				.getResourcesByRel("employees");
		List<Employee> emps = new ArrayList<>();
		for (ReadableRepresentation bean : empReps) {
			String empHref = bean.getResourceLink().getHref();
			emps.add(empStore.findById(Employee.class,
					LinkUtil.getIdFromLink(empHref)));
		}

		return new Project(name, emps);
	}

}
