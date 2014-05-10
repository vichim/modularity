package ro.codecamp.modularity.employee.control;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import ro.codecamp.modularity.employee.entity.DeliveryUnit;
import ro.codecamp.modularity.infrastructure.control.CRUDService;

@Stateless
public class DeliveryUnitStore extends CRUDService<DeliveryUnit> {

	public DeliveryUnit findByCode(String code) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<DeliveryUnit> query = cb.createQuery(DeliveryUnit.class);
		Root<DeliveryUnit> root = query.from(DeliveryUnit.class);

		query.where(cb.equal(root.get("code"), code));
		List<DeliveryUnit> resultList = em.createQuery(query).getResultList();
		if (resultList.isEmpty()) {
			return null;
		}
		return resultList.get(0);
	}

}
