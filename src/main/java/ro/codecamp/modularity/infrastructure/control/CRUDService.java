package ro.codecamp.modularity.infrastructure.control;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import ro.codecamp.modularity.infrastructure.entity.Identifiable;

public abstract class CRUDService<T extends Identifiable> {

	@PersistenceContext
	protected EntityManager em;

	public T persist(T item) {
		em.persist(item);
		em.flush();
		return item;
	}

	public T findById(Class<T> type, Long id) {
		return em.find(type, id);
	}

	public T update(T item) {
		return em.merge(item);
	}

	/**
	 * @return true if the entity is removed
	 */
	public boolean delete(Class<T> type, Long id) {
		Object ref = em.find(type, id);
		if (ref == null) {
			return false;
		}
		em.remove(ref);
		return true;
	}

	public List<T> findAll(Class<T> type) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(type);
		cq.from(type);
		return em.createQuery(cq).getResultList();
	}

}
