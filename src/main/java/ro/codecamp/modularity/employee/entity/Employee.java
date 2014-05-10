package ro.codecamp.modularity.employee.entity;

import java.util.ArrayList;
import java.util.Date;
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
public class Employee implements Identifiable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private Date startDate;

	@ManyToOne
	private DeliveryUnit deliveryUnit;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<EmployeeSkill> skills = new ArrayList<>();

	public Employee() {
		super();
	}

	public Employee(String name, Date startDate, DeliveryUnit deliveryUnit) {
		this.name = name;
		this.startDate = startDate;
		this.deliveryUnit = deliveryUnit;
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

	public DeliveryUnit getDeliveryUnit() {
		return deliveryUnit;
	}

	public void setDeliveryUnit(DeliveryUnit deliveryUnit) {
		this.deliveryUnit = deliveryUnit;
	}

	public List<EmployeeSkill> getSkills() {
		return skills;
	}

	public void setSkills(List<EmployeeSkill> skills) {
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
		Employee other = (Employee) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
