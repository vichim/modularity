package ro.codecamp.modularity.opportunity.entity;

import java.io.Serializable;

public class SkillRequirementId implements Serializable {

	Long opportunity;
	
	Long skillCategory;
	
	Integer levelId;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((levelId == null) ? 0 : levelId.hashCode());
		result = prime * result
				+ ((opportunity == null) ? 0 : opportunity.hashCode());
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
		SkillRequirementId other = (SkillRequirementId) obj;
		if (levelId == null) {
			if (other.levelId != null)
				return false;
		} else if (!levelId.equals(other.levelId))
			return false;
		if (opportunity == null) {
			if (other.opportunity != null)
				return false;
		} else if (!opportunity.equals(other.opportunity))
			return false;
		if (skillCategory == null) {
			if (other.skillCategory != null)
				return false;
		} else if (!skillCategory.equals(other.skillCategory))
			return false;
		return true;
	}

}
