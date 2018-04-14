package de.cn.chartserver.util;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class WebSocketTestClient extends WebSocketClient {

	public WebSocketTestClient(URI serverUri) {
		super(serverUri);
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
		System.exit(0);

	}

	@Override
	public void onError(Exception ex) {
		ex.printStackTrace();

	}

}
