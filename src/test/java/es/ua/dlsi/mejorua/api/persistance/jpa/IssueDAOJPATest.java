package es.ua.dlsi.mejorua.api.persistance.jpa;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import es.ua.dlsi.mejorua.api.business.IssueBO;
import es.ua.dlsi.mejorua.api.transfer.IssueTO;

public class IssueDAOJPATest {

	@Test
	@Ignore
	public void testGetAll() {
		
		List<IssueTO> issues = null;
		IssueDAO dao = new IssueDAO(true);
		issues = dao.getAll();
		
		Assert.assertEquals(5, issues.size());
	}

	@Test
	public void testCreate() {
		
		IssueBO issueBO = new IssueBO();
		IssueTO issueTO = issueBO.getTO();
		IssueDAO dao = new IssueDAO(true);
		long issueId = -1;
		
		issueId = dao.create(issueTO);
		
		System.out.println("testCreate() issueId: " + issueId);
		Assert.assertTrue(issueId > 0);
		
		List<IssueTO> issues = null;
		issues = dao.getAll();
		System.out.println("testCreate() issues.size(): " + issues.size());
		Assert.assertEquals(1, issues.size());
		
	}

	@Test
	@Ignore
	public void testGet() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testUpdate() {
		fail("Not yet implemented");
	}

}
