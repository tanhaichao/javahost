package io.leopard.javahost.impl;

import io.leopard.javahost.Hosts;
import io.leopard.javahost.model.Host;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Hosts文件解析抽象实现.
 * 
 * @author 阿海
 *
 */
public abstract class AbstractHosts implements Hosts {

	/**
	 * 获取hosts文件路径.
	 * 
	 * @return hosts文件路径
	 */
	protected String getHostsPath() {
		String path;
		if (System.getProperty("os.name").startsWith("Windows")) {
			path = "c:/windows/System32/drivers/etc/hosts";
		}
		else {
			path = "/etc/hosts";
		}
		return path;
	}

	/**
	 * 读取hosts文件内容.
	 * 
	 * @return hosts文件内容
	 * @throws IOException
	 */
	protected String readHostsFile() throws IOException {
		String path = this.getHostsPath();
		BufferedReader reader = new BufferedReader(new FileReader(path));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\n");
		}
		reader.close();
		return sb.toString();
	}

	protected List<Host> parseLine(String line) {
		line = line.trim();
		line = line.replaceFirst("#.*$", "");// 过滤注释
		List<Host> list = new ArrayList<Host>();
		if (line.length() == 0) {
			return list;
		}

		String[] hosts = line.split("\\s+");
		if (hosts.length < 2) {
			throw new RuntimeException("非法host记录[" + line + "].");
		}
		for (int i = 1; i < hosts.length; i++) {
			Host host = new Host();
			host.setIp(hosts[0]);
			host.setHost(hosts[i]);
			list.add(host);
		}
		return list;
	}

}
