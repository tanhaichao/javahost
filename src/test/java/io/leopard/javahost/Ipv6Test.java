package io.leopard.javahost;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class Ipv6Test {

	@Test
	public void ipv6() throws UnknownHostException {
		InetAddress address = Inet6Address.getByName("ipv6.leopard.io");
		String ip = address.getHostAddress();
		System.out.println("ip:" + ip);

		JavaHost.queryIp("leopard.io");

		Map<String, String> map = new HashMap<String, String>();
		map.put("javahost1.leopard.io", "127.0.0.1");
		map.put("javahost2.leopard.io", "127.0.0.2");
		map.put("javahost6.leopard.io", "fe80::a178:9e5e:47d:5df3%14");

		JavaHost.updateVirtualDns(map);
		JavaHost.printAllVirtualDns();
	}
}
