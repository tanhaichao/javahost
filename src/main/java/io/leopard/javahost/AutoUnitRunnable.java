package io.leopard.javahost;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 使用到AutoUnit时自动调用.
 * 
 * @author 阿海
 *
 */
public class AutoUnitRunnable implements Runnable {

	@Override
	public void run() {
		URL url;
		try {
			url = this.find();
		}
		catch (IOException e) {
			String message = "host文件[classpath:/dev/dns.properties]不存在.";
			System.out.println(message);
			return;
		}
		URLConnection conn = null;
		try {
			conn = url.openConnection();
			InputStream input = conn.getInputStream();
			Properties props = new Properties();
			props.load(input);
			input.close();
			JavaHost.updateVirtualDns(props);
		}
		catch (IOException e) {
			if (conn != null && conn instanceof HttpURLConnection) {
				((HttpURLConnection) conn).disconnect();
			}
		}
	}

	protected URL find() throws IOException {
		Enumeration<URL> urls = this.getClass().getClassLoader().getResources("dev/dns.properties");
		if (urls.hasMoreElements()) {
			return urls.nextElement();
		}
		throw new FileNotFoundException("classpath:/dev/dns.properties");
	}
}
