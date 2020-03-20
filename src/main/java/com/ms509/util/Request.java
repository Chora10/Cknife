package com.ms509.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import sun.net.www.protocol.http.AuthCacheImpl;
import sun.net.www.protocol.http.AuthCacheValue;

public class Request {
	public static String doPost(String url, String param, String code) {
		String data = "";
		HttpURLConnection huc;
		try {
			URL u = new URL(url);
			if (isHttps(url)) {
				TrustManager[] tm = { new MyX509TrustManager() };
				SSLContext sslContext = SSLContext.getInstance("SSL"); // 可以是TLS、TLSV1、TLSV1.1、TLSV1.2、SSL、SSLV3
				sslContext.init(null, tm, new SecureRandom());
				SSLSocketFactory sf = sslContext.getSocketFactory();
				if (Safe.PROXY_STATUS.equals("1")) {
					Proxy proxy = new Proxy(Common.ProxyType(),
							Common.ProxySocketAddress());
					AuthCacheValue.setAuthCache(new AuthCacheImpl());
					Authenticator.setDefault(new BasicAuthenticator(
							Safe.PROXY_USER, Safe.PROXY_PASS));
					huc = (HttpsURLConnection) u.openConnection(proxy);
				} else {
					huc = (HttpsURLConnection) u.openConnection();
				}
				((HttpsURLConnection) huc).setSSLSocketFactory(sf);
				((HttpsURLConnection) huc)
						.setHostnameVerifier(new MyHostnameVerifier());
			} else {
				if (Safe.PROXY_STATUS.equals("1")) {
					Proxy proxy = new Proxy(Common.ProxyType(),
							Common.ProxySocketAddress());
					AuthCacheValue.setAuthCache(new AuthCacheImpl());
					Authenticator.setDefault(new BasicAuthenticator(
							Safe.PROXY_USER, Safe.PROXY_PASS));
					huc = (HttpURLConnection) u.openConnection(proxy);
				} else {
					huc = (HttpURLConnection) u.openConnection();
				}
			}
			if (Safe.REQUEST_STATUS.equals("1")) {
				Common.RequestHeader(huc);
			}
			huc.setConnectTimeout(10000);
			huc.setReadTimeout(10000);
			huc.setDoOutput(true);
			PrintWriter out = new PrintWriter(huc.getOutputStream());
			out.write(param);
			out.flush();
			out.close();
			boolean normal = (huc.getResponseCode() == 200);
			InputStream is = normal ? huc.getInputStream() : huc
					.getErrorStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int len = -1;
			byte[] b = new byte[1024];
			while ((len = is.read(b)) != -1) {
				baos.write(b, 0, len);
			}
			data = new String(baos.toByteArray(), code);
			Map map = huc.getHeaderFields();
			Set<Map.Entry> set = map.entrySet();
			String head = "";
			for (Map.Entry entry : set) {
				List<String> list = (List<String>) entry.getValue();
				if (entry.getKey() == null) {
					for (String str : list) {
						head += str + System.lineSeparator();
					}
				} else {
					for (String str : list) {
						head += entry.getKey() + ": " + str
								+ System.lineSeparator();
					}
				}
			}
			data = head + System.lineSeparator() + data;
		} catch (Exception e) {
			data = e.getMessage();
		}
		return data;
	}

	public static byte[] doPost(String url, String param) {
		ByteArrayOutputStream baos = null;
		HttpURLConnection huc;
		try {
			URL u = new URL(url);
			if (isHttps(url)) {
				TrustManager[] tm = { new MyX509TrustManager() };
				SSLContext sslContext = SSLContext.getInstance("SSL"); // 可以是TLS、TLSV1、TLSV1.1、TLSV1.2、SSL、SSLV3
				sslContext.init(null, tm, new SecureRandom());
				SSLSocketFactory sf = sslContext.getSocketFactory();
				if (Safe.PROXY_STATUS.equals("1")) {
					Proxy proxy = new Proxy(Common.ProxyType(),
							Common.ProxySocketAddress());
					AuthCacheValue.setAuthCache(new AuthCacheImpl());
					Authenticator.setDefault(new BasicAuthenticator(
							Safe.PROXY_USER, Safe.PROXY_PASS));
					huc = (HttpsURLConnection) u.openConnection(proxy);
				} else {
					huc = (HttpsURLConnection) u.openConnection();
				}
				((HttpsURLConnection) huc).setSSLSocketFactory(sf);
				((HttpsURLConnection) huc)
						.setHostnameVerifier(new MyHostnameVerifier());
			} else {
				if (Safe.PROXY_STATUS.equals("1")) {
					Proxy proxy = new Proxy(Common.ProxyType(),
							Common.ProxySocketAddress());
					AuthCacheValue.setAuthCache(new AuthCacheImpl());
					Authenticator.setDefault(new BasicAuthenticator(
							Safe.PROXY_USER, Safe.PROXY_PASS));
					huc = (HttpURLConnection) u.openConnection(proxy);
				} else {
					huc = (HttpURLConnection) u.openConnection();
				}
			}
			if (Safe.REQUEST_STATUS.equals("1")) {
				Common.RequestHeader(huc);
			}
			huc.setConnectTimeout(10000);
			huc.setReadTimeout(10000);
			huc.setDoOutput(true);
			PrintWriter out = new PrintWriter(huc.getOutputStream());
			out.write(param);
			out.flush();
			out.close();
			InputStream is = huc.getInputStream();
			baos = new ByteArrayOutputStream();
			int len = -1;
			byte[] b = new byte[1024];
			while ((len = is.read(b)) != -1) {
				baos.write(b, 0, len);
			}
		} catch (Exception e) {
		}
		return baos.toByteArray();
	}

	public static boolean isHttps(String url) {
		String sub = url.substring(0, 5);
		if (sub.equalsIgnoreCase("https")) {
			return true;
		} else {
			return false;
		}
	}

	public static String doGet(String url, HashMap<String, String> advance) {
		String data = "";
		HttpURLConnection huc;
		try {
			URL u = new URL(url);
			if (isHttps(url)) {
				TrustManager[] tm = { new MyX509TrustManager() };
				SSLContext sslContext = SSLContext.getInstance("SSL"); // 可以是TLS、TLSV1、TLSV1.1、TLSV1.2、SSL、SSLV3
				sslContext.init(null, tm, new SecureRandom());
				SSLSocketFactory sf = sslContext.getSocketFactory();
				huc = (HttpsURLConnection) u.openConnection();
				((HttpsURLConnection) huc).setSSLSocketFactory(sf);
				((HttpsURLConnection) huc)
						.setHostnameVerifier(new MyHostnameVerifier());
			} else {
				huc = (HttpURLConnection) u.openConnection();
			}
			if (advance != null) {
				Set<Map.Entry<String, String>> headers = advance.entrySet();
				for (Map.Entry<String, String> header : headers) {
					huc.setRequestProperty(header.getKey(), header.getValue());
				}
			}
			boolean normal = (huc.getResponseCode() == 200);
			InputStream is = normal ? huc.getInputStream() : huc
					.getErrorStream();
			Scanner scanner = new Scanner(is);
			while (scanner.hasNextLine()) {
				data += scanner.nextLine() + "\r\n";
			}
		} catch (Exception e) {
			data = "error";
		}
		return data;
	}

	public static String doPost(String url, String param,
			HashMap<String, String> advance) {
		String data = "";
		HttpURLConnection huc;
		try {
			URL u = new URL(url);
			if (isHttps(url)) {
				TrustManager[] tm = { new MyX509TrustManager() };
				SSLContext sslContext = SSLContext.getInstance("SSL");
				sslContext.init(null, tm, new SecureRandom());
				SSLSocketFactory sf = sslContext.getSocketFactory();
				huc = (HttpsURLConnection) u.openConnection();
				((HttpsURLConnection) huc).setSSLSocketFactory(sf);
				((HttpsURLConnection) huc)
						.setHostnameVerifier(new MyHostnameVerifier());
			} else {
				huc = (HttpURLConnection) u.openConnection();
			}
			if (advance != null) {
				Set<Map.Entry<String, String>> headers = advance.entrySet();
				for (Map.Entry<String, String> header : headers) {
					huc.setRequestProperty(header.getKey(), header.getValue());
				}
			}
			huc.setDoOutput(true);
			PrintWriter out = new PrintWriter(huc.getOutputStream());
			out.write(param);
			out.flush();
			out.close();
			boolean normal = (huc.getResponseCode() == 200);
			InputStream is = normal ? huc.getInputStream() : huc
					.getErrorStream();
			Scanner scanner = new Scanner(is);
			while (scanner.hasNextLine()) {
				data += scanner.nextLine() + "\r\n";
			}
		} catch (Exception e) {
			data = "error";
		}
		return data;
	}

	public static String getResponse(String url, String data,
			HashMap<String, String> advance) {
		Map<String, List<String>> header = null;
		Map<String, String> headers = new HashMap<>();
		HttpURLConnection huc;
		try {
			URL u = new URL(url);
			if (isHttps(url)) {
				TrustManager[] tm = { new MyX509TrustManager() };
				SSLContext sslContext = SSLContext.getInstance("SSL");
				sslContext.init(null, tm, new SecureRandom());
				SSLSocketFactory sf = sslContext.getSocketFactory();
				huc = (HttpsURLConnection) u.openConnection();
				((HttpsURLConnection) huc).setSSLSocketFactory(sf);
				((HttpsURLConnection) huc)
						.setHostnameVerifier(new MyHostnameVerifier());
			} else {
				huc = (HttpURLConnection) u.openConnection();
			}
			if (advance != null) {
				Set<Map.Entry<String, String>> rheaders = advance.entrySet();
				for (Map.Entry<String, String> rheader : rheaders) {
					huc.setRequestProperty(rheader.getKey(), rheader.getValue());
				}
			}
			header = huc.getHeaderFields();
			for (Map.Entry<String, List<String>> entry : header.entrySet()) {
				String key = entry.getKey();
				if (key == null) {
					key = "Code";
				}
				for (String value : entry.getValue()) {
					headers.put(key, value);
				}
			}
		} catch (Exception e) {
			data = "error";
		}
		return headers.get(data);
	}

	public static String doSocket(String host, int port, String path,
			boolean isHttps) {
		try {
			Socket s;
			String ip = InetAddress.getByName(host).getHostAddress();
			if (isHttps) {
				TrustManager[] tm = { new MyX509TrustManager() };
				SSLContext sslContext = SSLContext.getInstance("SSL");
				sslContext.init(null, tm, new SecureRandom());
				SSLSocketFactory factory = sslContext.getSocketFactory();
				s = factory.createSocket(ip, port);
			} else {
				s = new Socket(ip, port);
			}
			s.setSoTimeout(1000 * 5);
			PrintWriter out = new PrintWriter(s.getOutputStream());
			StringBuffer sb = new StringBuffer();
			sb.append("GET " + path + " HTTP/1.1\r\n");
			sb.append("Host: " + host + "\r\n");
			// 必须加上Connection: close。
			// 不然在读取完内容后会阻塞一段时间，因为还在等待输出流，如果不加上默认为keep-alive
			sb.append("Connection: close" + "\r\n\r\n");
			out.write(sb.toString());
			out.flush();
			InputStream in = s.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int len = -1;
			byte[] b = new byte[1024];
			while ((len = in.read(b)) != -1) {
				baos.write(b, 0, len);
			}
			return baos.toString();
		} catch (Exception e) {
			return "error";
		}
	}
}

class MyHostnameVerifier implements HostnameVerifier {

	public boolean verify(String hostname, SSLSession session) {
		return true;
	}
}

class MyX509TrustManager implements X509TrustManager {

	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
	}

	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
	}

	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}