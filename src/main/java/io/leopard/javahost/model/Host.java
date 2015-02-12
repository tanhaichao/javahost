package io.leopard.javahost.model;

/**
 * Host Model.
 * 
 * @author 阿海
 *
 */
public class Host {
	private String host;
	private String ip;
	private long expiration;// 过期时间

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public long getExpiration() {
		return expiration;
	}

	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

	@Override
	public String toString() {
		return "JavaHost [host=" + host + ", ip=" + ip + "]";
	}

}
