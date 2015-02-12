package io.leopard.javahost.impl;

import io.leopard.javahost.model.Host;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * 虚拟DNS实现类
 * 
 * @author 阿海
 *
 */
public class DnsImpl extends AbstractDns {

	@Override
	public boolean update(String host, String ip) {
		Object entry = createCacheEntry(host, ip);
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
		return this.parseVirtualHost(entry);
	}

	/**
	 * 将entry解析成虚拟Host对象。
	 * 
	 * @param entry
	 * @return
	 */
	protected Host parseVirtualHost(Object entry) {
		Host bean = super.toHost(entry);
		long millis = bean.getExpiration() - System.currentTimeMillis();
		if (millis > ABOUT_YEAR) {
			// JVM的DNS缓存默认是30秒过期，如果过期时间大于1年则表示自定义的域名解析记录
			// 在要求特别准确的情况下请注意:如果自定义了JVM DNS缓存时间超过1年，则会返回错误数据.
			return bean;
		}
		return null;
	}

	@Override
	public List<Host> list() {
		List<Host> list = new ArrayList<Host>();
		Iterator<Entry<String, Object>> iterator = getAddressCache().entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			Host host = this.parseVirtualHost(entry.getValue());
			if (host != null) {
				list.add(host);
			}
		}
		return list;
	}

}
