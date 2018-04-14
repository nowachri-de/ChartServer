package de.cn.chartserver.examples;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.WebSocketImpl;

import de.cn.chartserver.ChartServer;
import de.cn.chartserver.ChartServerConfiguration;
import de.cn.chartserver.charts.LineChart2D;

/**
 * This example shows how to use ChartServer without configuration.
 */
public class MinimalConfigurationExample {
    static ChartServer server;
   
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        /**
         * Starting a server is of course only required if it has not been started
         * externally (e.g from the command line )
    	 */
    	WebSocketImpl.DEBUG = true;
        MinimalConfigurationExample.draw(MinimalConfigurationExample.startServer());
        
        //Wait a minute, so the user has time to call https://localhost:8787 in the browser
        Thread.sleep(1000 * 60);
        server.stop();
    }
    
    public static ChartServer startServer() throws IOException {
        server = ChartServer.createNewInstance(ChartServerConfiguration.createConfiguration(8787,8789));
        server.start();
        return server;
    }
    
    public static void draw(ChartServer server) throws InterruptedException, URISyntaxException {
        double[] testData = {1.0, 2.0};
        String uri = "wss://localhost:8789";
        //request the server to draw a 2D line chart and display it on the browser.
        LineChart2D chart2D = new LineChart2D(new URI(uri));
        chart2D.draw(testData);
    }
}
