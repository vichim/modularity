package ro.codecamp.modularity.opportunity.boundary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import ro.codecamp.modularity.infrastructure.control.SearchResults;
import ro.codecamp.modularity.infrastructure.rest.CRUDHalResource;
import ro.codecamp.modularity.opportunity.control.OpportunityStore;
import ro.codecamp.modularity.opportunity.entity.Opportunity;

import com.theoryinpractise.halbuilder.api.ReadableRepresentation;
import com.theoryinpractise.halbuilder.api.Representation;

@Stateless
@Path("/opportunities")
public class OpportunityController extends CRUDHalResource {

	@Inject
	private OpportunityStore oppStore;

	@Inject
	private OppRepresentationBuilder reprBuilder;

	@Override
	protected String getCollectionPath() {
		return reprBuilder.getPath();
	}

	@GET
	public Representation search() {
		SearchResults<Opportunity> searchResults = oppStore.search();

		Representation representation = halFactory.newRepresentation("/"
				+ getCollectionPath());
		for (Opportunity bean : searchResults.getItems()) {
			representation.withRepresentation("items",
					reprBuilder.createRepresentation(bean));
		}

		return representation;
	}

	@Override
	protected Representation doFindById(Long id) {
		Opportunity opportunity = oppStore.findById(Opportunity.class, id);
		return reprBuilder.createRepresentation(opportunity);
	}

	@Override
	protected Long doCreate(ReadableRepresentation representation) {
		Opportunity opp = oppStore.persist(reprBuilder
				.createEntity(representation));
		return opp.getId();
	}

	@Override
	protected Representation doUpdate(ReadableRepresentation representation,
			Long id) {
		Opportunity opportunity = reprBuilder.createEntity(representation);
		opportunity.setId(id);
		oppStore.update(opportunity);
		return reprBuilder.createRepresentation(opportunity);
	}

	@Override
	protected boolean doDelete(Long id) {
		return oppStore.delete(Opportunity.class, id);
	}

}
