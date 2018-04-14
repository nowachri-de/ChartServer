package de.cn.chartserver.websocket;

import java.net.URI;

import org.java_websocket.WebSocketImpl;
import org.junit.Assert;
import org.junit.Test;

import de.cn.chartserver.ChartServer;
import de.cn.chartserver.ChartServerConfiguration;


public class SSLWebSocketClientTest {
	@Test
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