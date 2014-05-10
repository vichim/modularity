package ro.codecamp.modularity.opportunity.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import ro.codecamp.modularity.infrastructure.entity.Identifiable;

@Entity
public class Opportunity implements Identifiable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private Date startDate;

	@OneToMany(mappedBy = "opportunity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<SkillRequirement> skills = new ArrayList<>();

	public Opportunity() {
	}

	public Opportunity(String name, Date startDate) {
		this.name = name;
		this.startDate = startDate;
	}

	public Opportunity(String name, Date startDate,
			List<SkillRequirement> skills) {
		this.name = name;
		this.startDate = startDate;
		for (SkillRequirement skill : skills) {
			skill.setOpportunity(this);
		}
		this.skills = skills;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public List<SkillRequirement> getSkills() {
		return skills;
	}

	public void setSkills(List<SkillRequirement> skills) {
		this.skills = skills;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Opportunity other = (Opportunity) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
