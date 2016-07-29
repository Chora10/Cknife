package com.ms509.util;

//数据库语句执行
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.UnsupportedCharsetException;

import javax.swing.JTree;

import sun.misc.BASE64Encoder;

public class DataBase {

	private static String dbtype;
	private static String dbhost;
	private static String dbuser;
	private static String dbpass;
	private static String dbcode;
	private static String dbmaster;
	private static String sp;
	private static String p1;
	private static String params;

	public DataBase() {
		// TODO Auto-generated constructor stub
	}

	// 初始化配置文件
	private static void init(String config, int type) {
		dbtype = config.substring(config.indexOf("<T>") + 3, config.indexOf("</T>"));
		switch (type) {
		case 0:// jsp
			if (dbtype.equals("MYSQL") || dbtype.equals("ORACLE")) { // 获取mysql
																		// oracle数据库配置信息
				dbhost = config.substring(config.indexOf("<H>") + 3, config.indexOf("</H>"));
				dbuser = config.substring(config.indexOf("<U>") + 3, config.indexOf("</U>"));
				dbpass = config.substring(config.indexOf("<P>") + 3, config.indexOf("</P>"));
				dbcode = config.substring(config.indexOf("<L>") + 3, config.indexOf("</L>"));
				if (config.indexOf("<M>") > 0) {
					dbmaster = config.substring(config.indexOf("<M>") + 3, config.indexOf("</M>"));
				} else {
					dbmaster = "";
				}

			}
			if (dbtype.equals("MSSQL")) { // 获取mssql数据库配置信息
				dbhost = config.substring(config.indexOf("<H>") + 3, config.indexOf("</H>"));
				dbuser = config.substring(config.indexOf("<U>") + 3, config.indexOf("</U>"));
				dbpass = config.substring(config.indexOf("<P>") + 3, config.indexOf("</P>"));
				dbcode = config.substring(config.indexOf("<L>") + 3, config.indexOf("</L>"));
				if (config.indexOf("<M>") > 0) {
					dbmaster = config.substring(config.indexOf("<M>") + 3, config.indexOf("</M>"));
				} else {
					dbmaster = "";
				}
			}
			break;
		case 1:// php
			if (dbtype.equals("MYSQL")) { // php － mysql
				dbhost = config.substring(config.indexOf("<H>") + 3, config.indexOf("</H>"));
				dbuser = config.substring(config.indexOf("<U>") + 3, config.indexOf("</U>"));
				dbpass = config.substring(config.indexOf("<P>") + 3, config.indexOf("</P>"));
				dbcode = config.substring(config.indexOf("<L>") + 3, config.indexOf("</L>"));
				if (config.indexOf("<M>") > 0) {
					dbmaster = config.substring(config.indexOf("<M>") + 3, config.indexOf("<M>"));
				} else {
					dbmaster = "";
				}
			} else if (dbtype.equals("MDB") || dbtype.equals("MSSQL")) { // php
																			// mdb
																			// mssql
																			// 暂无
				dbhost = config.substring(config.indexOf("<C>") + 3, config.indexOf("</C>"));
			}
			break;
		case 2:// asp
			dbhost = config.substring(config.indexOf("<C>") + 3, config.indexOf("</C>"));
			break;
		case 3:// aspx
			dbhost = config.substring(config.indexOf("<C>") + 3, config.indexOf("</C>"));
			break;
		}

	}

	// 获取数据库库名
	public static String[] getDBs(String url, String pass, String config, int type, String code) {
		init(config, type);
		String[] result = null;
		String rs = null;
		switch (type) {
		case 0: // JSP
			// 先不考虑jsp base64 编码
			if (dbtype.equals("MYSQL")) {
				p1 = Safe.JSP_DB_MYSQL;
			} else if (dbtype.equals("MSSQL")) {
				p1 = Safe.JSP_DB_MSSQL;
			} else if (dbtype.equals("ORACLE")) {
				// oracle
				p1 = Safe.JSP_DB_ORACLE;
			}
			// System.out.println("test");
			p1 = p1.replace("localhost", dbhost).replace("testdb", dbmaster).replace("username", dbuser)
					.replace("userpwd", dbpass);
			params = pass + "=" + Safe.JSP_MAKE + "&" + Safe.CODE + "=" + dbcode + "&" + Safe.ACTION + "=N" + "&z1="
					+ p1 + "&z2=&z3=";
			rs = Common.send(url, params, code);
			break;
		case 1: // php 仅支持mysql
			if (Safe.PHP_BASE64.equals("1")) {
				String payload = "";
				try {
					BASE64Encoder encode = new BASE64Encoder();

					payload = encode.encode(Safe.PHP_DB_MYSQL.getBytes(code));
					payload = URLEncoder.encode(payload);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sp = "choraheiheihei";
				String p1 = dbhost + sp + dbuser + sp + dbpass;
				String params = pass + "=" + Safe.PHP_MAKE + "&" + Safe.ACTION + "=" + payload + "&z1=" + p1
						+ "&z2=&z3=";
				rs = Common.send(url, params, code);
			} else {

			}
			break; //
		case 2: // asp //读取库名时实际并未连接数据库
			if (dbtype.equals("MDB")) {
				String dbname = dbhost.substring(dbhost.indexOf("Data Source=") + 12, dbhost.length());
				//System.out.println(dbname);
				rs = "" + dbname;
			} else if (dbtype.equals("MYSQL")) {
				String dname = "";
				dname = dbhost.substring(dbhost.indexOf("database=") + 9, dbhost.length());
				dname = dname.substring(0, dname.indexOf(";"));
				rs = exec_sql(url, pass, config, type, code, "show databases;", dname);
			} else if (dbtype.equals("MSSQL")) {
				if (dbhost.indexOf("SQLOLEDB") > 0) {
					rs = "[ado database]";
				}
				if (dbhost.indexOf("Driver=") > -1) {

					String sub = dbhost.substring(dbhost.indexOf("Database=") + 9, dbhost.length());
					sub = sub.substring(0, sub.indexOf(";"));
					String dname = sub;
					rs = exec_sql(url, pass, config, type, code,
							"select [name] from master.dbo.sysdatabases order by 1", dname);
					rs = rs.substring(rs.indexOf("name\t\\|\t\r\n") + 10, rs.length());
				}
			}

			break; //
		case 3: // aspx 问题同asp
			//System.out.println("aspx");
			// ASPX base64 编码
			if (dbtype.equals("MDB")) {
				String dbname = dbhost.substring(dbhost.indexOf("Data Source=") + 12, dbhost.indexOf("mdb") + 3);
				//System.out.println(dbname);
				getTables(url, pass, config, type, code, "");
				rs = "" + dbname;
			} else if (dbtype.equals("MYSQL")) {
				String dname = "";
				dname = dbhost.substring(dbhost.indexOf("database=") + 9, dbhost.length());
				dname = dname.substring(0, dname.indexOf(";"));
				rs = exec_sql(url, pass, config, type, code, "show databases;", dname);
			} else if (dbtype.equals("MSSQL")) {
				if (dbhost.indexOf("SQLOLEDB") > 0) {
					rs = "[ado database]";
				}
				if (dbhost.indexOf("Driver=") > -1) {

					String sub = dbhost.substring(dbhost.indexOf("Database=") + 9, dbhost.length());
					sub = sub.substring(0, sub.indexOf(";"));
					String dname = sub;
					rs = exec_sql(url, pass, config, type, code,
							"select [name] from master.dbo.sysdatabases order by 1", dname);
					rs = rs.substring(rs.indexOf("name\t\\|\t\r\n") + 10, rs.length());
				}
			} else {
				rs = "[ado database]";
				getTables(url, pass, config, type, code, "");
			}
			break; // aspx
		}
		result = rs.split("\t\\|\t\r\n");
		return result;
	}

	// 获取数据库表名
	public static String getTables(String url, String pass, String config, int type, String code, String dbn) {
		String s = "show tables from " + dbn;
		String result = "";
		switch (type) {
		case 0: // jsp
			if (dbtype.equals("MDB")) {
				result = exec_sql(url, pass, config, type, code, "", dbn);
			} else if (dbtype.equals("ORACLE")) {
				result = exec_sql(url, pass, config, type, code, "get_tables", dbn);
			} else if (dbtype.equals("MSSQL")) {
				result = exec_sql(url, pass, config, type, code,
						"SELECT [name] FROM sysobjects WHERE (xtype='U') ORDER BY 1", dbn);
			} else {
				result = exec_sql(url, pass, config, type, code, s, dbn);
			}
			break;
		case 1:// php
			if (dbtype.equals("MDB") || dbtype.equals("MSSQL")) {
				result = exec_sql(url, pass, config, type, code, "", dbn);
			} else {
				result = exec_sql(url, pass, config, type, code, s, dbn);
			}
			break;
		case 2:// asp
			if (dbtype.equals("MDB")) {
				result = exec_sql(url, pass, config, type, code, "", dbn);
			} else if (dbhost.indexOf("SQLOLEDB.1") > 0) {
				result = exec_sql(url, pass, config, type, code, "", dbn);
			} else if (dbhost.indexOf("Sql Server") > 0) {
				// params = pass + "=" + Safe.ASP_DB_MSSQL + "&z1=" + p1 +
				// "&z2=" + sql + "&z3=";
				String sql = "SELECT [name] FROM sysobjects WHERE (xtype='U') ORDER BY 1";
				result = exec_sql(url, pass, config, type, code, sql, dbn);
			} else {
				result = exec_sql(url, pass, config, type, code, "", dbn);
			}
			break;
		case 3:// aspx
			if (dbtype.equals("MDB")) {
				result = exec_sql(url, pass, config, type, code, "", dbn);
			} else if (dbhost.indexOf("SQLOLEDB.1") > 0) {
				result = exec_sql(url, pass, config, type, code, "", dbn);
			} else if (dbhost.indexOf("Sql Server") > 0) {
				// params = pass + "=" + Safe.ASP_DB_MSSQL + "&z1=" + p1 +
				// "&z2=" + sql + "&z3=";
				String sql = "SELECT [name] FROM sysobjects WHERE (xtype='U') ORDER BY 1";
				result = exec_sql(url, pass, config, type, code, sql, dbn);
			} else {
				result = exec_sql(url, pass, config, type, code, "", dbn);
			}

			break;
		}
		return result;
	}

	// 执行sql语句
	public static String exec_sql(String url, String pass, String config, int type, String code, String sql,
			String dbn) {

		init(config, type);
		String dbsql = "";
		String result = "";

		if (dbn.lastIndexOf("\t") == dbn.length()) {
			dbn.substring(0, dbn.length() - 1);
		}

		switch (type) {
		case 0: // jsp
			// System.out.println("jsp");
			String action = "Q";
			if (dbtype.equals("MYSQL")) {
				p1 = Safe.JSP_DB_MYSQL;
				p1 = p1.replace("localhost", dbhost).replace("testdb", dbn).replace("username", dbuser)
						.replace("userpwd", dbpass);

			} else if (dbtype.equals("MSSQL")) {
				if (sql.equals("get_tables")) {
					action = "O";
				}
				p1 = Safe.JSP_DB_MSSQL;
				p1 = p1.replace("localhost", dbhost).replace("testdb", dbn).replace("username", dbuser)
						.replace("userpwd", dbpass);
				// System.out.println(p1);
			} else if (dbtype.equals("ORACLE")) {
				if (sql.equals("get_tables")) {
					action = "O";
				}
				p1 = Safe.JSP_DB_ORACLE;
				p1 = p1.replace("localhost", dbhost).replace("testdb", dbmaster).replace("username", dbuser)
						.replace("userpwd", dbpass);
				// ORACLE 支持
			}
			sp = "choraheiheihei";
			params = pass + "=" + Safe.JSP_MAKE + "&" + Safe.CODE + "=" + dbcode + "&" + Safe.ACTION + "=" + action
					+ "&z1=" + p1 + sp + dbn + "&z2=" + sql + "&z3=";
			result = Common.send(url, params, code);
			break; // jsp
		case 1: // 还需区分数据库类型 php 暂只有mysql
			if (Safe.PHP_BASE64.equals("1")) {
				// System.out.println("use base 64");
				String payload = "";
				try {
					BASE64Encoder encode = new BASE64Encoder();

					payload = encode.encode(Safe.PHP_DB_MYSQL.getBytes(code));
					payload = URLEncoder.encode(payload);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					BASE64Encoder encode = new BASE64Encoder();

					dbsql = encode.encode(sql.getBytes(code));
					dbsql = URLEncoder.encode(dbsql);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sp = "choraheiheihei";
				String p1 = dbhost + sp + dbuser + sp + dbpass;
				String params = pass + "=" + Safe.PHP_MAKE + "&" + Safe.ACTION + "=" + payload + "&z1=" + p1 + "&z2="
						+ dbn + "&z3=" + dbsql;
				result = Common.send(url, params, code);
			}
			break; // php
		case 2: // asp
			p1 = dbhost;
			try {
				BASE64Encoder encode = new BASE64Encoder();
				p1 = toHexString(p1);
				p1 = URLEncoder.encode(p1);
				sql = toHexString(sql);
				sql = URLEncoder.encode(sql);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (dbtype.equals("MDB")) {
				params = pass + "=" + Safe.ASP_DB_MDB + "&z1=" + p1 + "&z2=" + sql + "&z3=";
			} else if (dbhost.indexOf("SQLOLEDB.1") > 0) {
				params = pass + "=" + Safe.ASP_DB_MSSQL + "&z1=" + p1 + "&z2=" + sql + "&z3=";
			} else if (dbhost.indexOf("Sql Server") > 0) {

				if (dbn != "") {
					String tmp = "USE [" + dbn + "];";
					tmp = toHexString(tmp);
					sql = tmp + sql;

				}

				params = pass + "=" + Safe.ASP_DB_MSSQL + "&z1=" + p1 + "&z2=" + sql + "&z3=";
			} else if (dbtype.equals("MYSQL")) {
				params = pass + "=" + Safe.ASP_DB_MSSQL + "&z1=" + p1 + "&z2=" + sql + "&z3=";
			}
			result = Common.send(url, params, code);
			break; // asp
		case 3: // aspx
			p1 = dbhost;
			try {
				BASE64Encoder encode = new BASE64Encoder();
				if (dbhost.indexOf("Sql Server") > 0) {   //使用sql server连接模式，需要先指定数据库

					if (dbn != "") {
						String tmp = "USE [" + dbn + "];";
						sql = tmp + sql;

					}
				}
								p1 = encode.encode(p1.getBytes(code));
				p1 = URLEncoder.encode(p1);
				sql = encode.encode(sql.getBytes(code));
				sql = URLEncoder.encode(sql);

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (dbtype.equals("MDB")) {
				params = pass + "=" + Safe.ASPX_DB_MDB + "&z1=" + p1 + "&z2=" + sql + "&z3=";
			} else if (dbtype.equals("MSSQL")) {
				if (dbhost.indexOf("SQLOLEDB.1") > 0) {
					params = pass + "=" + Safe.ASPX_DB_MSSQL + "&z1=" + p1 + "&z2=" + sql + "&z3=";
				} else if (dbhost.indexOf("Sql Server") > 0) {
					params = pass + "=" + Safe.ASPX_DB_MSSQL + "&z1=" + p1 + "&z2=" + sql + "&z3=";
				}
				// params = pass + "=" + Safe.ASPX_DB_MSSQL + "&z1=" + p1 +
				// "&z2=" + sql + "&z3=";
			} else if (dbtype.equals("MYSQL")) {
				params = pass + "=" + Safe.ASPX_DB_MYSQL + "&z1=" + p1 + "&z2=" + sql + "&z3=";
			}

			result = Common.send(url, params, code);
			break; // aspx
		}
		return result;
	}

	public static String[] Load_SQL() {
		String k = Safe.COMMON_SQL_STRING;
		String[] sqls = k.split("\\|\\|\\|");
		return sqls;
	}

	// 16进制 转换
	private static String toHexString(String s) {
		String str = "";
		try {
			byte[] b = s.getBytes();
			for (int i = 0; i < b.length; i++) {
				Integer I = new Integer(b[i]);
				String strTmp = I.toHexString(b[i]);
				if (strTmp.length() > 2)
					strTmp = strTmp.substring(strTmp.length() - 2);
				str = str + strTmp;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}
