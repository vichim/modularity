package ro.codecamp.modularity.employee;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import ro.codecamp.modularity.employee.control.DeliveryUnitStore;
import ro.codecamp.modularity.employee.control.EmployeeStore;
import ro.codecamp.modularity.employee.entity.DeliveryUnit;
import ro.codecamp.modularity.employee.entity.Employee;
import ro.codecamp.modularity.employee.entity.EmployeeSkill;
import ro.codecamp.modularity.taxonomy.control.SkillStore;
import ro.codecamp.modularity.taxonomy.entity.SkillCategory;
import ro.codecamp.modularity.taxonomy.entity.SkillLevel;

@Path("/testdata/employees")
public class EmployeeTestUtil {

	@Inject
	private DeliveryUnitStore duStore;

	@Inject
	private EmployeeStore empStore;
	
	@Inject
	private SkillStore skillStore;

	private static final String DU_CODE = "ISD";

	public List<Employee> createEmps(int noEmps, Date startDate,
			SkillCategory... skills) {
		List<Employee> results = new ArrayList<>();
		DeliveryUnit du = duStore.findByCode(DU_CODE);
		for (int i = 0; i < noEmps; i++) {
			Employee emp = new Employee("Emp " + i, startDate, du);
			for (SkillCategory skill : skills) {
				emp.getSkills().add(
						new EmployeeSkill(emp, skill, SkillLevel.AwareOf));
			}
			results.add(empStore.persist(emp));
		}
		return results;
	}
	
	@POST
	@Path("/du")
	public void createDeliveryUnits() {
		duStore.persist(new DeliveryUnit("BHD", "Bucharest"));
		duStore.persist(new DeliveryUnit("CLD", "Cluj"));
		duStore.persist(new DeliveryUnit("ISD", "Iasi"));
		duStore.persist(new DeliveryUnit("MDD", "Chisinau"));
	}

	@POST
	@Path("/emp")
	public void createEmployees() {
		for (int i = 0; i < 15; i++) {
			Employee emp = new Employee("Emp " + (i + 1), new Date(),
					duStore.findById(DeliveryUnit.class, (long) i % 4 + 1));
			if (i % 3 == 0) {
				emp.getSkills().addAll(createJavaProfile(emp));
			} else {
				emp.getSkills().addAll(createNetProfile(emp));
			}
			empStore.persist(emp);
		}
	}
	
	private List<EmployeeSkill> createJavaProfile(Employee emp) {
		List<EmployeeSkill> skills = new ArrayList<>();
		SkillCategory ejb = skillStore.findByName("JSP");
		SkillCategory jpa = skillStore.findByName("JUnit");

		skills.add(new EmployeeSkill(emp, ejb, SkillLevel.AwareOf));
		skills.add(new EmployeeSkill(emp, jpa, SkillLevel.ProficientIn));

		return skills;
	}

	private List<EmployeeSkill> createNetProfile(Employee emp) {
		List<EmployeeSkill> skills = new ArrayList<>();
		SkillCategory ejb = skillStore.findByName("ASP.NET 4.0");
		SkillCategory jpa = skillStore.findByName(".Net Remoting");

		skills.add(new EmployeeSkill(emp, ejb, SkillLevel.ProficientIn));
		skills.add(new EmployeeSkill(emp, jpa, SkillLevel.FamiliarWith));

		return skills;
	}
	
}
