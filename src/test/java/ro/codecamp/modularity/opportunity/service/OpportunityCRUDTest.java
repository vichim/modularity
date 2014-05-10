package ro.codecamp.modularity.opportunity.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import ro.codecamp.modularity.ServiceBaseTest;
import ro.codecamp.modularity.infrastructure.control.SearchResults;
import ro.codecamp.modularity.opportunity.control.OpportunityStore;
import ro.codecamp.modularity.opportunity.entity.Opportunity;
import ro.codecamp.modularity.opportunity.entity.SkillRequirement;
import ro.codecamp.modularity.taxonomy.SkillTestUtil;
import ro.codecamp.modularity.taxonomy.control.SkillStore;
import ro.codecamp.modularity.taxonomy.entity.SkillCategory;
import ro.codecamp.modularity.taxonomy.entity.SkillLevel;

@RunWith(Arquillian.class)
public class OpportunityCRUDTest extends ServiceBaseTest {

	@Inject
	private OpportunityStore oppStore;

	@Inject
	private SkillStore skillStore;

	@Inject
	private SkillTestUtil skillTestUtil;

	@Test
	@InSequence(1)
	public void create() {
		skillTestUtil.createTaxonomy();

		Opportunity opportunity = new Opportunity("Opportunity Test",
				new Date());
		SkillCategory root = skillStore.findRoot();
		List<SkillRequirement> req = new ArrayList<SkillRequirement>();
		req.add(new SkillRequirement(opportunity, root.getSubCategories()
				.get(0), SkillLevel.FamiliarWith, 10));
		opportunity.getSkills().addAll(req);

		Opportunity storedEntity = oppStore.persist(opportunity);
		assertNotNull(storedEntity.getId());

		Opportunity foundEntity = oppStore.findById(Opportunity.class,
				storedEntity.getId());
		assertNotNull(foundEntity);
	}

	@Test
	@InSequence(2)
	public void search() {
		SearchResults<Opportunity> searchResults = oppStore.search();
		assertEquals(1, searchResults.getTotalItems());
		assertEquals(1, searchResults.getItems().size());
	}

	@Test
	@InSequence(3)
	public void update() {
		Opportunity opp = oppStore.search().getItems().get(0);
		String updatedValue = "Opp Updated Name";
		opp.setName(updatedValue);

		Opportunity updatedOpp = oppStore.update(opp);

		assertEquals(updatedValue, updatedOpp.getName());
	}

	@Test
	@InSequence(4)
	public void delete() {
		Opportunity opp = oppStore.search().getItems().get(0);
		boolean deleted = oppStore.delete(Opportunity.class, opp.getId());
		assertTrue(deleted);
	}

}
