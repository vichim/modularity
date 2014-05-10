package ro.codecamp.modularity.project;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import ro.codecamp.modularity.employee.EmployeeTestUtil;
import ro.codecamp.modularity.employee.entity.Employee;
import ro.codecamp.modularity.project.control.ProjectStore;
import ro.codecamp.modularity.project.entity.Project;
import ro.codecamp.modularity.taxonomy.control.SkillStore;
import ro.codecamp.modularity.taxonomy.entity.SkillCategory;

@Path("/testdata/projects")
public class ProjectTestUtil {

	@Inject
	private ProjectStore projectStore;

	@Inject
	private SkillStore skillStore;

	@Inject
	private EmployeeTestUtil empTestUtil;

	public Project createProject(List<Employee> emps, Date endDate) {
		Project project = new Project("Project Test");
		project.getEmployees().addAll(emps);
		project.setEndDate(endDate);

		return projectStore.persist(project);
	}

	@POST
	public void createProjects() {
		Calendar calendar = Calendar.getInstance();

		Date present = calendar.getTime();
		calendar.add(Calendar.MONTH, 1);
		Date checkpoint1 = calendar.getTime();
		calendar.add(Calendar.MONTH, 1);
		Date checkpoint2 = calendar.getTime();
		calendar.add(Calendar.MONTH, 1);
		Date checkpoint3 = calendar.getTime();
		calendar.add(Calendar.MONTH, 1);
		Date checkpoint4 = calendar.getTime();

		SkillCategory angular = skillStore.findByName("AngularJS");
		SkillCategory require = skillStore.findByName("RequireJS");
		SkillCategory jpa = skillStore.findByName("JPA");
		SkillCategory ejb = skillStore.findByName("EJB");

		empTestUtil.createEmps(2, present, jpa);
		empTestUtil.createEmps(2, present, ejb);
		List<Employee> allocatedEmpsProj1 = empTestUtil.createEmps(2,
				checkpoint2, require);
		List<Employee> allocatedEmpsProj2 = empTestUtil.createEmps(2,
				checkpoint4, angular);
		empTestUtil.createEmps(2, checkpoint1, angular);
		empTestUtil.createEmps(1, checkpoint1, jpa);
		empTestUtil.createEmps(1, checkpoint2, require);
		empTestUtil.createEmps(2, checkpoint2, angular);
		empTestUtil.createEmps(3, checkpoint2, ejb);
		empTestUtil.createEmps(1, checkpoint3, require);
		empTestUtil.createEmps(1, checkpoint3, jpa);

		createProject(allocatedEmpsProj1, present);
		createProject(allocatedEmpsProj2, checkpoint2);
	}

}
