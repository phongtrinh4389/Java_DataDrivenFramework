package com.nashtech.utils.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.nashtech.common.Common;

public class SQLiteJDBC extends Common {
	public static String strSQLiteDbName = "ess_data.db";

	public static void executeQuery(String Query) throws Exception {
		Connection connection = null;
		Statement stmt = null;
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:"
				+ strSQLiteDbName);
		// System.out.println("Opened database successfully");

		stmt = connection.createStatement();
		stmt.executeUpdate(Query);
		stmt.close();
		connection.close();
	}

	public static void createTable(String strTableName) throws Exception {
		Connection connection = null;
		Statement stmt = null;
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:"
				+ strSQLiteDbName);

		stmt = connection.createStatement();
		String sql = "CREATE TABLE " + strTableName.trim()
				+ "(NAME TEXT PRIMARY KEY NOT NULL, " + " VALUE  TEXT)";
		stmt.executeUpdate(sql);
		stmt.close();
		connection.close();
	}

	public static void insertData(String strTableName, String strColname,
			String strValue) throws Exception {
		Connection connection = null;
		Statement stmt = null;
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:"
				+ strSQLiteDbName);
		System.out.println("Opened database successfully");

		stmt = connection.createStatement();
		String sql = "INSERT INTO " + strTableName + " (NAME, VALUE)] "
				+ "VALUES (" + strColname + ", " + strValue + ")";
		stmt.executeUpdate(sql);
		stmt.close();
		connection.close();
		System.out.println("Insert data successfully");
	}

	public static String selectData(String strTableName, String strColname)
			throws Exception {
		String strValue = null;
		Connection connection = null;
		Statement stmt = null;
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:"
				+ strSQLiteDbName);
		// System.out.println("Opened database successfully");

		stmt = connection.createStatement();
		String sql = "SELECT VALUE FROM " + strTableName + " WHERE NAME = '"
				+ strColname + "'";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			strValue = rs.getString("VALUE");
		}
		rs.close();
		stmt.close();
		connection.close();
		System.out.println("Select data successfully");

		return strValue.trim();
	}

	public static void updateData(String strTableName, String strColname,
			String strValue) throws Exception {
		Connection connection = null;
		Statement stmt = null;
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:"
				+ strSQLiteDbName);
		System.out.println("Opened database successfully");

		stmt = connection.createStatement();
		String sql = "UPDATE " + strTableName + " SET VALUE = '" + strValue
				+ "' WHERE NAME = '" + strColname + "'";
		stmt.executeUpdate(sql);
		stmt.close();
		connection.close();
		System.out.println("Update data successfully");
	}

	public static void prepareDataStructure() throws Exception {
		Connection connection = null;
		Statement stmt = null;
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:"
				+ strSQLiteDbName);
		System.out.println("Opened database successfully");

		stmt = connection.createStatement();
		String sql;
		if (!isTableExists(stmt, "Data")) {
			/*
			 * Create Data table - which includes all the information related
			 * Browser, Version, OS, URLs
			 */
			sql = "CREATE TABLE IF NOT EXISTS Data (NAME TEXT PRIMARY KEY NOT NULL, VALUE  TEXT)";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO Data VALUES ('Browser', 'Chrome')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO Data VALUES ('Version', '31')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO Data VALUES ('OS', 'WINDOWS')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO Data VALUES ('JEFFURL', 'http://172.16.24.17/jeffv3/web/')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO Data VALUES ('SalesForceURL', 'https://cs18.salesforce.com/')";
			stmt.executeUpdate(sql);

			/* End of create Data table */
		}

		if (!isTableExists(stmt, "Account")) {
			/*
			 * Create Account table - which includes all the information related
			 * account of system
			 */
			sql = "CREATE TABLE IF NOT EXISTS Account (NAME TEXT PRIMARY KEY NOT NULL, VALUE  TEXT)";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO Account VALUES ('Tier3', 'tier3@harveynash.vn')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO Account VALUES ('Password', 'NASH')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO Account VALUES ('SFUsername', 'jeff1@essensys.co.uk.testground')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO Account VALUES ('SFPassword', 'Z1uW9i56')";
			stmt.executeUpdate(sql);

			/* End of create Account table */
		}

		if (!isTableExists(stmt, "PartnerData")) {
			/*
			 * Create ProjectData table - which includes all the information
			 * related project of system
			 */
			sql = "CREATE TABLE IF NOT EXISTS PartnerData (NAME TEXT PRIMARY KEY NOT NULL, VALUE  TEXT)";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO PartnerData VALUES ('Alias', 'AUT3906')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO PartnerData VALUES ('Telephone', '0123456789')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO PartnerData VALUES ('Email', 'nhudinh@essensys.com')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO PartnerData VALUES ('SupportEmail', 'support@essensys.com')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO PartnerData VALUES ('SalesEmail', 'sales@essensys.com')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO PartnerData VALUES ('BillingAddress1', 'Cau Giay Ha Noi')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO PartnerData VALUES ('BillingAddress2', 'Ba Dinh Ha Noi')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO PartnerData VALUES ('PostCode', '0123')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO PartnerData VALUES ('City', 'Ha Noi')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO PartnerData VALUES ('Country', 'Viet Nam')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO PartnerData VALUES ('PartnerType', 'Serviced Office Operator')";
			stmt.executeUpdate(sql);
			sql = "INSERT INTO PartnerData VALUES ('PriceBook', 'Avanta Pricebook')";
			stmt.executeUpdate(sql);

			/* End of create ProjectData table */
		}

		stmt.close();
		connection.close();
		System.out.println("Create data preparation successfully");
	}

	public static void clearDataStructure() throws Exception {
		Connection connection = null;
		Statement stmt = null;
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:"
				+ strSQLiteDbName);
		System.out.println("Opened database successfully");

		stmt = connection.createStatement();
		String sql = "DROP TABLE IF EXISTS Data";
		stmt.executeUpdate(sql);
		sql = "DROP TABLE IF EXISTS Account";
		stmt.executeUpdate(sql);
		sql = "DROP TABLE IF EXISTS PartnerData";
		stmt.executeUpdate(sql);

		stmt.close();
		connection.close();
		System.out.println("Clear data structure successfully");
	}

	/**
	 * This method use to check a table is existed in SQLite db or not
	 * 
	 * @param stmt
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public static boolean isTableExists(Statement stmt, String tableName)
			throws Exception {
		boolean isExists;
		if (tableName == null || stmt == null) {
			return false;
		}
		String sql = "SELECT 1 FROM sqlite_master WHERE type='table' AND name='"
				+ tableName + "'";
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next()) {
			isExists = true;
		} else {
			isExists = false;
		}
		return isExists;
	}

}