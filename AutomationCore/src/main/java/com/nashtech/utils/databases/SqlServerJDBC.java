package com.nashtech.utils.databases;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;

import com.nashtech.common.Common;
import com.nashtech.utils.report.Log;
import com.nashtech.utils.report.TestngLogger;

public class SqlServerJDBC extends Common{
	
	
	/**
	 * Get connection from jdbc
	 * 
	 * @return A connection instance that connect to jdbc
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
		
		Connection conn = null;

		try {
			
			String sqlServerName = Common.getConfigValue("SqlServerName");
			String sqlServerDbName = Common.getConfigValue("SqlServerDbName");
			String sqlServerUser = Common.getConfigValue("SqlServerUSer");
			String sqlServerPwd = Common.getConfigValue("SqlServerPwd");

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection("jdbc:sqlserver://"
					+ sqlServerName + ";databaseName=" + sqlServerDbName,
					sqlServerUser, sqlServerPwd);
			Log.info("Connected to db");

		} catch (Exception e) {

			Log.error(e.getMessage());
			TestngLogger.writeLog(e.getMessage());
			throw (e);

		}

		return conn;
	}
	
	/**
	 * Execute query command
	 * 
	 * @param query
	 *            The sql command
	 * @throws Exception
	 */
	public static void executeQuery(String query) throws Exception {

		try {

			Connection conn = getConnection();
			Statement st = conn.createStatement();
			st.execute(query);

		} catch (Exception e) {

			Log.error(e.getMessage());
			TestngLogger.writeLog(e.getMessage());
			throw (e);

		}

	}
	
	/**
	 * Perform query command and get value of a column
	 * 
	 * @param query
	 *            The sql command used to query value
	 * @param columnName
	 *            The column that want to get value
	 * @return
	 * @throws Exception
	 */
	public static String getValueInDatabase(String query, String columnName)
			throws Exception {
		String data = null;

		try {

			Connection con = getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				data = rs.getString(columnName);
			}

		} catch (Exception e) {

			Log.error(e.getMessage());
			TestngLogger.writeLog(e.getMessage());
			throw (e);

		}

		return data;
	}

}
