package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

	private Connection connection = null;
	private static DBConnection dbConnection;
	
	/* her skal der forbindes til databasen
	 * 
	 * 
	 * 
	 */
	
	private static final String driverClass = "";
	private static final String dbName = "";
	private static final String serverAddress = "";
	//private static final String serverAddress = "192.168.56.2";
	private static final int    serverPort = 1433;
	private static final String userName = "";
	private static final String password = "";
	
	
	
	private DBConnection() throws DataAccessException {
		String connectionString = String.format("jdbc:sqlserver://%s:%d;databaseName=%s;user=%s;password=%s",
				serverAddress, serverPort, dbName, userName, password);
		try {
			Class.forName(driverClass);
			connection = DriverManager.getConnection(connectionString);
		} catch (ClassNotFoundException e) {
			throw new DataAccessException(e, "Could not load JDBC driver");
		} catch(SQLException e) {
			throw new DataAccessException(e, "Connection string was: " + connectionString.substring(0, connectionString.length() - password.length()) + "....");
		}
	}
	
	public static DBConnection getInstance() throws DataAccessException {
		if(dbConnection == null) {
			dbConnection = new DBConnection();
		}
		return dbConnection;
	}
	
	// ved ikke om dette skal bruges
	
	public void startTransaction() throws SQLException {
		connection.setAutoCommit(false);
	}
	
	public void commitTransaction() throws SQLException {
		connection.commit();
		connection.setAutoCommit(true);
	}
	
	public void rollbackTransaction() throws SQLException {
		connection.rollback();
		connection.setAutoCommit(true);
	}
	
	//hertil
	
	public int executeInsertWithIdentity(PreparedStatement ps) throws SQLException{
		int res = -1;
		try {
			res = ps.executeUpdate();
			if(res > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				rs.next();
				res = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return res;
	}
	
	public int executeInsertWithIdentity(String sql) throws SQLException {
		System.out.println("DBConnection, Inserting: " + sql);
		int res = -1;
		try(Statement s = connection.createStatement()) {
			res = s.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			if(res > 0) {
				ResultSet rs = s.getGeneratedKeys();
				rs.next();
				res = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return res;
	}
	
	public int executeUpdate(String sql) throws SQLException {
		System.out.println("DBConnection, Updating: " + sql);
		int res = -1;
		try(Statement s = connection.createStatement()) {
			res = s.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return res;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public void disconnect() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
