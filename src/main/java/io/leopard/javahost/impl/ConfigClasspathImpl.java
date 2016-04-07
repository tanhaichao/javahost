package io.leopard.javahost.impl;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import io.leopard.javahost.Config;

public class ConfigClasspathImpl implements Config {

	@Override
	public InputStream find() {
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources;
		try {
			resources = resolver.getResources("classpath*:/dev/dns.properties");
		}
		catch (IOException e) {
			return null;
		}
		for (Resource resource : resources) {
			try {
				InputStream is = resource.getInputStream();
				return is;
			}
			catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		return null;
	}

}
