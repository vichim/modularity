package ro.codecamp.modularity.opportunity.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import ro.codecamp.modularity.ServiceBaseTest;
import ro.codecamp.modularity.opportunity.OpportunityTestUtil;
import ro.codecamp.modularity.opportunity.control.OpportunityStore;
import ro.codecamp.modularity.taxonomy.SkillTestUtil;
import ro.codecamp.modularity.taxonomy.control.SkillStore;
import ro.codecamp.modularity.taxonomy.entity.SkillCategory;

@RunWith(Arquillian.class)
public class OpportunitySkillTest extends ServiceBaseTest {

	@Inject
	private OpportunityStore oppStore;

	@Inject
	private SkillStore skillStore;

	@Inject
	private OpportunityTestUtil oppTestUtil;

	@Inject
	private SkillTestUtil skillTestUtil;

	private static final int DEFAULT_HEAD_COUNT = 10;

	@Test
	public void countNeededSkills() {
		skillTestUtil.createTaxonomy();

		int noOpps = 5;

		SkillCategory root = skillStore.findRoot();
		oppTestUtil.createOpps(
				noOpps,
				new Date(),
				DEFAULT_HEAD_COUNT,
				root.getSubCategories().toArray(
						new SkillCategory[root.getSubCategories().size()]));

		int actualHeadCount = oppStore.getTotalHeadCountBySkill(root
				.getSubCategories().get(0));

		assertEquals(noOpps * DEFAULT_HEAD_COUNT, actualHeadCount);
	}

}
