package com.nashtech.utils.databases;

import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Connection;

import com.mysql.jdbc.PreparedStatement;
import com.nashtech.common.Common;
import com.nashtech.utils.report.Log;
import com.nashtech.utils.report.TestngLogger;

public class MySQLJDBC extends Common {

	/**
	 * Get connection from jdbc
	 * 
	 * @return A connection instance that connect to jdbc
	 * @throws Exception
	 */
	public Connection getConnection() throws Exception {

		String strBluefinDBServername = Common.getConfigValue("BluefinDBServerName");
		String strBluefinDBName = Common.getConfigValue("BluefinDBName");
		String strBluefinDBUsername = Common.getConfigValue("BluefinDBUserName");
		String strBluefinDBPassword = Common.getConfigValue("BluefinDBPassword")
				.equalsIgnoreCase("N") ? null : Common
				.getConfigValue("BluefinDBPassword");
		
		Connection conn = null;

		try {

			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://"
					+ strBluefinDBServername + "/" + strBluefinDBName,
					strBluefinDBUsername, strBluefinDBPassword);

		} catch (Exception e) {

			Log.error(e.getMessage());
			TestngLogger.writeLog(e.getMessage());
			throw (e);

		}

		return conn;
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
	public String getValueInDatabase(String query, String columnName)
			throws Exception {
		String Data = null;

		try {

			Connection con = getConnection();
			PreparedStatement st = (PreparedStatement) con
					.prepareStatement(query);
			java.sql.ResultSet r1 = (java.sql.ResultSet) st.executeQuery();

			if (((java.sql.ResultSet) r1).next()) {
				Data = ((java.sql.ResultSet) r1).getString(columnName);
			}

		} catch (Exception e) {

			Log.error(e.getMessage());
			TestngLogger.writeLog(e.getMessage());
			throw (e);

		}

		return Data;
	}

	/**
	 * Execute query command
	 * 
	 * @param query
	 *            The sql command
	 * @throws Exception
	 */
	public void executeQuery(String query) throws Exception {

		try {

			Connection conn = getConnection();
			Statement st = (Statement) conn.createStatement();
			st.execute(query);

		} catch (Exception e) {

			Log.error(e.getMessage());
			TestngLogger.writeLog(e.getMessage());
			throw (e);

		}

	}

}