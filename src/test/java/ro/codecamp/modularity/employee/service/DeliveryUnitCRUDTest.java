package ro.codecamp.modularity.employee.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import ro.codecamp.modularity.ServiceBaseTest;
import ro.codecamp.modularity.employee.control.DeliveryUnitStore;
import ro.codecamp.modularity.employee.entity.DeliveryUnit;

@RunWith(Arquillian.class)
public class DeliveryUnitCRUDTest extends ServiceBaseTest {

	@Inject
	private DeliveryUnitStore duStore;

	private static final String DU_CODE = "ISD";
	private static final String DU_NAME = "IASI";

	@Test
	@InSequence(1)
	public void create() {
		DeliveryUnit storedEntity = duStore.persist(new DeliveryUnit(DU_CODE,
				DU_NAME));
		assertNotNull(storedEntity.getId());

		DeliveryUnit foundEntity = duStore.findById(DeliveryUnit.class,
				storedEntity.getId());
		assertNotNull(foundEntity);
	}

	@Test
	@InSequence(2)
	public void findByCode() {
		DeliveryUnit foundEntity = duStore.findByCode(DU_CODE);
		assertNotNull(foundEntity);
	}

	@Test
	@InSequence(3)
	public void update() {
		DeliveryUnit du = duStore.findByCode(DU_CODE);
		String updatedValue = "Iasi";

		du.setName(updatedValue);
		DeliveryUnit updatedDu = duStore.update(du);

		assertEquals(updatedValue, updatedDu.getName());
	}

	@Test
	@InSequence(4)
	public void delete() {
		DeliveryUnit du = duStore.findByCode(DU_CODE);
		boolean deleted = duStore.delete(DeliveryUnit.class, du.getId());
		assertTrue(deleted);
	}

}
