package de.cn.chartserver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.cli.ParseException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.cn.chartserver.resource.ResourceFileHandler;

public class HttpInsteadOfHttpsTest {
	ChartServer server;

	@Before
	public void startServer() throws ParseException, IOException {
		String[] args = { "-p", "8686", "-wsp", "8687", "-http" };
		server = ChartServer.createNewInstance(args);
		server.start();
	}

	@After
	public void stopServer() {
		server.stop();
	}

	@Test
	public void makeHttpCall() throws IOException, ParseException {

		CloseableHttpClient client = HttpClients.createMinimal();
		HttpGet httpGet = new HttpGet("http://localhost:8686/example");
		httpGet.setHeader("Accept", "text/html");

		HttpResponse response = client.execute(httpGet);
		Assert.assertTrue(response.getStatusLine().getStatusCode() == 200);
		Assert.assertEquals(ResourceFileHandler.getResourceAsString("html/samples/example1.html"),
				ResourceFileHandler.inputStreamToString(response.getEntity().getContent(), StandardCharsets.UTF_8));
		response.getEntity().getContent().close();

		server.stop();
	}
	
	@Test(expected = javax.net.ssl.SSLHandshakeException.class)
	public void httpsMustFail() throws IOException, ParseException {

		CloseableHttpClient client = HttpClients.createMinimal();
		HttpGet httpGet = new HttpGet("https://localhost:8686/example");
		httpGet.setHeader("Accept", "text/html");

		HttpResponse response = client.execute(httpGet);
		Assert.assertTrue(response.getStatusLine().getStatusCode() == 200);
		Assert.assertEquals(ResourceFileHandler.getResourceAsString("html/samples/example1.html"),
				ResourceFileHandler.inputStreamToString(response.getEntity().getContent(), StandardCharsets.UTF_8));
		response.getEntity().getContent().close();

		server.stop();
	}
}
