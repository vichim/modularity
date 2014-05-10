package ro.codecamp.modularity.taxonomy.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import ro.codecamp.modularity.infrastructure.entity.Identifiable;

@Entity
public class SkillCategory implements Identifiable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@ManyToOne
	private SkillCategory parent;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<SkillCategory> subCategories = new ArrayList<SkillCategory>();

	public SkillCategory() {
	}

	public SkillCategory(Long id, String name, SkillCategory parent) {
		this.id = id;
		this.name = name;
		this.parent = parent;
	}

	public SkillCategory(String name, SkillCategory parent) {
		this.name = name;
		this.parent = parent;
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

	public SkillCategory getParent() {
		return parent;
	}

	public void setParent(SkillCategory parent) {
		this.parent = parent;
	}

	public List<SkillCategory> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(List<SkillCategory> subCategories) {
		this.subCategories = subCategories;
	}
	
	public boolean isLeaf(){
		return this.subCategories.isEmpty();
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
		SkillCategory other = (SkillCategory) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
