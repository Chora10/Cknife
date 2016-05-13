package com.ms509.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

public class Configuration {

	private OrderedProperties propertie;
	private FileInputStream fis;
	private FileOutputStream fos;

	public Configuration() {
		propertie = new OrderedProperties();
	}

	private void checkFile() {

		try {
			File file = new File("Config.ini");
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (Exception e) {

		}
	}

	public void setValue(String key, String value) {
		try {
			this.checkFile();
			fis = new FileInputStream("Config.ini");
			propertie.load(fis);
			fis.close();
			propertie.setProperty(key, value);
			fos = new FileOutputStream("Config.ini");
			propertie.store(fos, null);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getValue(String key) {
		String value = null;
		try {
			fis = new FileInputStream("Config.ini");
			propertie.load(fis);
			fis.close();
			value = propertie.getProperty(key);
		} catch (Exception e) {
			
		}
		if(value == null)
		{
			value = "";
		}
		return value;
	}
}

class OrderedProperties extends Properties {
	private static final long serialVersionUID = -4627607243846121965L;
	private final LinkedHashSet<Object> keys = new LinkedHashSet<Object>();

	public Enumeration<Object> keys() {
		return Collections.<Object> enumeration(keys);
	}

	public Object put(Object key, Object value) {
		keys.add(key);
		return super.put(key, value);
	}

	public Set<Object> keySet() {
		return keys;
	}

	public Set<String> stringPropertyNames() {
		Set<String> set = new LinkedHashSet<String>();
		for (Object key : this.keys) {
			set.add((String) key);
		}
		return set;
	}
}
