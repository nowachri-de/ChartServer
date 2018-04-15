function WebSocketTest() {
	if ("WebSocket" in window) {
		alert("WebSocket is supported by your Browser!");

		// Let us open a web socket
		var ws = new WebSocket("wss://localhost:{{wsport}}");

		ws.onopen = function() {
			// Web Socket is connected, send data using send()
			ws.send("{\"command\":\"SendLine2DChart\"}");
			alert("Message is sent...");
		};

		ws.onmessage = function(evt) {
			var received_msg = evt.data;
			console.log("Message is received " + received_msg);
			eval(received_msg);
		};

		ws.onclose = function() {
			// websocket is closed.
			alert("Connection is closed...");
		};

		window.onbeforeunload = function(event) {
			socket.close();
		};
	}

	else {
		// The browser doesn't support WebSocket
		alert("WebSocket NOT supported by your Browser!");
	}
}