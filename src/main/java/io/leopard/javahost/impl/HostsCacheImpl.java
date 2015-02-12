package io.leopard.javahost.impl;

import io.leopard.javahost.model.Host;

import java.util.List;

/**
 * Hosts文件解析缓存实现类.
 * 
 * @author 阿海
 *
 */
public class HostsCacheImpl extends HostsImpl {
	private List<Host> list = null;

	@Override
	public List<Host> list() {
		if (list != null) {
			return list;
		}
		list = super.list();
		return list;
	}
}
