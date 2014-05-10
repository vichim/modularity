package ro.codecamp.modularity.forecast.skillcoverage_api;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ro.codecamp.modularity.taxonomy.entity.SkillCategory;

public class EmployeeEntries {

	private Date date;

	// (skill, no entries)
	protected Map<SkillCategory, Integer> skillEntries = new HashMap<>();

	public EmployeeEntries(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Map<SkillCategory, Integer> getSkillEntries() {
		return skillEntries;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
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
		EmployeeEntries other = (EmployeeEntries) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		return true;
	}

}
