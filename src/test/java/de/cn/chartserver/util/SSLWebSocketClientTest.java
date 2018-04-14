package de.cn.chartserver.util;

import java.net.URI;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.java_websocket.WebSocketImpl;
import org.junit.Assert;
import org.junit.Test;

import de.cn.chartserver.ChartServer;
import de.cn.chartserver.ChartServerConfiguration;


public class SSLWebSocketClientTest {

	protected void setupSSL(WebSocketTestClient testClient) throws Exception {
		String STORETYPE = "JKS";

		KeyStore ks = KeyStore.getInstance(STORETYPE);
		String resourcePath = "ssl/keystore.jks";
		ks.load(ResourceFileHandler.getInputStream(resourcePath),
				ChartServerConfiguration.DEFAULT_KEYSTORE_PASSWORD.toCharArray());

		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
		kmf.init(ks, ChartServerConfiguration.DEFAULT_KEYSTORE_PASSWORD.toCharArray());

		TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
		tmf.init(ks);

		SSLContext sslContext = null;
		sslContext = SSLContext.getInstance("TLS");
		sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

		SSLSocketFactory factory = sslContext.getSocketFactory();
		testClient.setSocket(factory.createSocket());
	}

	@Test(timeout=10000)
	public void webSocketTest() throws Exception {
		WebSocketImpl.DEBUG = false;
		ChartServer chartServer = ChartServer.createNewInstance(ChartServerConfiguration.createConfiguration(8787,8789));
		
		WebSocketTestClient testClient = new WebSocketTestClient(new URI("wss://localhost:8789"));
		setupSSL(testClient);
		Assert.assertTrue(testClient.connectBlocking());
		testClient.send("Unit Test");
		testClient.close();
	}
}