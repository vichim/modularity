package ro.codecamp.modularity;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ro.codecamp.modularity.employee.contract.EmployeeContractTest;
import ro.codecamp.modularity.forecast.DistributionContractTest;
import ro.codecamp.modularity.forecast.ForecastContractTest;
import ro.codecamp.modularity.opportunity.contract.OpportunityContractTest;
import ro.codecamp.modularity.project.ProjectContractTest;
import ro.codecamp.modularity.taxonomy.SkillContractTest;

@RunWith(Suite.class)
@SuiteClasses({ EmployeeContractTest.class, SkillContractTest.class,
		OpportunityContractTest.class, ProjectContractTest.class,
		ForecastContractTest.class, DistributionContractTest.class })
public class RestTestSuite {

}
