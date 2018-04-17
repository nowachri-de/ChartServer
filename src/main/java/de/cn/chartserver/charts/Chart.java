package de.cn.chartserver.charts;

import java.io.InputStream;
import java.net.URI;

import javax.net.ssl.SSLSocketFactory;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.pmw.tinylog.Logger;

import de.cn.chartserver.ChartServerConfiguration;
import de.cn.chartserver.resource.ResourceFileHandler;
import de.cn.chartserver.ssl.SSLSetup;

/**
 * Base class for chart classes implementing concrete charts
 *
 */
public abstract class Chart extends WebSocketClient {

	public Chart(URI uri) {
		super(uri);
		try {
			setupSSL();
		} catch (Exception e) {
			Logger.error(e);
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.java_websocket.client.WebSocketClient#onOpen(org.java_websocket.handshake.ServerHandshake)
	 */
	@Override
	public void onOpen(ServerHandshake handshakedata) {
		
	}

	/* (non-Javadoc)
	 * @see org.java_websocket.client.WebSocketClient#onMessage(java.lang.String)
	 */
	@Override
	public void onMessage(String message) {
		
	}

	/* (non-Javadoc)
	 * @see org.java_websocket.client.WebSocketClient#onClose(int, java.lang.String, boolean)
	 */
	@Override
	public void onClose(int code, String reason, boolean remote) {
		
	}

	/* (non-Javadoc)
	 * @see org.java_websocket.client.WebSocketClient#onError(java.lang.Exception)
	 */
	@Override
	public void onError(Exception ex) {
		ex.printStackTrace();
	}

	/**
	 * @throws Exception
	 */
	public void setupSSL() throws Exception {
	    InputStream keyStoreStream = ResourceFileHandler.getInputStream("ssl/keystore.jks");
	    String password = ChartServerConfiguration.DEFAULT_KEYSTORE_PASSWORD;
	    SSLSocketFactory factory = SSLSetup.createSSLContext(keyStoreStream, password).getSocketFactory();
        setSocket(factory.createSocket());
	}

}
