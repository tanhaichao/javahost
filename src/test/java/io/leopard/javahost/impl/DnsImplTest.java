package io.leopard.javahost.impl;

import io.leopard.javahost.JavaHost;
import io.leopard.javahost.model.Host;

import java.util.List;

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
			JavaHost.queryIp("baidu.com");
			this.dns.query("baidu.com");
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

	@Test
	public void list() {
		dns.update("javahost.leopard.io", new String[] { "127.0.0.1", "127.0.0.2" });
		List<Host> list = dns.list("javahost.leopard.io");
		System.out.println("######虚拟DNS记录#######");
		for (Host host : list) {
			System.out.println(host);
		}
		System.out.println("######ping#######");
		for (int i = 0; i < 10; i++) {
			String ip = JavaHost.queryIp("javahost.leopard.io");
			System.out.println("ping host:javahost.leopard.io" + " 返回的ip:" + ip);
		}
	}

	@Test
	public void test() {
	}

}