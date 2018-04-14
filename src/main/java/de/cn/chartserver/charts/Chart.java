package de.cn.chartserver.charts;


import java.net.URI;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.pmw.tinylog.Logger;

import de.cn.chartserver.ChartServerConfiguration;
import de.cn.chartserver.util.ResourceFileHandler;

/**
 * Base class for chart classes implementing concrete charts
 *
 */
public abstract class Chart extends WebSocketClient{

    public Chart(URI uri) {
    	super(uri);
    	try {
			setupSSL();
		} catch (Exception e) {
			Logger.error(e);
		}
    }

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		System.out.println("Connected");

	}

	@Override
	public void onMessage(String message) {
		System.out.println("got: " + message);

	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("Disconnected");
	}

	@Override
	public void onError(Exception ex) {
		ex.printStackTrace();

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
