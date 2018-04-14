package de.cn.chartserver.websocket;

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
import de.cn.chartserver.util.ResourceFileHandler;


public class SSLWebSocketClientTest {

	

	@Test(timeout=10000)
	public void webSocketTest() throws Exception {
		WebSocketImpl.DEBUG = false;
		ChartServer chartServer = ChartServer.createNewInstance(ChartServerConfiguration.createConfiguration(8787,8789));
		
		WebSocketTestClient testClient = new WebSocketTestClient(new URI("wss://localhost:8789"));
		testClient.setupSSL();
		Assert.assertTrue(testClient.connectBlocking());
		testClient.send("Unit Test");
		testClient.close();
	}
}