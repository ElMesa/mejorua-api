package es.ua.dlsi.mejorua.api.persistance;

import java.util.ArrayList;
import java.util.HashMap;

import es.ua.dlsi.mejorua.api.business.IssueBO;
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
		for(int i = 1; i < 10; i++) {
			add(DEBUGnewIssue(i));
		}
		
		System.out.println("IssueDAO.DEBUGprePopulate()\n" + JSON.encode(issues));
	}
	
	private static IssueBO DEBUGnewIssue(int id) {
		IssueBO debugIssue = new IssueBO();
		debugIssue.setId(id);
		debugIssue.setLatitude(id);
		debugIssue.setLongitude(id);
		debugIssue.setTerm(id + " PrePopulated Term");
		debugIssue.setAction(id + " PrePopulated Action");
		
		System.out.println("\n\n\nIssueDAO.DEBUGnewIssue()\n" + JSON.encode(debugIssue));
		
		return debugIssue;
	}
}
