package es.ua.dlsi.mejorua.api.persistance;

import es.ua.dlsi.mejorua.api.persistance.jdbc.mysql.IssueDAO;

//Singleton DAO factory
public class DAOFactory {

	public enum DAOType {DEBUG, JDBCMYSQL, JPA};
	
	private static DAOType activeDAOType;
	private static IIssueDAO issueDAO;
	
	static {
		activeDAOType = DAOType.JDBCMYSQL;
		issueDAO = null;
	}
	
	public static void setDAOType(DAOType type) {
		activeDAOType = type;
		issueDAO = null;
	}
	
	public static IIssueDAO getIssueDAO() {
		if(issueDAO == null) issueDAO = newIssueDAO();
		return issueDAO;
	}
	
	public static IIssueDAO newIssueDAO() {
		IIssueDAO issueDAO = null;
		
		switch (activeDAOType) {
		case DEBUG:
			issueDAO = new es.ua.dlsi.mejorua.api.persistance.debug.IssueDAO();
			break;
		case JDBCMYSQL:
			issueDAO = new es.ua.dlsi.mejorua.api.persistance.jdbc.mysql.IssueDAO();
			break;
		case JPA:
			issueDAO = new es.ua.dlsi.mejorua.api.persistance.jpa.IssueDAO();
			break;
		}
		
		return issueDAO;
	}
	
}
