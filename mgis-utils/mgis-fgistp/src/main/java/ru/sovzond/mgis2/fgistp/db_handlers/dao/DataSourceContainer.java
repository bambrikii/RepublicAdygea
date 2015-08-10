package ru.sovzond.mgis2.fgistp.db_handlers.dao;

import org.postgresql.ds.PGPoolingDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Alexander Arakelyan on 07.08.15.
 */
public class DataSourceContainer {

	private PGPoolingDataSource dataSource;

	public DataSourceContainer(String dataSourceName, String serverName, int portNumber, String databaseName, String user, String password) {
		dataSource = new PGPoolingDataSource();
		dataSource.setDataSourceName(dataSourceName);
		dataSource.setServerName(serverName);
		dataSource.setPortNumber(portNumber);
		dataSource.setDatabaseName(databaseName);
		dataSource.setUser(user);
		dataSource.setPassword(password);
		dataSource.setMaxConnections(10);
	}

//	public Connection getConnection() throws ClassNotFoundException, SQLException {
//		return dataSource.getConnection();
//	}
//
//	public void closeConnection(Connection connection) throws SQLException {
//		connection.close();
//	}

	public <T> T exec(ExecutableWithResult exec) throws SQLException {
		try (Connection cn = dataSource.getConnection()) {
			return exec.execute(cn);
		}
	}

	public void exec(Executable exec) throws SQLException {
		try (Connection cn = dataSource.getConnection()) {
			exec.execute(cn);
		}
	}

	public interface ExecutableWithResult {
		<T> T execute(Connection cn) throws SQLException;
	}

	public interface Executable {
		void execute(Connection cn) throws SQLException;
	}
}
