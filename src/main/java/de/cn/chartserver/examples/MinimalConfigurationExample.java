package de.cn.chartserver.examples;

import java.io.IOException;

import de.cn.chartserver.ChartServer;
import de.cn.chartserver.charts.LineChart2D;

/**
 * This example shows how to use ChartServer without configuration.
 */
public class MinimalConfigurationExample {
    
    public static void main(String[] args) throws IOException, InterruptedException {
        /*
         * Starting a server is of course only required if you did not start it
         * externally (e.g from the command line )
    	*/
        MinimalConfigurationExample.startServer();
        MinimalConfigurationExample.draw();
        
        //Wait a minute, so the user has time to call https://localhost:8787 in its browser
        Thread.sleep(1000 * 60);
        
    }

    public static void startServer() throws IOException {
        ChartServer server = ChartServer.createNewInctance();
        server.start();
    }
    public static void draw() throws InterruptedException {
        double[] testData = {1.0, 2.0};

        //request the server to draw a 2D line chart and display it on the browser.
        LineChart2D chart2D = new LineChart2D();
        chart2D.draw(testData);
    }

   
}
