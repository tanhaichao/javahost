package io.leopard.javahost;

import io.leopard.javahost.impl.DnsImpl;
import io.leopard.javahost.impl.HostsCacheImpl;
import io.leopard.javahost.model.Host;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;

/**
 * 虚拟DNS util类.
 * 
 * @author 阿海
 *
 */
public class JavaHost {

	private static Dns dns = new DnsImpl();
	private static Hosts hosts = new HostsCacheImpl();

	/**
	 * 获取虚拟DNS接口
	 * 
	 * @return
	 */
	public static Dns getDns() {
		return dns;
	}

	/**
	 * 获取hosts文件解析接口
	 * 
	 * @return
	 */
	public static Hosts getHosts() {
		return hosts;
	}

	/**
	 * 更新虚拟DNS域名指向.
	 * 
	 * @param host
	 *            域名
	 * @param ip
	 *            IP
	 * @return
	 */
	public static boolean updateVirtualDns(String host, String ip) {
		return dns.update(host, ip);
	}

	/**
	 * 更新虚拟DNS域名指向.
	 * 
	 * @param host
	 *            域名
	 * @param ip
	 *            IP数组
	 * @return
	 */
	public static boolean updateVirtualDns(String host, String[] ips) {
		return dns.update(host, ips);
	}

	/**
	 * 更新虚拟DNS域名指向.
	 * 
	 * @param properties
	 *            key为域名，value为IP地址
	 * 
	 * @return 更新虚拟DNS记录的条数
	 */
	public static int updateVirtualDns(Properties properties) {
		Iterator<Entry<Object, Object>> iterator = properties.entrySet().iterator();
		int count = 0;
		while (iterator.hasNext()) {
			Entry<Object, Object> entry = iterator.next();
			if (!(entry.getKey() instanceof String) || !(entry.getValue() instanceof String)) {
				continue;
			}
			String host = ((String) entry.getKey()).trim();
			String ip = ((String) entry.getValue()).trim();

			if (!isLocalHost(host)) {
				count += updateVirtualDnsByStrings(host, ip);
			}
		}
		return count;
	}

	protected static int updateVirtualDnsByStrings(String host, String ipList) {
		List<String> list = new ArrayList<String>();
		for (String ip : ipList.split(",")) {
			ip = ip.trim();
			if (isValidIp(ip)) {
				list.add(ip);
			}
		}
		if (!list.isEmpty()) {
			String[] ips = new String[list.size()];
			list.toArray(ips);
			JavaHost.updateVirtualDns(host, ips);
		}
		return list.size();
	}

	/**
	 * 打印所有虚拟DNS记录.
	 */
	public static void printAllVirtualDns() {
		List<Host> list = dns.list();
		for (Host host : list) {
			System.out.println(host);
		}
	}

	public static int updateVirtualDns(Map<String, String> map) {
		Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
		int count = 0;
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			String host = entry.getKey().trim();
			String ip = entry.getValue().trim();
			if (!isLocalHost(host)) {
				count += updateVirtualDnsByStrings(host, ip);
			}
		}
		return count;
	}

	/**
	 * 判断是否合法IP.
	 * 
	 * @param ip
	 * @return
	 */
	protected static boolean isValidIp(String ip) {
		if (ip == null || ip.length() == 0) {
			return false;
		}
		String[] strs = ip.split("\\.");
		if (strs.length != 4) {
			return false;
		}
		for (int i = 0; i < strs.length; i++) {
			int num = Integer.parseInt(strs[i]);
			if (num > 255) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 根据域名查询IP，多IP时随机返回1个(查询范围:包括hosts文件、DNS服务器、虚拟DNS).
	 * 
	 * @param host
	 *            域名
	 * @return IP
	 */
	public static String queryIp(String host) {
		InetAddress[] addresses;
		try {
			addresses = InetAddress.getAllByName(host);
		}
		catch (UnknownHostException e) {
			return null;
		}
		// System.out.println("len:" + addresses.length);
		if (addresses.length == 1) {
			return addresses[0].getHostAddress();
		}
		else {
			// 多IP时，随机返回一个
			int random = new Random().nextInt(addresses.length);
			return addresses[random].getHostAddress();
		}
	}

	/**
	 * 根据域名查询IP，多IP时只返回第一个(查询范围:包括hosts文件、DNS服务器、虚拟DNS).
	 * 
	 * @param host
	 *            域名
	 * @return IP
	 */
	public static String getIp(String host) {
		InetAddress address;
		try {
			address = InetAddress.getByName(host);
		}
		catch (UnknownHostException e) {
			return null;
		}
		return address.getHostAddress();
	}

	/**
	 * 从本地hosts文件解析域名.
	 * 
	 * @param host
	 *            域名
	 * @return IP
	 */
	public static String queryIpByLocalHosts(String host) {
		return hosts.query(host);
	}

	/**
	 * 从虚拟DNS解析域名.
	 * 
	 * @param host
	 *            域名
	 * @return IP
	 */
	public static String queryIpByVirtualDns(String host) {
		return dns.queryIp(host);
	}

	/**
	 * 是否在hosts文件配置的域名?
	 * 
	 * @param host
	 *            域名
	 * @return
	 */
	public static boolean isLocalHost(String host) {
		return hosts.exist(host);
	}
}
