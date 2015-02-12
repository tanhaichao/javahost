package io.leopard.javahost.impl;

import io.leopard.javahost.Dns;
import io.leopard.javahost.model.Host;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Map;

/**
 * 虚拟DNS抽象实现类.
 * 
 * @author 阿海
 *
 */
public abstract class AbstractDns implements Dns {

	protected static final long ABOUT_YEAR = 3600 * 24 * 1000L * 365;// 大约1年的毫秒数
	private static final long EXPIRATION = ABOUT_YEAR * 10;// 大约10年失效

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Map<String, Object> getAddressCache() {
		try {
			final Field cacheField = InetAddress.class.getDeclaredField("addressCache");
			cacheField.setAccessible(true);
			final Object addressCache = cacheField.get(InetAddress.class);

			Class clazz = addressCache.getClass();
			final Field cacheMapField = clazz.getDeclaredField("cache");
			cacheMapField.setAccessible(true);
			return (Map) cacheMapField.get(addressCache);
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private byte[] toBytes(String ip) {
		byte[] addr = new byte[4];
		{
			String[] strs = ip.split("\\.");
			for (int i = 0; i < strs.length; i++) {
				// System.out.println("strs[i]:" + strs[i]);
				addr[i] = (byte) Integer.parseInt(strs[i]);
			}
		}
		return addr;
	}

	// static final class CacheEntry {
	//
	// CacheEntry(InetAddress[] addresses, long expiration) {
	// this.addresses = addresses;
	// this.expiration = expiration;
	// }
	//
	// InetAddress[] addresses;
	// long expiration;
	// }
	protected Host[] toHost(Object entry) {
		if (entry == null) {
			throw new NullPointerException("entry不能为空.");
		}
		try {
			Class<?> clazz = entry.getClass();

			long expiration;
			{
				Field field = clazz.getDeclaredField("expiration");
				field.setAccessible(true);
				expiration = (Long) field.get(entry);
			}
			InetAddress[] addresses;
			{
				Field field = clazz.getDeclaredField("addresses");
				field.setAccessible(true);
				addresses = (InetAddress[]) field.get(entry);
			}
			Host[] hosts = new Host[addresses.length];
			for (int i = 0; i < addresses.length; i++) {
				Inet4Address address = (Inet4Address) addresses[i];
				Host host = new Host();
				host.setExpiration(expiration);
				host.setHost(address.getHostName());
				host.setIp(address.getHostAddress());
				hosts[i] = host;
			}
			return hosts;
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	// static final class CacheEntry {
	//
	// CacheEntry(InetAddress[] addresses, long expiration) {
	// this.addresses = addresses;
	// this.expiration = expiration;
	// }
	//
	// InetAddress[] addresses;
	// long expiration;
	// }
	protected Object createCacheEntry(String host, String[] ips) {
		try {
			long expiration = System.currentTimeMillis() + EXPIRATION;// 10年失效
			InetAddress[] addresses = new InetAddress[ips.length];
			for (int i = 0; i < addresses.length; i++) {
				addresses[i] = InetAddress.getByAddress(host, toBytes(ips[i]));
			}
			String className = "java.net.InetAddress$CacheEntry";
			Class<?> clazz = Class.forName(className);
			Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
			constructor.setAccessible(true);
			return constructor.newInstance(addresses, expiration);
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
