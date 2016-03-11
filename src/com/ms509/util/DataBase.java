package com.ms509.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.swing.JTree;

import sun.misc.BASE64Encoder;

public class DataBase {

	private static String dbtype;
	private static String dbhost;
	private static String dbuser;
	private static String dbpass;
	private static String dbcode;

	private static String sp;
	private static String p1;
	private static String params;
	
	public DataBase() {
		// TODO Auto-generated constructor stub
	}
	//初始化配置文件
	private static void init(String config)
	{
		 dbtype = config.substring(config.indexOf("<T>")+3, config.indexOf("</T>"));
		 System.out.println("dbtype"+dbtype);
		 if(dbtype.equals("MYSQL"))
		 {
			 
			 dbhost = config.substring(config.indexOf("<H>")+3, config.indexOf("</H>"));
			 dbuser = config.substring(config.indexOf("<U>")+3, config.indexOf("</U>"));
			 dbpass = config.substring(config.indexOf("<P>")+3, config.indexOf("</P>"));
			 dbcode = config.substring(config.indexOf("<L>")+3, config.indexOf("</L>"));
		 }
		 else if(dbtype.equals("ADO"))
		 {
			 dbhost = config.substring(config.indexOf("<C>")+3, config.indexOf("</C>"));
			 System.out.println("db="+dbhost);
		 }
		
	}
	
	//获取数据库库名
	public static String[] getDBs(String url, String pass, String config, int type,String code) {
		//System.out.print("config="+config);
//		String dbtype = config.substring(config.indexOf("<T>")+3, config.indexOf("</T>"));
//		String dbhost = config.substring(config.indexOf("<H>")+3, config.indexOf("</H>"));
//		String dbuser = config.substring(config.indexOf("<U>")+3, config.indexOf("</U>"));
//		String dbpass = config.substring(config.indexOf("<P>")+3, config.indexOf("</P>"));
//		String dbcode = config.substring(config.indexOf("<L>")+3, config.indexOf("</L>"));
		init(config);
		String[] result = null;
		String rs = null;
		switch (type) {
		case 0:
			System.out.println("jsp");
			//先不考虑jsp base64 编码
			//String payload = "";

			sp = "choraheiheihei";
			p1 = Safe.JSP_DB_MYSQL;
			
			p1 = p1.replace("localhost", dbhost).replace("testdb","").replace("username", dbuser).replace("userpwd", dbpass);
			p1.replace("choraheiheihei", sp);
			
			params = pass+"="+Safe.JSP_MAKE+"&"+Safe.CODE+"="+dbcode+"&"+Safe.ACTION+"=N"+"&z1="+p1+"&z2=&z3=";
			System.out.println("params="+params);
			rs = Common.send(url, params, code);
			
			break; // jsp
		case 1:
			System.out.println("php");
			if(Safe.PHP_BASE64.equals("1"))
			{
				System.out.println("use base 64");
				String payload = "";
				try {
					BASE64Encoder encode = new BASE64Encoder();
					
					payload = encode.encode(Safe.PHP_DB_MYSQL.getBytes(code));
					payload = URLEncoder.encode(payload);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String sp = "choraheiheihei";
				String p1 = dbhost+sp+dbuser+sp+dbpass;				
				String params = pass+"="+Safe.PHP_MAKE+"&"+Safe.ACTION+"=" + payload +"&z1="+p1+"&z2=&z3=";
				//System.out.println(params);
				rs = Common.send(url, params, code);
				//System.out.println(rs);
			}
			break; // php
		case 2:
			System.out.println("asp");
			break; // asp
		case 3:
			System.out.println("aspx");
			//ASPX base64 编码
			//String payload = "";
			if(dbhost.indexOf("mdb")>0)
			{
				String dbname = dbhost.substring(dbhost.indexOf("Data Source=")+12,dbhost.indexOf("mdb")+3);
				System.out.println(dbname);
				rs = "\t|\t\r\n"+dbname;
			}else
			{
				rs = "\t|\t\r\n[ado database]";
			}
			break; // aspx
		}
		result = rs.split("\t\\|\t\r\n");
		return result;
	}
	
	
	//获取数据库表明
	public static String getTables(String url, String pass, String config, int type,String code,String dbn)
	{
		//String result = null;
		String s = "show tables from "+dbn;
		String result ="";
		if(dbtype.equals("ADO"))
		{
			result = exec_sql(url,pass,config,type,code,"",dbn);
		}
		else
		{
			result = exec_sql(url,pass,config,type,code,s,dbn);
		}
		return result;
	}
	
	
	//执行sql语句
	public static String exec_sql(String url, String pass, String config, int type,String code,String sql,String dbn) {
		//System.out.print("config="+config);
		init(config);
		String dbsql = "";
		String result = "";
		switch (type) {
		case 0:
			System.out.println("jsp");
			sp = "choraheiheihei";
			p1 = Safe.JSP_DB_MYSQL;
			
			p1 = p1.replace("localhost", dbhost).replace("testdb",dbn).replace("username", dbuser).replace("userpwd", dbpass);
			p1.replace("choraheiheihei", sp);
			
			params = pass+"="+Safe.JSP_MAKE+"&"+Safe.CODE+"="+dbcode+"&"+Safe.ACTION+"=Q"+"&z1="+p1+"&z2="+sql+"&z3=";
			System.out.println("params="+params);
			result = Common.send(url, params, code);
			break; // jsp
		case 1:   //还需区分数据库类型
			System.out.println("php");
			if(Safe.PHP_BASE64.equals("1"))
			{
				System.out.println("use base 64");
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
				String sp = "choraheiheihei";
				String p1 = dbhost+sp+dbuser+sp+dbpass;				
				String params = pass+"="+Safe.PHP_MAKE+"&"+Safe.ACTION+"=" + payload +"&z1="+p1+"&z2="+dbn+"&z3="+dbsql;
				//System.out.println(params);
				result = Common.send(url, params, code);
				//System.out.println(rs);
			}
			break; // php
		case 2:
			System.out.println("asp");
			break; // asp
		case 3:
			System.out.println("aspx");
			System.out.println("D="+dbhost);
			System.out.println(dbhost.indexOf("mdb"));
			

				p1 = dbhost;
				System.out.println("p1="+dbhost+"\n");
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
				if(dbhost.indexOf("mdb")>0)
				{
					params = pass+"="+Safe.ASPX_DB_MDB+"&z1="+p1+"&z2="+sql+"&z3=";
				}else if(dbhost.indexOf("SQLOLEDB.1")>0)
				{
					params = pass+"="+Safe.ASPX_DB_MSSQL1+"&z1="+p1+"&z2="+sql+"&z3=";
				}
				System.out.println("params="+params);
				result = Common.send(url, params, code);
			
			
			break; // aspx
		}
		return result;
	}


}
