package io.leopard.javahost;

import io.leopard.javahost.model.Host;

import java.util.List;

/**
 * Hosts文件解析接口.
 * 
 * @author 阿海
 * 
 *
 */
public interface Hosts {

	/**
	 * 获取所有hosts文件配置的记录.
	 * 
	 * @return hosts文件记录
	 */
	List<Host> list();

	/**
	 * 判断域名是否存在.
	 * 
	 * @param host
	 *            域名
	 * @return IP
	 */
	boolean exist(String host);

	/**
	 * 根据域名获取IP.
	 * 
	 * @param host
	 *            域名
	 * @return IP
	 */
	String query(String host);
}
