package es.ua.dlsi.mejorua.api.persistance.jdbc.mysql;

import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DAO {

	private static MysqlDataSource dataSource;

	static {
		dataSource = new MysqlDataSource();
		IssueDAO.DEBUGprePopulate();
	}
	
	public static Connection getConnection() {

		// String database = "mejorua";
		String database = "mejorua";

		String user = "mejorua";
		String password = "";

		String dbms = "mysql";
		String host = "localhost";
		String port = "3306";

		//MysqlDataSource dataSource = null;
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
		/*
		try {
			Context context = new InitialContext();
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e1.toString(), e1);
		}
		*/
		//MysqlDataSource dataSource = new MysqlDataSource(); //Passed to static constructor to reuse it and avoid MySQLNonTransientConnectionException on runtime on getConnection
		
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
}
