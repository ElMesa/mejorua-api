package es.ua.dlsi.mejorua.api.persistance.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import es.ua.dlsi.mejorua.api.persistance.IIssueDAO;
import es.ua.dlsi.mejorua.api.transfer.IssueTO;

public class IssueDAO implements IIssueDAO {

	private static final String PERSISTANCEUNIT = "es.ua.dlsi.mejorua.api.persistanceUnit.mysql";
	private static final String PERSISTANCEUNIT_TEST = "es.ua.dlsi.mejorua.api.persistanceUnit.h2";
	
	private EntityManagerFactory emf;
	private EntityManager em;
	
	public IssueDAO() {
		this(false); 
	}
	
	public IssueDAO(Boolean isTestMode) {
		String persistanceUnit;
		if(isTestMode) {
			persistanceUnit = PERSISTANCEUNIT_TEST;
		} else {
			persistanceUnit = PERSISTANCEUNIT;
		}
		emf = Persistence.createEntityManagerFactory(persistanceUnit);
		em = emf.createEntityManager();
	}
	
	// TODO - IMPLEMENNT
	public List<IssueTO> getAll() {
		
		Query query = em.createQuery("SELECT e FROM IssueTO e");
		List<IssueTO> allIssues = (List<IssueTO>) query.getResultList();
		
		//Detach objets from entity manager
		//em.clear();
		
		return allIssues;
	}

	public long create(IssueTO issue) {

		long issueId = -1;
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		em.persist(issue);

		tx.commit();
		
		//Detach objets from entity manager
		//em.clear();
		
		issueId = issue.getId();

		return issueId;
	}

	// TODO - IMPLEMENNT
	public IssueTO get(long id) {
		
		IssueTO issue = em.find(IssueTO.class, id);
		
		return issue;
	}

	// TODO - IMPLEMENNT
	public IssueTO update(IssueTO issue) {
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		em.merge(issue);

		tx.commit();
		
		//Detach objets from entity manager
		//em.clear();
		
		return issue;
	}
}
