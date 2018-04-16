package de.cn.chartserver.websocket;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;
import org.java_websocket.server.WebSocketServer;
import org.pmw.tinylog.Logger;

import com.google.gson.Gson;

import de.cn.chartserver.resource.ResourceFileHandler;
import de.cn.chartserver.ssl.SSLSetup;
import de.cn.chartserver.websocket.dto.Command;
import de.cn.chartserver.websocket.dto.RequestChart;

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
		Command cmd = (new Gson()).fromJson(message, RequestChart.class);
		try {
			String javascript = ResourceFileHandler.getResourceAsString("html/javascript/line2d.js");
			conn.send(javascript);
		} catch (IOException e) {
			Logger.error(e);
		}
		
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
