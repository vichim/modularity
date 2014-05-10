package ro.codecamp.modularity.employee.boundary;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import ro.codecamp.modularity.employee.control.DeliveryUnitStore;
import ro.codecamp.modularity.employee.entity.Employee;
import ro.codecamp.modularity.employee.entity.EmployeeSkill;
import ro.codecamp.modularity.infrastructure.DateUtil;
import ro.codecamp.modularity.infrastructure.rest.LinkUtil;
import ro.codecamp.modularity.taxonomy.boundary.SkillRepresentationBuilder;
import ro.codecamp.modularity.taxonomy.control.SkillStore;
import ro.codecamp.modularity.taxonomy.entity.SkillCategory;
import ro.codecamp.modularity.taxonomy.entity.SkillLevel;

import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.json.JsonRepresentationFactory;

public class EmpRepresentationBuilder {

	private JsonRepresentationFactory halFactory = new JsonRepresentationFactory();

	@Inject
	private DeliveryUnitStore duStore;

	@Inject
	private SkillStore skillStore;

	@Inject
	private SkillRepresentationBuilder skillReprBuilder;

	public String getPath() {
		return "employees";
	}

	public Representation createRepresentation(Employee emp) {
		Representation result = nameOnlyRepresentation(emp).withProperty(
				"startDate", DateUtil.formatJSDate(emp.getStartDate()))
				.withProperty("du", emp.getDeliveryUnit().getCode());

		for (EmployeeSkill bean : emp.getSkills()) {
			Representation category = skillReprBuilder.createWithName(
					bean.getSkillCategory(), null);
			Representation skill = halFactory.newRepresentation()
					.withRepresentation("category", category)
					.withProperty("level", bean.getLevel().getDescription());

			result.withRepresentation("skills", skill);
		}

		return result;
	}

	public Representation nameOnlyRepresentation(Employee emp) {
		return halFactory
				.newRepresentation("/" + getPath() + "/" + emp.getId())
				.withProperty("name", emp.getName());
	}

	public Employee createEntity(ReadableRepresentation representation) {
		Map<String, Object> props = representation.getProperties();
		String name = (String) props.get("name");
		Date startDate = DateUtil.parseJSDate((String) props.get("startDate"));
		String deliveryUnit = (String) props.get("du");
		Employee result = new Employee(name, startDate,
				duStore.findByCode(deliveryUnit));

		List<? extends ReadableRepresentation> skillReps = representation
				.getResourcesByRel("skills");
		List<EmployeeSkill> skills = new ArrayList<EmployeeSkill>();
		for (ReadableRepresentation bean : skillReps) {
			EmployeeSkill skill = new EmployeeSkill();
			skill.setEmployee(result);
			String categoryHref = bean.getResourceMap().get("category")
					.iterator().next().getResourceLink().getHref();
			skill.setSkillCategory(skillStore.findById(SkillCategory.class,
					LinkUtil.getIdFromLink(categoryHref)));
			skill.setLevel(SkillLevel.fromDescription((String) bean
					.getValue("level")));

			skills.add(skill);
		}

		return result;
	}

}
