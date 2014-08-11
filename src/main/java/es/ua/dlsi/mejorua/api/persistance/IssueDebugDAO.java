package es.ua.dlsi.mejorua.api.persistance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.ua.dlsi.mejorua.api.business.IssueBO;
import es.ua.dlsi.mejorua.api.transfer.IssueTO;
import es.ua.dlsi.mejorua.api.transfer.IssueTO.State;
import es.ua.dlsi.mejorua.api.util.JSON;

public class IssueDebugDAO implements IIssueDAO {

	private static long nextId = 1;
	private static HashMap<Long, IssueTO> issues = new HashMap<Long, IssueTO>();

	public IssueDebugDAO() {
		DEBUGprePopulate();
	}

	public List<IssueTO> getAll() {
		ArrayList<IssueTO> issuesList = new ArrayList<IssueTO>(issues.values());
		return issuesList;
	}

	public IssueTO get(long id) {

		return issues.get(id);
	}

	public long create(IssueTO issue) {

		long newId = nextId;

		nextId++;

		issue.setId(newId);

		issues.put(issue.getId(), issue);

		return newId;
	}

	public boolean update(IssueTO issue) {

		boolean isUpdated = false;

		if (issues.get(issue.getId()) != null) {
			issues.put(issue.getId(), issue);
			isUpdated = true;
		}

		return isUpdated;
	}

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// DEBUG
	//
	// /////////////////////////////////////////////////////////////////////////////////

	private void DEBUGprePopulate() {

		List<IssueTO> issues = IssueTO.DEBUGnewIssueList();

		for (IssueTO issue : issues) {
			create(issue);
		}
	}

}
