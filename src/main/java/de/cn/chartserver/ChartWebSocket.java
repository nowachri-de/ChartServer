package de.cn.chartserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;
import org.java_websocket.server.WebSocketServer;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;

import de.cn.chartserver.util.ResourceFileHandler;

public class ChartWebSocket extends WebSocketServer {

	public ChartWebSocket(int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
	}

	public ChartWebSocket(InetSocketAddress address) {
		super(address);
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		Logger.debug(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		Logger.debug(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " disconnected");
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		Logger.debug(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " message received: " + message);

	}

	@Override
	public void onMessage(WebSocket conn, ByteBuffer message) {
		Logger.debug(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " message received: " + message);
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		WebSocketImpl.DEBUG = false;
		Configurator.currentConfig()
		   .level("org.pmw.tinylog", Level.DEBUG)
		   .activate();
		int port = 8887; // 843 flash policy port
		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception ex) {
		}
		ChartWebSocket s = new ChartWebSocket(port);
		s.setupSSL();
		s.start();
		System.out.println("ChartWebSocket started on port: " + s.getPort());

		BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			String in = sysin.readLine();
			s.broadcast(in);
			if (in.equals("exit")) {
				s.stop(1000);
				break;
			}
		}
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
		if (conn != null) {
			// some errors like port binding failed may not be assignable to a
			// specific websocket
		}
	}

	@Override
	public void onStart() {
		System.out.println("Server started!");
	}

	public void setupSSL() {
		// load up the key store
		String STORETYPE = "JKS";

		try{
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
			setWebSocketFactory(new DefaultSSLWebSocketServerFactory(sslContext));
		}catch(Exception e){
			Logger.error(e);
		}
	}
}
