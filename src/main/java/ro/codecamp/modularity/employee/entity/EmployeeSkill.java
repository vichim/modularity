package ro.codecamp.modularity.employee.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import ro.codecamp.modularity.taxonomy.entity.SkillCategory;
import ro.codecamp.modularity.taxonomy.entity.SkillLevel;

@Entity
@IdClass(EmployeeSkillId.class)
public class EmployeeSkill {

	@Id
	@ManyToOne
	private Employee employee;

	@Id
	@ManyToOne
	private SkillCategory skillCategory;

	@Enumerated(EnumType.STRING)
	private SkillLevel level;

	public EmployeeSkill() {
	}

	public EmployeeSkill(Employee employee, SkillCategory skillCategory,
			SkillLevel level) {
		this.employee = employee;
		this.skillCategory = skillCategory;
		this.level = level;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public SkillCategory getSkillCategory() {
		return skillCategory;
	}

	public void setSkillCategory(SkillCategory skillCategory) {
		this.skillCategory = skillCategory;
	}

	public SkillLevel getLevel() {
		return level;
	}

	public void setLevel(SkillLevel level) {
		this.level = level;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((employee == null) ? 0 : employee.hashCode());
		result = prime * result
				+ ((skillCategory == null) ? 0 : skillCategory.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeeSkill other = (EmployeeSkill) obj;
		if (employee == null) {
			if (other.employee != null)
				return false;
		} else if (!employee.equals(other.employee))
			return false;
		if (skillCategory == null) {
			if (other.skillCategory != null)
				return false;
		} else if (!skillCategory.equals(other.skillCategory))
			return false;
		return true;
	}

}
