package de.cn.chartserver.charts;

import java.io.IOException;
import java.net.URI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.cn.chartserver.ChartServer;
import de.cn.chartserver.ChartServerConfiguration;

public class ChartTest {
	
	ChartServer chartServer;
	
	@Before
	public void startServer() throws IOException{
		chartServer = ChartServer
		.createNewInstance(ChartServerConfiguration.createConfiguration(8787, 8789));
		chartServer.start();
	}
	
	@After
	public void stopServer(){
		chartServer.stop();
	}
	
	@Test(timeout = 10000)
	public void chartTest() throws Exception {
		double[] testData = { 1.0, 2.0 };
		
		LineChart2D chart = new LineChart2D(new URI("wss://localhost:8789"));
		chart.draw(testData);
		chartServer.stop();
	}
}