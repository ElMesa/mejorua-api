package es.ua.dlsi.mejorua.api.persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import es.ua.dlsi.mejorua.api.business.IssueEventBO;
import es.ua.dlsi.mejorua.api.transfer.IssueTO;
import es.ua.dlsi.mejorua.api.util.JSON;

public class IssueJDBCMySQLDAO implements IIssueDAO {

	private static Logger logger = Logger.getLogger("IssueJDBCMySQLDAO");

	public IssueJDBCMySQLDAO() {
		DEBUGprePopulate();
	}

	private static Connection getConnection() {

		// String database = "mejorua";
		String database = "mejorua";

		String user = "mejorua";
		String password = "";

		String dbms = "mysql";
		String host = "localhost";
		String port = "3306";

		Connection conn = null;

		// Oracle way results in "SQLException: No suitable driver", problem
		// asociated with lack of driver (is at pom.xml and deployed at WEB-INF
		// libs) or malformed URL
		// * Oracle howto :
		// http://docs.oracle.com/javase/tutorial/jdbc/basics/processingsqlstatements.html
		// * "SQLException: No suitable driver" sources reference:
		// http://stackoverflow.com/questions/2839321/java-connectivity-with-mysql/2840358#2840358
		/*
		 * Properties connectionProps = new Properties();
		 * connectionProps.put("user", user); connectionProps.put("password",
		 * password);
		 * 
		 * try { conn = DriverManager.getConnection("jdbc:" + dbms + "://" +
		 * host + ":" + port + "/" + database, connectionProps); } catch
		 * (SQLException e) { // TODO Auto-generated catch block
		 * logger.info(e.toString()); }
		 * 
		 * if(conn != null) logger.info("Connected to database");
		 */

		// http://stackoverflow.com/questions/2839321/java-connectivity-with-mysql/2840358#2840358
		try {
			Context context = new InitialContext();
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e1.toString(), e1);
		}
		MysqlDataSource dataSource = new MysqlDataSource();

		dataSource.setUser(user);
		dataSource.setPassword(password);
		dataSource.setServerName(host);
		dataSource.setDatabaseName(database);

		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.toString(), e);
		}

		return conn;
	}

	// Read all
	public List<IssueTO> getAll() {
		ArrayList<IssueTO> issues = new ArrayList<IssueTO>();
		IssueTO issue = null;
		ArrayList<IssueEventBO> events = null;
		IssueEventBO event = null;

		Connection con = getConnection();

		Statement statement = null;
		ResultSet rs = null;

		Statement subStatement = null;
		ResultSet srs = null;

		String query = "SELECT * FROM ISSUETO;";

		try {
			statement = con.createStatement();
			rs = statement.executeQuery(query);
			while (rs.next()) {
				logger.info("ROW TREATED");
				// issueBO = new IssueBO();
				// issue = issueBO.getTO();
				issue = new IssueTO();

				issue.setId(rs.getLong("ID"));
				issue.setAction(rs.getString("ACTION"));
				issue.setTerm(rs.getString("TERM"));
				issue.setState(IssueTO.State.getType(rs.getInt("STATE")));
				issue.setCreationDate(rs.getLong("CREATIONDATE"));
				issue.setLastModifiedDate(rs.getLong("LASTMODIFIEDDATE"));
				issue.setLatitude(rs.getDouble("LATITUDE"));
				issue.setLongitude(rs.getDouble("LONGITUDE"));

				query = "SELECT * FROM ISSUEEVENTBO WHERE ISSUETOID = "
						+ rs.getLong("ID");

				subStatement = con.createStatement();
				srs = subStatement.executeQuery(query);
				
				events = new ArrayList<IssueEventBO>();
				while (srs.next()) {
					event = new IssueEventBO();
					event.setId(srs.getLong("ID"));
					event.setType(IssueEventBO.eventType.getType(srs.getInt("EVENTTYPE")));
					event.setDate(srs.getLong("DATE"));
					
					events.add(event);
				}				
				if(srs != null) srs.close();
				if(subStatement != null) subStatement.close();
				
				issue.setEventsList(events);
				issues.add(issue);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e.toString(), e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					throw new RuntimeException(e.toString(), e);
				}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException(e.toString(), e);
				}
			}
			if (subStatement != null)
				try {
					subStatement.close();
				} catch (SQLException e) {
					throw new RuntimeException(e.toString(), e);
				}
			if (srs != null)
				try {
					srs.close();
				} catch (SQLException e) {
					throw new RuntimeException(e.toString(), e);
				}

		}

		logger.info("IssueJDBCMySQLDAO.getAll() issues:" + JSON.encode(issues));

		return issues;
	}

	// Create
	public long create(IssueTO issue) {

		long newIssueId = -1;

		Connection con = null;
		PreparedStatement statement = null;
		PreparedStatement subStatement = null;
		ResultSet generatedKeys = null;

		String SQL_INSERT = "INSERT INTO `ISSUETO` (`ACTION`, `CREATIONDATE`, `LASTMODIFIEDDATE`, `LATITUDE`, `LONGITUDE`, `STATE`, `TERM`) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try {
			con = getConnection();
			//TODO Substitute with named parameters preparedStatement: http://stackoverflow.com/questions/2309970/named-parameters-in-jdbc
			statement = con.prepareStatement(SQL_INSERT,
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, issue.getAction());
			statement.setLong(2, issue.getCreationDate());
			statement.setLong(3, issue.getLastModifiedDate());
			statement.setDouble(4, issue.getLatitude());
			statement.setDouble(5, issue.getLongitude());
			statement.setInt(6, issue.getState().getId());
			statement.setString(7, issue.getTerm());

			int affectedRows = statement.executeUpdate();
			if (affectedRows > 0) {
				generatedKeys = statement.getGeneratedKeys();

				if (generatedKeys.next()) {
					newIssueId = generatedKeys.getLong(1);

					List<IssueEventBO> events = issue.getEventsList();

					SQL_INSERT = "INSERT INTO `mejorua`.`ISSUEEVENTBO` (`EVENTTYPE`, `DATE`, `ISSUETOID`) VALUES (?, ?, ?)";
					subStatement = con.prepareStatement(SQL_INSERT);

					for (IssueEventBO event : events) {
						subStatement.setInt(1, event.getType().getId());
						subStatement.setLong(2, event.getDate());
						subStatement.setLong(3, newIssueId);
						subStatement.executeUpdate();
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
		return new IssueTO();
	}

	// Update
	public boolean update(IssueTO issue) {
		return false;
	}

	// /////////////////////////////////////////////////////////////////////////////////
	//
	// DEBUG
	//
	// /////////////////////////////////////////////////////////////////////////////////

	private void DEBUGprePopulate() {

		DEBUGdeleteAll();
		
		List<IssueTO> issues = IssueTO.DEBUGnewIssueList();

		for (IssueTO issue : issues) {
			create(issue);
		}
	}
	
	private void DEBUGdeleteAll() {
		
		Connection con = getConnection();

		Statement statement = null;
		ResultSet rs = null;
		
		String query = "DELETE FROM ISSUEEVENTBO;";

		try {
			statement = con.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e) {
			throw new RuntimeException(e.toString(), e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					throw new RuntimeException(e.toString(), e);
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
		
		query = "DELETE FROM ISSUETO;";

		try {
			statement = con.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e) {
			throw new RuntimeException(e.toString(), e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					throw new RuntimeException(e.toString(), e);
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
	}

}
