package de.cn.chartserver.websocket;

/*
 * Copyright (c) 2010-2018 Nathan Rajlich
 *
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use,
 *  copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following
 *  conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;
import org.java_websocket.server.WebSocketServer;
import org.pmw.tinylog.Logger;

import de.cn.chartserver.ssl.SSLSetup;

/**
 * A simple WebSocketServer implementation.
 */
public class ChartWebSocketServer extends WebSocketServer {

    /**
     * Constructor expecting listening port as parameter
     * 
     * @param port Listening port of WebSocketServer
     * @throws UnknownHostException
     */
    public ChartWebSocketServer( int port ) throws UnknownHostException {
        super( new InetSocketAddress( port ) );
    }

    /**
     * Constructor expecting InetSocketAddrress as parameter
     * 
     * @param address InetSocketAddress to be used by this WebSocket
     */
    public ChartWebSocketServer( InetSocketAddress address ) {
        super( address );
    }

    /* (non-Javadoc)
     * @see org.java_websocket.server.WebSocketServer#onOpen(org.java_websocket.WebSocket, org.java_websocket.handshake.ClientHandshake)
     */
    @Override
    public void onOpen( WebSocket conn, ClientHandshake handshake ) {
        //broadcast( "new connection: " + handshake.getResourceDescriptor() ); //This method sends a message to all clients connected
        Logger.debug("new connection: " + handshake.getResourceDescriptor());
    }

    /* (non-Javadoc)
     * @see org.java_websocket.server.WebSocketServer#onClose(org.java_websocket.WebSocket, int, java.lang.String, boolean)
     */
    @Override
    public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
        //broadcast( conn + " has left the room!" );
        Logger.debug("new connection: " + conn.getResourceDescriptor());
    }

    /* (non-Javadoc)
     * @see org.java_websocket.server.WebSocketServer#onMessage(org.java_websocket.WebSocket, java.lang.String)
     */
    @Override
    public void onMessage( WebSocket conn, String message ) {
        Collection <WebSocket> clients = super.getConnections();
        
        Map<Draft, List<Framedata>> draftFrames = new HashMap<Draft, List<Framedata>>();
        synchronized( clients ) {
            for( WebSocket client : clients ) {
                if (client.equals(conn)){
                    continue;
                }
                if( client != null ) {
                    Draft draft = client.getDraft();
                    if( !draftFrames.containsKey( draft ) ) {
                        List<Framedata> frames = draft.createFrames( message, false );
                        draftFrames.put( draft, frames );
                    }
                    try {
                        client.sendFrame( draftFrames.get( draft ) );
                    } catch ( WebsocketNotConnectedException e ) {
                        //Ignore this exception in this case
                    }
                }
            }
        }
        
        Logger.debug("message from: " + conn.getResourceDescriptor() + " " +message);
    }
    
    
    /* (non-Javadoc)
     * @see org.java_websocket.server.WebSocketServer#onMessage(org.java_websocket.WebSocket, java.nio.ByteBuffer)
     */
    @Override
    public void onMessage( WebSocket conn, ByteBuffer message ) {
        broadcast( message.array() );
        Logger.debug("message from: " + conn.getResourceDescriptor() + " " +message);
    }


    public static void main( String[] args ) throws InterruptedException , IOException {
        WebSocketImpl.DEBUG = true;
        int port = 8887; // 843 flash policy port
        try {
            port = Integer.parseInt( args[ 0 ] );
        } catch ( Exception ex ) {
        }
        ChartWebSocketServer s = new ChartWebSocketServer( port );
        s.start();
        System.out.println( "ChatServer started on port: " + s.getPort() );

        BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );
        while ( true ) {
            String in = sysin.readLine();
            s.broadcast( in );
            if( in.equals( "exit" ) ) {
                s.stop(1000);
                break;
            }
        }
    }
    /* (non-Javadoc)
     * @see org.java_websocket.server.WebSocketServer#onError(org.java_websocket.WebSocket, java.lang.Exception)
     */
    @Override
    public void onError( WebSocket conn, Exception ex ) {
        //ex.printStackTrace();
        if( conn != null ) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    /* (non-Javadoc)
     * @see org.java_websocket.server.WebSocketServer#onStart()
     */
    @Override
    public void onStart() {
        
    }

    /**
     * Activate TLS by setting a DefaultSSLWebSocketFactory. The DefaultSSLWebSocketFactory used the ssl context beeing created
     * using the provided keystore.
     * 
     * @param keystoreStream Inputstream to keystore file
     * @param passphrase Password for keystore
     * @throws Exception
     */
    public void setupSSL(InputStream keystoreStream,String passphrase) throws Exception {
        setWebSocketFactory(new DefaultSSLWebSocketServerFactory(SSLSetup.createSSLContext(keystoreStream , passphrase)));
    }
}