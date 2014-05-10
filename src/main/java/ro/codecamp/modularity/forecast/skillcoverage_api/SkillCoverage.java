package ro.codecamp.modularity.forecast.skillcoverage_api;

public class SkillCoverage {

	private int actual;
	
	private int expected;

	public SkillCoverage(int actual, int expected) {
		this.actual = actual;
		this.expected = expected;
	}

	public int getActual() {
		return actual;
	}

	public void setActual(int actual) {
		this.actual = actual;
	}

	public int getExpected() {
		return expected;
	}

	public void setExpected(int expected) {
		this.expected = expected;
	}

	public String getCoverage(){
		return actual + "/" + expected;
	}
	
}
