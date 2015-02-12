package io.leopard.javahost.impl;

import io.leopard.javahost.model.Host;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

/**
 * 虚拟DNS实现类
 * 
 * @author 阿海
 *
 */
public class DnsImpl extends AbstractDns {

	@Override
	public boolean update(String host, String ip) {
		return this.update(host, new String[] { ip });
	}

	@Override
	public boolean update(String host, String[] ips) {
		Object entry = createCacheEntry(host, ips);
		getAddressCache().put(host, entry);
		return true;
	}

	@Override
	public boolean remove(String host) {
		Object obj = getAddressCache().remove(host);
		return (obj != null);
	}

	@Override
	public String queryIp(String host) {
		Host bean = this.query(host);
		if (bean == null) {
			return null;
		}
		return bean.getHost();
	}

	@Override
	public Host query(String host) {
		Object entry = getAddressCache().get(host);
		if (entry == null) {
			return null;
		}
		Host[] hosts = super.toHost(entry);
		Host bean = hosts[new Random().nextInt(hosts.length)];// 随机获取一个host
		if (isVirtualDns(bean)) {
			return bean;
		}
		return null;
	}

	/**
	 * 判断是否虚拟DNS设置的host.
	 * 
	 * @param host
	 *            DNS对象
	 * @return
	 */
	protected boolean isVirtualDns(Host host) {
		long millis = host.getExpiration() - System.currentTimeMillis();
		// JVM的DNS缓存默认是30秒过期，如果过期时间大于1年则表示自定义的域名解析记录
		// 在要求特别准确的情况下请注意:如果自定义了JVM DNS缓存时间超过1年，则会返回错误数据.
		return (millis > ABOUT_YEAR);
	}

	@Override
	public List<Host> list() {
		List<Host> list = new ArrayList<Host>();
		Iterator<Entry<String, Object>> iterator = getAddressCache().entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			Host[] hosts = super.toHost(entry.getValue());
			for (Host host : hosts) {
				if (isVirtualDns(host)) {
					list.add(host);
				}
			}
		}
		return list;
	}

	@Override
	public List<Host> list(String host) {
		Object entry = getAddressCache().get(host);
		if (entry == null) {
			return null;
		}
		Host[] hosts = super.toHost(entry);
		List<Host> list = new ArrayList<Host>();
		for (Host bean : hosts) {
			if (isVirtualDns(bean)) {
				list.add(bean);
			}
		}
		return list;
	}

}
