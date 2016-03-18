package com.ms509.util;

import java.net.InetAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {

	public static String purData(String data) {
		String datas = data;
		String regex = Common.purRegex(Safe.SPL) + "(.*)"
				+ Common.purRegex(Safe.SPR);
		Matcher m = Pattern.compile(regex, Pattern.DOTALL).matcher(data);
		if (m.find()) {
			datas = m.group(1);
		} 
		return datas;
	}

	public static String purRegex(String regex) {
		regex = regex.replaceAll("\\|", "\\\\|");
		return regex;

	}

	public static String send(String url, String params, String code) {
//		System.out.println(Request.doPost(url, params, code));
		return Common.purData(Request.doPost(url, params, code));
	}

	public static String makeParams(String... args) {
		String params = "";
		if (args.length == 2) {
			params = Safe.PASS + "=" + args[0] + "&" + Safe.ACTION + "="
					+ args[1];
		} else if (args.length == 3) {
			params = Safe.PASS + "=" + args[0] + "&" + Safe.ACTION + "="
					+ args[1] + "&" + Safe.PARAM1 + "=" + args[2];
		} else if (args.length == 4) {
			params = Safe.PASS + "=" + args[0] + "&" + Safe.ACTION + "="
					+ args[1] + "&" + Safe.PARAM1 + "=" + args[2] + "&"
					+ Safe.PARAM2 + "=" + args[3];
		}
		return params;
	}

	public static String getAbsolutePath(String path) {
		int pos = path.lastIndexOf(Safe.SYSTEMSP);
		return path.substring(0, pos + 1);
	}
	public static String getName(String path)
	{

		String names[] = path.split("[/\\\\]");
		int len = names.length;
		if(len==0)
		{
			return "/";
		} else {
			return names[len-1];
		}
	
	}
	public static String autoPath(String path)
	{
		if(!path.endsWith(Safe.SYSTEMSP))
		{
			path = path + Safe.SYSTEMSP;
		}
		return path;
	}
	public static String getTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	public static String getIp(String url) {
		String ip = "";
		try {

			ip = InetAddress.getByName(new URL(url).getHost()).toString().split("/")[1];
		} catch (Exception e) {
			
		}
		return ip;
	}

	public static String toHex(byte[] bytes) {
		String hexString = "0123456789ABCDEF";
		// 根据默认编码获取字节数组

		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}
}
