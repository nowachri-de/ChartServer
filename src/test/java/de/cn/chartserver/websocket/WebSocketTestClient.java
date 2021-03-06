package de.cn.chartserver.websocket;

import java.net.URI;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import de.cn.chartserver.ChartServerConfiguration;
import de.cn.chartserver.resource.ResourceFileHandler;

public class WebSocketTestClient extends WebSocketClient {
    protected int callCount = 0;
    
	public WebSocketTestClient(URI serverUri) {
		super(serverUri);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
	}

	@Override
	public void onMessage(String message) {
		callCount++;
	}
	
	@Override
	public void onClose(int code, String reason, boolean remote) {
		
	}

	@Override
	public void onError(Exception ex) {
		ex.printStackTrace();

	}
	
	public int getCallCount(){
	    return callCount;
	}
	
	public void setupSSL() throws Exception {
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
		setSocket(factory.createSocket());
	}

}
