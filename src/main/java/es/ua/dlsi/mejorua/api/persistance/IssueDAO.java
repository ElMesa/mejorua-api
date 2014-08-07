package es.ua.dlsi.mejorua.api.persistance;

import java.util.HashMap;

import es.ua.dlsi.mejorua.api.business.IssueBO;
import es.ua.dlsi.mejorua.api.transfer.IssueTO;
import es.ua.dlsi.mejorua.api.transfer.IssueTO.State;
import es.ua.dlsi.mejorua.api.util.JSON;

public class IssueDAO implements IIssueDAO {

	private static long nextId = 1;
	private static HashMap<Long, IssueTO> issues = new HashMap<Long, IssueTO>();

	public IssueDAO() {
		DEBUGprePopulate();
	}

	public HashMap<Long, IssueTO> getAll() {
		return issues;
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

	private void DEBUGprePopulate() {

		float[] aulario2Coords = new float[] { 38.384488f, -0.510120f };
		float[] bibliotecaGeneralCoords = new float[] { 38.383235f, -0.512158f };
		float[] eps1Coords = new float[] { 38.386755f, -0.511295f };

		create(DEBUGnewIssue(1, State.pending, aulario2Coords));
		create(DEBUGnewIssue(2, State.done, bibliotecaGeneralCoords));
		create(DEBUGnewIssue(3, State.inProgress, eps1Coords));

		System.out.println("IssueDAO.DEBUGprePopulate()\n"
				+ JSON.encode(issues));
	}

	private IssueTO DEBUGnewIssue(int id, State state, float[] coordinates) {
		IssueBO issueBO = new IssueBO();
		IssueTO issueTO = issueBO.getTO();
		
		issueTO.setId(id);
		issueTO.setState(state);
		issueTO.setTerm(id + " PrePopulated Term");
		issueTO.setAction(id + " PrePopulated Action");

		issueTO.setLatitude(coordinates[0]);
		issueTO.setLongitude(coordinates[1]);

		System.out.println("\n\n\nIssueDAO.DEBUGnewIssue()\n"
				+ JSON.encode(issueTO));

		return issueTO;
	}
}
