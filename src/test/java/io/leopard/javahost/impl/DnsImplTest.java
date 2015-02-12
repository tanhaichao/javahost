package io.leopard.javahost.impl;

import io.leopard.javahost.JavaHost;

import org.junit.Assert;
import org.junit.Test;

public class DnsImplTest {

	private DnsImpl dns = new DnsImpl();

	@Test
	public void update() {
		Assert.assertNull(JavaHost.queryIp("javahost.leopard.io"));
		dns.update("javahost.leopard.io", "127.0.0.1");
		Assert.assertEquals("127.0.0.1", JavaHost.queryIp("javahost.leopard.io"));
		dns.remove("javahost.leopard.io");
		Assert.assertNull(JavaHost.queryIp("javahost.leopard.io"));
	}

	@Test
	public void query() {

		{
			JavaHost.queryIp("leopard.io");
			this.dns.query("leopard.io");
		}
		{
			dns.update("javahost.leopard.io", "127.0.0.1");
			JavaHost.queryIp("javahost.leopard.io");
			this.dns.query("javahost.leopard.io");
		}
		{
			JavaHost.queryIp("leopard2e.leopard.io");
			this.dns.query("leopard2e.leopard.io");
		}
	}

}