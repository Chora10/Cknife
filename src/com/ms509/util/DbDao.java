package com.ms509.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbDao {
	private static DbDao dao;
	private String driver;
	private String url;
	private String user;
	private String pass;
	private Statement stmt;
	private Connection conn;

	public DbDao() {
		this.driver = "org.sqlite.JDBC";
		this.url = "jdbc:sqlite:Cknife.db";
		try {
			Class.forName(this.driver);
			conn = DriverManager.getConnection(this.url);
			stmt = conn.createStatement();
			stmt.execute("CREATE TABLE IF NOT EXISTS  \"data\" (\"id\"  INTEGER NOT NULL,\"url\"  TEXT,\"pass\"  TEXT,\"config\"  TEXT,\"type\"  TEXT,\"code\"  TEXT,\"ip\"  TEXT,\"time\"  TEXT,PRIMARY KEY (\"id\"));");
		} catch (Exception e) {
		}
	}

	public static DbDao getInstance() {
		if (dao == null) {
			dao = new DbDao();
		}
		return dao;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public Statement getStmt() {
		return stmt;
	}

	public void setStmt(Statement stmt) {
		this.stmt = stmt;
	}

}
