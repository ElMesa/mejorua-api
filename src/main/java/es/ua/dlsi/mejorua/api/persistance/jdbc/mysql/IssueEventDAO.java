package es.ua.dlsi.mejorua.api.persistance.jdbc.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.datanucleus.enhancer.methods.IsPersistent;

import es.ua.dlsi.mejorua.api.business.IssueEventBO;

public class IssueEventDAO {

	private static Logger logger = Logger.getLogger("es.ua.dlsi.mejorua.api.persistance.jdbc.mysql.IssueEventDAO");

	public List<IssueEventBO> getAllFromIssue(long issueId) {
		ArrayList<IssueEventBO> events = null;
		IssueEventBO event = null;

		Connection con = null;
		String SQL_CREATE = "";
		Statement statement = null;
		ResultSet rs = null;
		String SQL_SELECT;

		events = new ArrayList<IssueEventBO>();
		
		con = DAO.getConnection();

		SQL_SELECT = "SELECT * FROM ISSUEEVENTBO WHERE ISSUEID = " + issueId;

		try {
			statement = con.createStatement();
			rs = statement.executeQuery(SQL_SELECT);
			while (rs.next()) {
				event = new IssueEventBO();
				event.setId(rs.getLong("ID"));
				event.setType(IssueEventBO.eventType.getType(rs
						.getInt("TYPE")));
				event.setDate(rs.getLong("DATE"));
				event.setIssueId(rs.getLong("ISSUEID"));

				events.add(event);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e.toString(), e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					throw new RuntimeException(e.toString(), e);
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException(e.toString(), e);
				}
			}
		}
		return events;
	}

	// Create
	public long create(long issueId, IssueEventBO event) {
		boolean isCreated = true;
		long eventId = -1;
		
		Connection con = null;
		String SQL_CREATE = "";
		PreparedStatement statement = null;
		ResultSet generatedKeys = null;
		int rowsAffected = -1;

		con = DAO.getConnection();

		SQL_CREATE = "INSERT INTO `mejorua`.`ISSUEEVENTBO` (`TYPE`, `DATE`, `ISSUEID`) VALUES (?, ?, ?)";
		try {
			statement = con.prepareStatement(SQL_CREATE,
					Statement.RETURN_GENERATED_KEYS);

			statement.setInt(1, event.getType().getId());
			statement.setLong(2, event.getDate());
			statement.setLong(3, issueId);
			rowsAffected = statement.executeUpdate();
			
			if(rowsAffected > 0) {
					generatedKeys = statement.getGeneratedKeys();
					if (generatedKeys.next()) {
						eventId = generatedKeys.getLong(1);
					}
			} else {
				logger.info("IssueEventDAO - Event not created. SQL affected 0 rows");
				isCreated = false;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e.toString(), e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException(e.toString(), e);
				}
			}
		}
		
		if(!isCreated) eventId = -1;
		
		return eventId;
	}

	//No real update. Checks for new events and creates them
	public boolean updateListFromIssue(long issueId, List<IssueEventBO> events) {
		boolean isUpdated = true;
		long lastEventDate = -1;
		List<IssueEventBO> eventsToCreate = null;
		long eventId = -1;

		
		List<IssueEventBO> alreadyPersistedEvents = getAllFromIssue(issueId);
		
		lastEventDate = IssueEventBO.lastEventDate(alreadyPersistedEvents);
		eventsToCreate = IssueEventBO.eventsAfterDate(lastEventDate, events);
		
		for(IssueEventBO event : eventsToCreate) {
			eventId = create(issueId, event);
			if(eventId < 0) {
				logger.info("IssueEventDAO - Event not created");
				isUpdated = false;
				break;
			}
		}
				
		return isUpdated;
	}
	
}