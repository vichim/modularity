package ro.codecamp.modularity.employee.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import ro.codecamp.modularity.ServiceBaseTest;
import ro.codecamp.modularity.employee.EmployeeTestUtil;
import ro.codecamp.modularity.employee.control.DeliveryUnitStore;
import ro.codecamp.modularity.employee.control.EmployeeStore;
import ro.codecamp.modularity.employee.entity.Employee;
import ro.codecamp.modularity.employee.entity.EmployeeSkill;
import ro.codecamp.modularity.taxonomy.SkillTestUtil;
import ro.codecamp.modularity.taxonomy.control.SkillStore;
import ro.codecamp.modularity.taxonomy.entity.SkillCategory;
import ro.codecamp.modularity.taxonomy.entity.SkillLevel;

@RunWith(Arquillian.class)
public class EmployeeCRUDTest extends ServiceBaseTest {

	@Inject
	private EmployeeStore empStore;

	@Inject
	private DeliveryUnitStore duStore;

	@Inject
	private SkillStore skillStore;

	@Inject
	private SkillTestUtil skillTestUtil;

	@Inject
	private EmployeeTestUtil empTestUtil;

	private static final String DU_CODE = "ISD";
	private static final String SKILL_NAME = "JPA";

	@Test
	@InSequence(1)
	public void create() {
		empTestUtil.createDeliveryUnits();
		skillTestUtil.createTaxonomy();

		Employee emp = new Employee("Emp Test", new Date(),
				duStore.findByCode(DU_CODE));
		SkillCategory skill = skillStore.findByName(SKILL_NAME);
		emp.getSkills().add(
				new EmployeeSkill(emp, skill, SkillLevel.FamiliarWith));

		Employee storedEmp = empStore.persist(emp);
		assertNotNull(storedEmp.getId());

		Employee foundEmp = empStore.findById(Employee.class,
				storedEmp.getId());

		assertNotNull(foundEmp);
	}

	@Test
	@InSequence(2)
	public void update() {
		Employee emp = empStore.search(0).getItems().get(0);
		assertNotNull(emp);

		String updatedValue = "Emp Name Updated";

		emp.setName(updatedValue);
		Employee empUpdated = empStore.update(emp);

		assertEquals(updatedValue, empUpdated.getName());
	}

	@Test
	@InSequence(3)
	public void delete() {
		boolean deleted = empStore.delete(Employee.class, empStore
				.search(0).getItems().get(0).getId());
		assertTrue(deleted);
	}

}
