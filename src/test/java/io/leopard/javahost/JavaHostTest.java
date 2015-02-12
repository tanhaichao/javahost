package io.leopard.javahost;

import io.leopard.javahost.model.Host;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class JavaHostTest {

	@Test
	public void queryForIp() {
		Assert.assertEquals("112.126.75.27", JavaHost.queryIp("leopard2e.leopard.io"));
	}

	@Test
	public void isValidIp() {
		// Assert.assertTrue(JavaHost.isValidIp("255.255.255.255"));
		// Assert.assertFalse(JavaHost.isValidIp("255.255.255.256"));
		// Assert.assertFalse(JavaHost.isValidIp("255.255.255"));
		Assert.assertFalse(JavaHost.isValidIp("255.255.255."));
	}

	@Test
	public void updateVirtualDns() {
		JavaHost.queryIp("leopard.io");

		Map<String, String> map = new HashMap<String, String>();
		map.put("javahost1.leopard.io", "127.0.0.1");
		map.put("javahost2.leopard.io", "127.0.0.2");

		JavaHost.updateVirtualDns(map);
		List<Host> list = JavaHost.getDns().list();
		for (Host host : list) {
			System.out.println(host);
		}
		Assert.assertEquals(2, list.size());
	}

}