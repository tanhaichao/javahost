package io.leopard.javahost.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

import io.leopard.javahost.Config;

public class ConfigImpl implements Config {

	@Override
	public InputStream find() throws IOException {
		InputStream input;
		try {
			input = this.findInternal();
		}
		catch (IOException e) {
			input = null;
		}
		if (input == null) {
			try {
				input = this.findByClasspath();
			}
			catch (Exception e) {
				throw new FileNotFoundException("classpath*:/dev/dns.properties");
			}
		}
		return input;
	}

	protected InputStream findByClasspath() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		@SuppressWarnings("unchecked")
		Class<Config> clazz = (Class<Config>) Class.forName("io.leopard.javahost.impl.ConfigClasspathImpl");
		Config config = clazz.newInstance();
		return config.find();
	}

	protected InputStream findInternal() throws IOException {
		Enumeration<URL> urls = this.getClass().getClassLoader().getResources("dev/dns.properties");
		URL url = null;
		if (urls.hasMoreElements()) {
			url = urls.nextElement();
		}
		else {
			throw new FileNotFoundException("classpath:/dev/dns.properties");
			// String message = "host文件[classpath:/dev/dns.properties]不存在.";
			// System.out.println(message);
			// return null;
		}
		URLConnection conn = null;
		try {
			conn = url.openConnection();
			InputStream input = conn.getInputStream();
			return input;
		}
		catch (IOException e) {
			if (conn != null && conn instanceof HttpURLConnection) {
				((HttpURLConnection) conn).disconnect();
			}
			return null;
		}
	}

}
