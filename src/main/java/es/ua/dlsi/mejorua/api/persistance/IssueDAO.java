package es.ua.dlsi.mejorua.api.persistance;

import java.util.HashMap;

import es.ua.dlsi.mejorua.api.business.IssueBO;
import es.ua.dlsi.mejorua.api.business.IssueBO.State;
import es.ua.dlsi.mejorua.api.util.JSON;

public class IssueDAO {
	
	private static long nextId = 1;
	private static HashMap<Long, IssueBO> issues = new HashMap<Long, IssueBO>();
	
	static { DEBUGprePopulate(); }
	
	public static HashMap<Long, IssueBO> getAll() {
		return issues;
	}
	
	public static IssueBO get(long id) {

		return issues.get(id);
	}

	public static long add(IssueBO issue) {

		long newId = nextId;
		
		nextId++;
		
		issue.setId(newId);

		issues.put(issue.getId(), issue);
		
		return newId;
	}

	public static boolean update(IssueBO issue) {

		boolean isUpdated = false;
		
		if (issues.get(issue.getId()) != null) {
			issues.put(issue.getId(), issue);
			isUpdated = true;
		}
		
		return isUpdated;
	}

	private static void DEBUGprePopulate() {
		
		float[] aulario2Coords = new float[]{38.384488f,-0.510120f};
		float[] bibliotecaGeneralCoords = new float[]{38.383235f,-0.512158f};
		float[] eps1Coords = new float[]{38.386755f,-0.511295f};
		
		add(DEBUGnewIssue(1, "pending", aulario2Coords));
		add(DEBUGnewIssue(2, "done", bibliotecaGeneralCoords));
		add(DEBUGnewIssue(3, "inProgress", eps1Coords));
		
		System.out.println("IssueDAO.DEBUGprePopulate()\n" + JSON.encode(issues));
	}
	
	private static IssueBO DEBUGnewIssue(int id, String status, float[] coordinates) {
		IssueBO debugIssue = new IssueBO();
		debugIssue.setId(id);
		debugIssue.setState(State.pending);
		debugIssue.setTerm(id + " PrePopulated Term");
		debugIssue.setAction(id + " PrePopulated Action");
		
		debugIssue.setLatitude(coordinates[0]);
		debugIssue.setLongitude(coordinates[1]);
		
		System.out.println("\n\n\nIssueDAO.DEBUGnewIssue()\n" + JSON.encode(debugIssue));
		
		return debugIssue;
	}
}
