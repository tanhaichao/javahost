package io.leopard.javahost;

import io.leopard.javahost.model.Host;

import java.util.List;

/**
 * 虚拟DNS.
 * 
 * @author 阿海
 *
 */
public interface Dns {

	/**
	 * 设置域名解析.
	 * 
	 * @param host
	 *            域名
	 * @param ip
	 *            IP数组
	 * @return
	 */
	boolean update(String host, String ip);

	/**
	 * 设置域名解析.
	 * 
	 * @param host
	 *            域名
	 * @param ips
	 *            IP数组
	 * @return
	 */
	boolean update(String host, String[] ips);

	/**
	 * 删除域名解析.
	 * 
	 * @param host
	 *            域名
	 * @param ip
	 *            IP数组
	 * @return
	 */
	boolean remove(String host);

	/**
	 * 解析域名.
	 * 
	 * @param host
	 *            域名
	 * @return IP
	 */
	String queryIp(String host);

	/**
	 * 解析域名.
	 * 
	 * @param host
	 *            域名
	 * @return Host对象
	 */
	Host query(String host);

	/**
	 * 获取所有虚拟DNS记录.
	 * 
	 * @return
	 */
	List<Host> list();

	/**
	 * 获取所有虚拟DNS记录.
	 * 
	 * @param host域名
	 * @return
	 */
	List<Host> list(String host);
}
