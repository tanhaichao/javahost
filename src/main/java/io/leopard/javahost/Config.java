package io.leopard.javahost;

import java.io.IOException;
import java.io.InputStream;

public interface Config {

	InputStream find() throws IOException;
}
