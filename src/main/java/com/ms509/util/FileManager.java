package com.ms509.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import sun.misc.BASE64Encoder;

public class FileManager {

	private String url;
	private String type;
	private String code;
	private String action;
	private static String kk;

	public FileManager(String url, String pass, String type, String code) {
		// TODO Auto-generated constructor stub
		this.url = url;
		Safe.PASS = pass; // 初始化密码
		this.code = code;
		this.type = type;
	}

	@SuppressWarnings("unused")
	private String makeParam1(String path) {
		String param1 = "";
		try {
			switch (this.type) {
			case "ASP(Eval)":
				if (Safe.ASP_BASE64.equals("1")) {
					BASE64Encoder encode = new BASE64Encoder();
					param1 = encode.encode(path.getBytes(this.code));
					param1 = URLEncoder.encode(param1);
				} else {
					//现使用16进制
					//param1 = URLEncoder.encode(path, this.code);
					param1 = toHexString(param1);
				}
				break;
			case "ASPX(Eval)":
				try {
					//文件内容解码是utf－8，发送请求只能是gb2312 才不乱码， 问题标注＝＝＝＝＝＝＝＝＝＝＝＝
				  param1 = URLEncoder.encode(path,this.code);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "PHP(Eval)":
				if (Safe.PHP_BASE64.equals("1")) {
					BASE64Encoder encode = new BASE64Encoder();
					param1 = encode.encode(path.getBytes(this.code));
					param1 = URLEncoder.encode(param1);
				} else {
					param1 = URLEncoder.encode(path, this.code);
				}
				break;
			case "JSP(Eval)":
				param1 = URLEncoder.encode(path, this.code);
				break;
			case "Customize":
				param1 = URLEncoder.encode(path, this.code);
				break;
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return param1;
	}

	@SuppressWarnings("unused")
	private String makeParam2(String data) {
		String param2 = "";
		try {
			switch (this.type) {
			case "ASP(Eval)":
				param2 = URLEncoder.encode(data, this.code);
				break;
			case "ASPX(Eval)":
				//暂时使用base64 默认编码
				BASE64Encoder encode_aspx = new BASE64Encoder();
				try {
					//文件内容解码是utf－8，发送请求只能是gb2312 才不乱码， 问题标注＝＝＝＝＝＝＝＝＝＝＝＝
				  param2 = encode_aspx.encode(data.getBytes("GB2312"));
				  param2 = URLEncoder.encode(param2);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "PHP(Eval)":
				if (Safe.PHP_BASE64.equals("1")
						&& !this.action.equals("upload")) {// 特殊情况特殊处理
					BASE64Encoder encode = new BASE64Encoder();
					param2 = encode.encode(data.getBytes(this.code));
					param2 = URLEncoder.encode(param2);
				} else {
					param2 = URLEncoder.encode(data, this.code);
				}
				break;
			case "JSP(Eval)":
				param2 = URLEncoder.encode(data, this.code);
				break;
			case "Customize":
				param2 = URLEncoder.encode(data, this.code);
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return param2;
	}

	public byte[] Download(String path) {
		String params = "";
		switch (this.type) {
		case "PHP(Eval)":
			params = Common.makeParams(Safe.PHP_MAKE, Safe.PHP_DOWNLOAD,
					this.makeParam1(path));
			break;
		case "JSP(Eval)":
			params = Common.makeParams(Safe.JSP_MAKE, Safe.JSP_DOWNLOAD,
					this.makeParam1(path))+"&"+Safe.CODE+"="+this.code;
			break;
		case "ASP(Eval)":
			System.out.println("d");
			params = Safe.PASS+"="+Safe.ASP_MAKE.replace("PAYLOAD", Safe.ASP_DOWNLOAD)+"&"+Safe.PARAM1+"="+this.makeParam2(path);
			//System.out.println(params);
			break;
		case "ASPX(Eval)":
			params = Safe.PASS+"="+Safe.ASPX_DOWNLOAD+"&"+Safe.PARAM1+"="+this.makeParam1(path);
			break;
		}
		return Request.doPost(this.url, params);
	}

	public String doAction(String... args) {
		String params = "";
		String action = "";
		String path = "";
		String data = "";
		this.action = ""; // 每执行一次重置一次
		if (args.length == 1) {
			action = args[0];
		} else if (args.length == 2) {
			action = args[0];
			path = args[1];
		} else {
			action = args[0];
			path = args[1];
			data = args[2];
		}
		switch (this.type) {
		case "ASP(Eval)":
			switch (action) {
			case "readindex":
				params = Safe.PASS+"="+Safe.ASP_MAKE.replace("PAYLOAD", Safe.ASP_INDEX);
				break;
			case "readdict":
				params = Safe.PASS+"="+Safe.ASP_MAKE.replace("PAYLOAD", Safe.ASP_READDICT)+"&"+Safe.PARAM1+"="+toHexString(path);
				break;
			case "readfile":
				params = Safe.PASS+"="+Safe.ASP_MAKE.replace("PAYLOAD", Safe.ASP_READFILE)+"&"+Safe.PARAM1+"="+toHexString(path);
//				System.out.println(params);
				break;
			case "savefile":
				params = Safe.PASS+"="+Safe.ASP_MAKE.replace("PAYLOAD", Safe.ASP_SAVEFILE)+"&"+Safe.PARAM1+"="+toHexString(path)+"&"+Safe.PARAM2+"="+this.makeParam2(data);
				break;
			case "addfile":
				params = Safe.PASS+"="+Safe.ASP_MAKE.replace("PAYLOAD", Safe.ASP_SAVEFILE)+"&"+Safe.PARAM1+"="+toHexString(path)+"&"+Safe.PARAM2+"="+this.makeParam2(data);
				break;
			case "newdict":
				params = Safe.PASS+"="+Safe.ASP_MAKE.replace("PAYLOAD", Safe.ASP_NEWDICT)+"&"+Safe.PARAM1+"="+this.makeParam2(path);
				//System.out.println("newfile = "+params);
				break;
			case "delete":
				params = Safe.PASS+"="+Safe.ASP_MAKE.replace("PAYLOAD", Safe.ASP_DELETE)+"&"+Safe.PARAM1+"="+this.makeParam2(path);
				//System.out.println("newfile = "+params);
				break;
			case "rename":
				params = Safe.PASS+"="+Safe.ASP_MAKE.replace("PAYLOAD", Safe.ASP_RENAME)+"&"+Safe.PARAM1+"="+this.makeParam2(path)+"&"+Safe.PARAM2+"="+this.makeParam2(data);
				break;
			case "retime":
				params = Safe.PASS+"="+Safe.ASP_MAKE.replace("PAYLOAD", Safe.ASP_RETIME)+"&"+Safe.PARAM1+"="+this.makeParam2(path)+"&"+Safe.PARAM2+"="+this.makeParam2(data);
				break;
			case "upload":
				params = Safe.PASS+"="+Safe.ASP_MAKE.replace("PAYLOAD", Safe.ASP_UPLOAD)+"&"+Safe.PARAM1+"="+toHexString(path)+"&"+Safe.PARAM2+"="+this.makeParam2(data)+"&z3=0";	
				int l = 0;
				l =  this.makeParam2(data).length();
				if(l>20000)
				{
					int z3 = 0;
					int m = 0;
					for(m  = 0 ;m<l;m=m+20000)
					{
						String k = null;
						if((m+20000)>l)
						{
							System.out.println("end");
							 k = this.makeParam2(data).substring(m,l);
						}else
						{
							 k = this.makeParam2(data).substring(m,m+20000);
						}
						z3 = m;
						params = "z3="+z3+"&"+Safe.PASS+"="+Safe.ASP_MAKE.replace("PAYLOAD", Safe.ASP_UPLOAD)+"&"+Safe.PARAM1+"="+toHexString(path)+"&"+Safe.PARAM2+"="+k;
						Common.send(this.url, params, this.code);

					}

				}else
				{
					params = "z3=0&"+Safe.PASS+"="+Safe.ASP_MAKE.replace("PAYLOAD", Safe.ASP_UPLOAD)+"&"+Safe.PARAM1+"="+toHexString(path)+"&"+Safe.PARAM2+"="+this.makeParam2(data);
					Common.send(this.url, params, this.code);
				}
				params = Safe.PASS+"="+Safe.ASP_MAKE.replace("PAYLOAD", Safe.ASP_INDEX);
				break;
			}
			break;
		case "ASPX(Eval)":
			switch (action) {
			case "readindex":
				params = Safe.PASS+"="+Safe.ASPX_INDEX+"&"+Safe.PARAM1+"="+this.makeParam2(path);
				break;
			case "readdict":
				params = Safe.PASS+"="+Safe.ASPX_READDICT+"&"+Safe.PARAM1+"="+this.makeParam2(path);
				break;
			case "readfile":
				params = Safe.PASS+"="+Safe.ASPX_READFILE+"&"+Safe.PARAM1+"="+this.makeParam2(path);
				break;
			case "savefile":
				params = Safe.PASS+"="+Safe.ASPX_SAVEFILE+"&"+Safe.PARAM1+"="+this.makeParam2(path)+"&"+Safe.PARAM2+"="+toHexString(data);
				break;
			case "addfile":
				params = Safe.PASS+"="+Safe.ASPX_SAVEFILE+"&"+Safe.PARAM1+"="+this.makeParam2(path)+"&"+Safe.PARAM2+"="+toHexString(data);
				break;
			case "newdict":
				params = Safe.PASS+"="+Safe.ASPX_NEWDICT+"&"+Safe.PARAM1+"="+this.makeParam1(path);
				break;
			case "delete":
				String param2=null;
				try {
				 param2 = URLEncoder.encode(path, this.code);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				params = Safe.PASS+"="+Safe.ASPX_DELETE+"&"+Safe.PARAM1+"="+param2;
				break;
			case "rename":
				params = Safe.PASS+"="+Safe.ASPX_RENAME+"&"+Safe.PARAM1+"="+this.makeParam1(path)+"&"+Safe.PARAM2+"="+this.makeParam1(data);
				break;
			case "retime":
				params = Safe.PASS+"="+Safe.ASPX_RETIME+"&"+Safe.PARAM1+"="+this.makeParam1(path)+"&"+Safe.PARAM2+"="+this.makeParam1(data);
				break;
			case "upload":
				String param3=null;
				BASE64Encoder encode_aspx = new BASE64Encoder();
				try {
					//文件内容解码是utf－8，发送请求只能是gb2312 才不乱码， 问题标注＝＝＝＝＝＝＝＝＝＝＝＝
				  param3 = encode_aspx.encode(path.getBytes(this.code));
				  param3 = URLEncoder.encode(param3);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				String k = data;
				int aspx_l = k.length();
				String start = "1";
				if(k.length()>40000)
				{
						int z3 = 0;
						int m = 0;
						for(m  = 0 ;m<aspx_l;m=m+40000)
						{
							String k1 = null;
							if((m+40000)>aspx_l)
							{
								System.out.println("end");
								 k1 = k.substring(m,aspx_l);
							}else
							{
								 k1 = k.substring(m,m+40000);
							}
							z3 = m;
							params = Safe.PASS+"="+Safe.ASPX_UPLOAD+"&"+Safe.PARAM1+"="+param3+"&"+Safe.PARAM2+"="+k1+"&z3="+start;
							//System.out.println("m="+m+"z="+start);
							start = "0";
							Common.send(this.url, params, this.code);

						}
					
				}else
				{
					params = Safe.PASS+"="+Safe.ASPX_UPLOAD+"&"+Safe.PARAM1+"="+param3+"&"+Safe.PARAM2+"="+data+"&z3="+start;
					start="0";
					System.out.println(params);
					Common.send(this.url, params, this.code);
				}
				break;
			}
//			System.out.println(params);
			break;
		case "PHP(Eval)":
			switch (action) {
			case "readindex":
				params = Common.makeParams(Safe.PHP_MAKE, Safe.PHP_INDEX);
				break;
			case "readdict":
				params = Common.makeParams(Safe.PHP_MAKE, Safe.PHP_READDICT,
						this.makeParam1(path));
				break;
			case "readfile":
				params = Common.makeParams(Safe.PHP_MAKE, Safe.PHP_READFILE,
						this.makeParam1(path));
				break;
			case "savefile":
				params = Common.makeParams(Safe.PHP_MAKE, Safe.PHP_SAVEFILE,
						this.makeParam1(path), this.makeParam2(data));
				break;
			case "delete":
				params = Common.makeParams(Safe.PHP_MAKE, Safe.PHP_DELETE,
						this.makeParam1(path));
				break;
			case "rename":
				params = Common.makeParams(Safe.PHP_MAKE, Safe.PHP_RENAME,
						this.makeParam1(path), this.makeParam2(data));
				break;
			case "retime":
				params = Common.makeParams(Safe.PHP_MAKE, Safe.PHP_RETIME,
						this.makeParam1(path), this.makeParam2(data));
				break;
			case "newdict":
				params = Common.makeParams(Safe.PHP_MAKE, Safe.PHP_NEWDICT,
						this.makeParam1(path));
				break;
			case "upload":
				this.action = "upload"; // php上传文件时z2为url去掉编码去掉%即不用base64加密,其他情况z2为base64加密
				params = Common.makeParams(Safe.PHP_MAKE, Safe.PHP_UPLOAD,
						this.makeParam1(path), this.makeParam2(data));
				break;
			}
			break;
		case "JSP(Eval)":
			switch (action) {
			case "readindex":
				params = Common.makeParams(Safe.JSP_MAKE, Safe.JSP_INDEX)+"&"+Safe.CODE+"="+this.code;
				break;
			case "readdict":
				params = Common.makeParams(Safe.JSP_MAKE, Safe.JSP_READDICT,this.makeParam1(path))+"&"+Safe.CODE+"="+this.code;
				break;
			case "readfile":
				params = Common.makeParams(Safe.JSP_MAKE, Safe.JSP_READFILE,this.makeParam1(path))+"&"+Safe.CODE+"="+this.code;
				break;
			case "savefile":
				params = Common.makeParams(Safe.JSP_MAKE, Safe.JSP_SAVEFILE,this.makeParam1(path),this.makeParam2(data))+"&"+Safe.CODE+"="+this.code;
				break;
			case "delete":
				params = Common.makeParams(Safe.JSP_MAKE, Safe.JSP_DELETE,this.makeParam1(path))+"&"+Safe.CODE+"="+this.code;
				break;
			case "rename":
				params = Common.makeParams(Safe.JSP_MAKE, Safe.JSP_RENAME,this.makeParam1(path),this.makeParam2(data))+"&"+Safe.CODE+"="+this.code;
				break;
			case "retime":
				params = Common.makeParams(Safe.JSP_MAKE, Safe.JSP_RETIME,this.makeParam1(path),this.makeParam2(data))+"&"+Safe.CODE+"="+this.code;
				break;
			case "newdict":
				params = Common.makeParams(Safe.JSP_MAKE, Safe.JSP_NEWDICT,this.makeParam1(path))+"&"+Safe.CODE+"="+this.code;
				break;
			case "upload":
				params = Common.makeParams(Safe.JSP_MAKE, Safe.JSP_UPLOAD,this.makeParam1(path),this.makeParam2(data))+"&"+Safe.CODE+"="+this.code;
				break;
			
			}
			break;
		case "Customize":
			switch (action) {
			case "readindex":
				params = Common.makeParams(Safe.CUS_MAKE, Safe.CUS_INDEX);
				break;
			case "readdict":
				params = Common.makeParams(Safe.CUS_MAKE, Safe.CUS_READDICT,this.makeParam1(path));
				break;
			case "readfile":
				params = Common.makeParams(Safe.CUS_MAKE, Safe.CUS_READFILE,this.makeParam1(path));
				break;
			case "savefile":
				params = Common.makeParams(Safe.CUS_MAKE, Safe.CUS_SAVEFILE,this.makeParam1(path),this.makeParam2(data));
				break;
			case "delete":
				params = Common.makeParams(Safe.CUS_MAKE, Safe.CUS_DELETE,this.makeParam1(path));
				break;
			case "rename":
				params = Common.makeParams(Safe.CUS_MAKE, Safe.CUS_RENAME,this.makeParam1(path),this.makeParam2(data));
				break;
			case "retime":
				params = Common.makeParams(Safe.CUS_MAKE, Safe.CUS_RETIME,this.makeParam1(path),this.makeParam2(data));
				break;
			case "newdict":
				params = Common.makeParams(Safe.CUS_MAKE, Safe.CUS_NEWDICT,this.makeParam1(path));
				break;
			case "upload":
				params = Common.makeParams(Safe.CUS_MAKE, Safe.CUS_UPLOAD,this.makeParam1(path),this.makeParam2(data));
				break;
			}
			break;
		}
		
		return Common.send(this.url, params, this.code);
	}

	public String[] makeleft(String path) {
		String[] filedicts = null;
		try {
			filedicts = this.doAction("readdict", path).split("\n");
			ArrayList<String> al = new ArrayList<String>();
			for (String tmp : filedicts) {
				String s = tmp.split("\t")[0];
				if (s.charAt(s.length() - 1) == '/') {
					String noslash = s.substring(0, s.length() - 1);
					if (!noslash.equals(".") && !noslash.equals("..")) {
						al.add(noslash);
					}
				}
			}
			String[] left = al.toArray(new String[] {});
			return left;
		} catch (Exception e) {
			return filedicts = new String[]{};
		}

	}

	public String[] makeright(String path) {
		String tmp = this.doAction("readdict", path);
		if(tmp.equals(""))
		{
			tmp = "./	1970-00-00 00:00:00	0	0\n../	1970-00-00 00:00:00	0	0";//初始化
		}
		String[] filedicts = tmp.split("\n");
		return filedicts;
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
}
