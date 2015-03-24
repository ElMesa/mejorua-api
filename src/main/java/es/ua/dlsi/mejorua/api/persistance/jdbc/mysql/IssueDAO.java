package es.ua.dlsi.mejorua.api.persistance.jdbc.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import es.ua.dlsi.mejorua.api.business.IssueEventBO;
import es.ua.dlsi.mejorua.api.persistance.IIssueDAO;
import es.ua.dlsi.mejorua.api.transfer.IssueTO;
import es.ua.dlsi.mejorua.api.util.JSON;

public class IssueDAO implements IIssueDAO {

	private static Logger logger = Logger
			.getLogger("es.ua.dlsi.mejorua.api.persistance.jdbc.mysql.IssueDAO");

	// Read all
	public List<IssueTO> getAll() {
		IssueEventDAO eventDAO = null;
		ArrayList<IssueTO> issues = null;;
		IssueTO issue = null;
		List<IssueEventBO> events = null;
		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		boolean isError = false;

		
		issues = new ArrayList<IssueTO>();
		eventDAO = new IssueEventDAO();
		con = DAO.getConnection();

		String query = "SELECT * FROM ISSUETO;";

		try {
			statement = con.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {

				issue = newIssueFromResultset(rs);

				if (issue != null) {
					issues.add(issue);
				} else {
					logger.info("IssueDAO - Could not extract the issue from DB");
					isError = true;
					break;
				}
			}
		} catch (SQLException e) {
			isError = true;
			throw new RuntimeException(e.toString(), e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					isError = true;
					throw new RuntimeException(e.toString(), e);
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					isError = true;
					throw new RuntimeException(e.toString(), e);
				}
			}
			if(con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					isError = true;
					throw new RuntimeException(e.toString(), e);
				}
			}
		}

		if (isError) {
			issues = null;
		}

		// logger.info("IssueJDBCMySQLDAO.getAll() issues:" +
		// JSON.encode(issues));

		return issues;
	}

	// Create
	public long create(IssueTO issue) {
		long newIssueId = -1;
		IssueEventDAO eventDAO = null;
		Connection con = null;
		PreparedStatement statement = null;
		PreparedStatement subStatement = null;
		ResultSet generatedKeys = null;

		con = DAO.getConnection();
		String SQL_CREATE = "INSERT INTO `ISSUETO` (`ACTION`, `CREATIONDATE`, `LASTMODIFIEDDATE`, `LATITUDE`, `LONGITUDE`, `STATE`, `TERM`, `IDSIGUA`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			// TODO Substitute with named parameters preparedStatement:
			// http://stackoverflow.com/questions/2309970/named-parameters-in-jdbc
			statement = con.prepareStatement(SQL_CREATE,
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, issue.getAction());
			statement.setLong(2, issue.getCreationDate());
			statement.setLong(3, issue.getLastModifiedDate());
			statement.setDouble(4, issue.getLatitude());
			statement.setDouble(5, issue.getLongitude());
			statement.setInt(6, issue.getState().getId());
			statement.setString(7, issue.getTerm());
			statement.setString(8, issue.getIdSIGUA());

			int affectedRows = statement.executeUpdate();
			if (affectedRows > 0) {
				generatedKeys = statement.getGeneratedKeys();

				if (generatedKeys.next()) {

					newIssueId = generatedKeys.getLong(1);
					List<IssueEventBO> events = issue.getEvents();
					eventDAO = new IssueEventDAO();

					for (IssueEventBO event : events) {
						eventDAO.create(newIssueId, event);
					}
				}
			}

		} catch (SQLException e) {
			throw new RuntimeException(e.toString(), e);
		} finally {
			if (generatedKeys != null)
				try {
					generatedKeys.close();
				} catch (SQLException e) {
					throw new RuntimeException(e.toString(), e);
				}
			if (statement != null)
				try {
					statement.close();
				} catch (SQLException e) {
					throw new RuntimeException(e.toString(), e);
				}
			if (subStatement != null)
				try {
					subStatement.close();
				} catch (SQLException e) {
					throw new RuntimeException(e.toString(), e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException logOrIgnore) {
				}
		}

		return newIssueId;
	}

	// Read
	public IssueTO get(long id) {
		IssueTO issue = null;
		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		boolean isError = false;

		
		con = DAO.getConnection();
		String query = "SELECT * FROM ISSUETO WHERE ID = " + id;

		try {
			statement = con.createStatement();
			rs = statement.executeQuery(query);
			if (rs.next()) {

				issue = newIssueFromResultset(rs);

				if (issue == null) {
					logger.info("IssueDAO - Could not extract the issue from DB");
					isError = true;
				}
			} else {
				logger.info("IssueDAO - Issue not found");
				isError = true;
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
			if(con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					isError = true;
					throw new RuntimeException(e.toString(), e);
				}
			}
		}

		if (isError) {
			issue = null;
		}

		return issue;
	}

	// Update
	//TODO Implement a better partial update. Now IssueResource gets from DB the persisted issue, changes partially, and then makes a full update. It would be better to just "SQL UPDATE SET" the changed atributes to avoid the first BD GET.
	public IssueTO update(IssueTO issue) {

		IssueEventDAO eventDAO = null;
		boolean isUpdated = true;
		Connection con = null;
		PreparedStatement statement = null;
		int affectedRows = 0;

		
		eventDAO = new IssueEventDAO();
		long issueId = issue.getId();
		con = DAO.getConnection();
		
		String SQL_UPDATE = "UPDATE `ISSUETO` "
				+ "SET `ACTION`=?, `CREATIONDATE`=?, `LASTMODIFIEDDATE`=?, `LATITUDE`=?, `LONGITUDE`=?, `STATE`=?, `TERM`=? "
				+ "WHERE ID = ?";

		try {
			// TODO Substitute with named parameters preparedStatement:
			// http://stackoverflow.com/questions/2309970/named-parameters-in-jdbc
			statement = con.prepareStatement(SQL_UPDATE);
			statement.setString(1, issue.getAction());
			statement.setLong(2, issue.getCreationDate());
			statement.setLong(3, issue.getLastModifiedDate());
			statement.setDouble(4, issue.getLatitude());
			statement.setDouble(5, issue.getLongitude());
			statement.setInt(6, issue.getState().getId());
			statement.setString(7, issue.getTerm());
			statement.setLong(8, issueId);

			//logger.info(statement.toString());

			affectedRows = statement.executeUpdate();
			if (affectedRows > 0) {

				List<IssueEventBO> events = issue.getEvents();

				isUpdated = eventDAO.updateListFromIssue(issueId, events);

				if (!isUpdated)
					logger.info("IssueDAO - List IssueEventBO not updated");

			} else {
				logger.info("IssueDAO - IssueTO not updated");
				isUpdated = false;
			}

		} catch (SQLException e) {
			// TODO Exception - Treat
			throw new RuntimeException(e.toString(), e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Exception - Treat
					throw new RuntimeException(e.toString(), e);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Exception - Treat
					throw new RuntimeException(e.toString(), e);
				}
			}
		}

		return issue;
	}

	private IssueTO newIssueFromResultset(ResultSet rs) throws SQLException {
		IssueEventDAO eventDAO = new IssueEventDAO();
		IssueTO issue = new IssueTO();
		List<IssueEventBO> events = null;

		issue.setId(rs.getLong("ID"));
		issue.setAction(rs.getString("ACTION"));
		issue.setTerm(rs.getString("TERM"));
		issue.setState(IssueTO.State.getType(rs.getInt("STATE")));
		issue.setCreationDate(rs.getLong("CREATIONDATE"));
		issue.setLastModifiedDate(rs.getLong("LASTMODIFIEDDATE"));
		issue.setLatitude(rs.getDouble("LATITUDE"));
		issue.setLongitude(rs.getDouble("LONGITUDE"));
		issue.setIdSIGUA(rs.getString("IDSIGUA"));

		events = eventDAO.getAllFromIssue(rs.getLong("ID"));

		if (events == null) {
			logger.info("IssueDAO - Events could not be retrieved");
			issue = null;
		} else {
			issue.setEvents(events);
		}

		return issue;
	}

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// DEBUG
	//
	// /////////////////////////////////////////////////////////////////////////////////

	public static void DEBUGprePopulate() {

		IssueDAO dao = new IssueDAO();

		DEBUGdeleteAll();

		List<IssueTO> issues = IssueTO.DEBUGnewIssueList();

		for (IssueTO issue : issues) {
			dao.create(issue);
		}
	}

	public static void DEBUGdeleteAll() {

		Connection con = DAO.getConnection();
		Statement statement = null;

		String query = "DELETE FROM ISSUEEVENTBO;";

		try {
			statement = con.createStatement();
			statement.executeUpdate(query);
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

		query = "DELETE FROM ISSUETO;";

		try {
			statement = con.createStatement();
			statement.executeUpdate(query);
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
	}

}
