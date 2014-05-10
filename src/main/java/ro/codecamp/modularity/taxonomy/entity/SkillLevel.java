package ro.codecamp.modularity.taxonomy.entity;

public enum SkillLevel {

	AwareOf("Aware of"), FamiliarWith("Familiar with"), ProficientIn(
			"Proficient in");

	private String description;

	private SkillLevel(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public static SkillLevel fromDescription(String description) {
		for (SkillLevel bean : values()) {
			if (bean.getDescription().equals(description)) {
				return bean;
			}
		}
		return null;
	}

}
