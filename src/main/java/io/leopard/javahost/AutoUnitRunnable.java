package io.leopard.javahost;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import io.leopard.javahost.impl.ConfigImpl;

/**
 * 使用到AutoUnit时自动调用.
 * 
 * @author 阿海
 *
 */
public class AutoUnitRunnable implements Runnable {

	@Override
	public void run() {
		try {
			Config config = new ConfigImpl();
			InputStream input = config.find();
			Properties props = new Properties();
			props.load(input);
			input.close();
			JavaHost.updateVirtualDns(props);
		}
		catch (IOException e) {
			String message = "host文件[classpath:/dev/dns.properties]不存在.";
			System.out.println(message);
		}
	}
}
