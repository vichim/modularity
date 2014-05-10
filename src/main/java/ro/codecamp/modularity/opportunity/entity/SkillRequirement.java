package ro.codecamp.modularity.opportunity.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import ro.codecamp.modularity.taxonomy.entity.SkillCategory;
import ro.codecamp.modularity.taxonomy.entity.SkillLevel;

@Entity
@IdClass(SkillRequirementId.class)
public class SkillRequirement {

	@Id
	@ManyToOne
	private Opportunity opportunity;

	@Id
	@ManyToOne
	private SkillCategory skillCategory;

	// JPA hack
	@Id
	private Integer levelId;
	
	@Enumerated(EnumType.STRING)
	private SkillLevel level;

	private int headCount;

	public SkillRequirement() {
	}
	
	public SkillRequirement(SkillCategory skillCategory, SkillLevel level,
			int headCount) {
		this.skillCategory = skillCategory;
		setLevel(level);
		this.headCount = headCount;
	}

	public SkillRequirement(Opportunity opportunity,
			SkillCategory skillCategory, SkillLevel level, int headCount) {
		this.opportunity = opportunity;
		this.skillCategory = skillCategory;
		setLevel(level);
		this.headCount = headCount;
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
		this.levelId = level.ordinal();
	}

	public int getHeadCount() {
		return headCount;
	}

	public void setHeadCount(int headCount) {
		this.headCount = headCount;
	}

	public Opportunity getOpportunity() {
		return opportunity;
	}

	public void setOpportunity(Opportunity opportunity) {
		this.opportunity = opportunity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((level == null) ? 0 : level.hashCode());
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
		SkillRequirement other = (SkillRequirement) obj;
		if (level != other.level)
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
