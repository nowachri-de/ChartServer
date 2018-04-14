package de.cn.chartserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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

import de.cn.chartserver.resource.ResourceFileHandler;
import de.cn.chartserver.ssl.SSLSetup;
import fi.iki.elonen.NanoHTTPD;

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
	}

	public void setupSSL(InputStream keystoreStream,String passphrase) throws Exception {
		setWebSocketFactory(new DefaultSSLWebSocketServerFactory(SSLSetup.createSSLContext(keystoreStream , passphrase)));
	}
}
