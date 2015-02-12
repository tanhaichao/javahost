package io.leopard.javahost.impl;

import io.leopard.javahost.model.Host;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Hosts文件解析实现类.
 * 
 * @author 阿海
 *
 */
public class HostsImpl extends AbstractHosts {

	@Override
	public List<Host> list() {
		String content;
		try {
			content = readHostsFile();
		}
		catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		// System.out.println("content:" + content);
		String[] lines = content.split("\n");
		List<Host> list = new ArrayList<Host>();
		for (String line : lines) {
			list.addAll(this.parseLine(line));
		}
		return list;
	}

	@Override
	public boolean exist(String host) {
		String ip = this.query(host);
		return (ip != null);
	}

	@Override
	public String query(String host) {
		List<Host> list = this.list();
		for (Host bean : list) {
			if (bean.getHost().equals(host)) {
				return bean.getIp();
			}
		}
		return null;
	}

}
