package ro.codecamp.modularity;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ro.codecamp.modularity.employee.service.DeliveryUnitCRUDTest;
import ro.codecamp.modularity.employee.service.EmployeeCRUDTest;
import ro.codecamp.modularity.employee.service.EmployeeSkillTest;
import ro.codecamp.modularity.forecast.DistributionServiceTest;
import ro.codecamp.modularity.forecast.SkillCoverageDefaultImplTest;
import ro.codecamp.modularity.forecast.SkillCoverageEnhancedImplTest;
import ro.codecamp.modularity.opportunity.service.OpportunityCRUDTest;
import ro.codecamp.modularity.opportunity.service.OpportunitySkillTest;
import ro.codecamp.modularity.project.ProjectCRUDTest;
import ro.codecamp.modularity.taxonomy.SkillServiceTest;

@RunWith(Suite.class)
@SuiteClasses({ DeliveryUnitCRUDTest.class, SkillServiceTest.class,
		EmployeeCRUDTest.class, EmployeeSkillTest.class, ProjectCRUDTest.class,
		OpportunityCRUDTest.class, OpportunitySkillTest.class,
		SkillCoverageDefaultImplTest.class, SkillCoverageEnhancedImplTest.class,
		DistributionServiceTest.class })
public class ServiceTestSuite {

}
