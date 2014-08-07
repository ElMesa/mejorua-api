package es.ua.dlsi.mejorua.api.persistance;

import java.util.HashMap;

import es.ua.dlsi.mejorua.api.business.IssueBO;
import es.ua.dlsi.mejorua.api.business.IssueBO.State;
import es.ua.dlsi.mejorua.api.util.JSON;

public class IssueDAO implements IIssueDAO {

	private static long nextId = 1;
	private static HashMap<Long, IssueBO> issues = new HashMap<Long, IssueBO>();

	public IssueDAO() {
		DEBUGprePopulate();
	}

	public HashMap<Long, IssueBO> getAll() {
		return issues;
	}

	public IssueBO get(long id) {

		return issues.get(id);
	}

	public long create(IssueBO issue) {

		long newId = nextId;

		nextId++;

		issue.setId(newId);

		issues.put(issue.getId(), issue);

		return newId;
	}

	public boolean update(IssueBO issue) {

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

	private IssueBO DEBUGnewIssue(int id, State state, float[] coordinates) {
		IssueBO debugIssue = new IssueBO();
		debugIssue.setId(id);
		debugIssue.setState(state);
		debugIssue.setTerm(id + " PrePopulated Term");
		debugIssue.setAction(id + " PrePopulated Action");

		debugIssue.setLatitude(coordinates[0]);
		debugIssue.setLongitude(coordinates[1]);

		System.out.println("\n\n\nIssueDAO.DEBUGnewIssue()\n"
				+ JSON.encode(debugIssue));

		return debugIssue;
	}
}
