package de.cn.chartserver.websocket;

import java.net.URI;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.cn.chartserver.ChartServer;
import de.cn.chartserver.ChartServerConfiguration;


public class SSLWebSocketClientTest {
	ChartServer chartServer;
	@Before
	public void setupServer(){
		chartServer = ChartServer.createNewInstance(ChartServerConfiguration.createConfiguration(8787,8789));	
	}
	
	@After
	public void stopServer(){
		chartServer.stop();
	}
	
	/**
	 * Test if secure websocket connection and communication works 
	 */
	@Test
	public void webSocketTest() throws Exception {	
		WebSocketTestClient testClient = new WebSocketTestClient(new URI("wss://localhost:8789"));
		testClient.setupSSL();
		
		//connectBlocking will return true if everything is O.K
		Assert.assertTrue(testClient.connectBlocking());
		testClient.send("Unit Test");
		testClient.close();
	}
}