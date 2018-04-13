package de.cn.chartserver.examples;

import java.io.IOException;

import de.cn.chartserver.ChartServer;
import de.cn.chartserver.charts.LineChart2D;

/**
 * This example shows how to use ChartServer without configuration.
 */
public class MinimalConfigurationExample {
    
    public static void main(String[] args) throws IOException, InterruptedException {
        
        MinimalConfigurationExample.startServer();
        MinimalConfigurationExample.draw();
        
        while(true){
            Thread.sleep(1000);
        }
    }

    public static void draw() throws InterruptedException {
        double[] testData = {1.0, 2.0};

        LineChart2D chart2D = new LineChart2D();
        chart2D.draw(testData);
    }

    public static void startServer() throws IOException {
        ChartServer server = ChartServer.createNewInctance();
        server.start();
    }
}
