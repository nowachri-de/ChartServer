package de.cn.chartserver;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Used for providing server configuration values
 * 
 */
public class ChartServerConfiguration {

	public static final String DEFAULT_KEYSTORE_LOCATION = "/ssl";
	public static final String DEFAULT_KEYSTORE_FILENAME = "keystore.jks";
	public static final String DEFAULT_KEYSTORE_PASSWORD = "password";
	public static final String DEFAULT_PROCTOCOL = "https";
	public static final String DEFAULT_HOST = "localhost";

	public static final int DEFAULT_WEB_THREAD_POOL_SIZE = 10;
	public static final int DEFAULT_WEBSOCKET_PORT = 8788;
	public static final int DEFAULT_SERVER_PORT = 8787;

	protected int port = ChartServerConfiguration.DEFAULT_SERVER_PORT;

	protected int webSocketPort = ChartServerConfiguration.DEFAULT_SERVER_PORT + 1;
	protected int webThreadPoolSize = ChartServerConfiguration.DEFAULT_WEB_THREAD_POOL_SIZE;
	protected String protocol = DEFAULT_PROCTOCOL;
	protected String host = "localhost";

	protected Map<String,Object> routes = new HashMap<>();
	
	public Map<String, Object> getRoutes() {
		return routes;
	}

	public void setRoutes(Map<String, Object> routes) {
		this.routes = routes;
	}

	/**
	 * Private no arg constructor. Instances of ChartServerconfiguration have to
	 * be created using the createConfiguration methods.
	 */
	private ChartServerConfiguration() {
	}

	/** 
	 * Factory method for providing a ChartServerConfiguration instance
	 *
	 * @param args
	 * @return
	 * @throws ParseException
	 */
	public static ChartServerConfiguration createConfiguration(String[] args) throws ParseException {

		Options options = new Options();
		options.addOption("p", true, "port at which server is listening");
		options.addOption("wsp", true, "port at which websocket is listening");
		options.addOption("h", true, "name of host");
		options.addOption("nws", true, "maximum number of threads for the web server");
		
		CommandLineParser parser = new DefaultParser();
		CommandLine cmdline = parser.parse(options, args);

		ChartServerConfiguration config = new ChartServerConfiguration();

		config.setHost(cmdline.getOptionValue("h", "localhost"));
		config.setPort(Integer
				.parseInt(cmdline.getOptionValue("p", String.valueOf(ChartServerConfiguration.DEFAULT_SERVER_PORT))));
		config.setWebSocketPort(Integer.parseInt(
				cmdline.getOptionValue("wsp", String.valueOf(ChartServerConfiguration.DEFAULT_WEBSOCKET_PORT))));
		config.setWebThreadPoolSize(Integer.parseInt(
				cmdline.getOptionValue("nws", String.valueOf(ChartServerConfiguration.DEFAULT_WEB_THREAD_POOL_SIZE))));
		
		return config;
	}

	/**
	 * Factory method for providing a ChartServerConfiguration instance.
	 * 
	 * @param serverPort
	 * @return ChartServerConfiguration with listening port set according to the
	 *         provided parameter
	 */
	public static ChartServerConfiguration createConfiguration(int serverPort) {
		ChartServerConfiguration config = new ChartServerConfiguration();
		config.setPort(serverPort);
		return config;
	}
	
	public static ChartServerConfiguration createConfiguration() {
		return ChartServerConfiguration.createConfiguration(ChartServerConfiguration.DEFAULT_SERVER_PORT);
	}

	/**
	 * Factory method for providing a ChartServerConfiguration instance.
	 * 
	 * @param host
	 *            hostname to be used in order to reach this server
	 * @param serverPort
	 *            port at which server is listening
	 * @return ChartServerConfiguration being set to the provided parameters
	 */
	public static ChartServerConfiguration createConfiguration(String host, int serverPort) {
		ChartServerConfiguration config = createConfiguration(serverPort);
		config.setHost(host);
		return config;
	}

	/**
	 * Factory method for providing a ChartServerConfiguration instance.
	 * 
	 * @param host
	 *            hostname to be used in order to reach this server
	 * @param serverPort
	 *            port at which server is listening
	 * @return ChartServerConfiguration being set to the provided parameters
	 */
	public static ChartServerConfiguration createConfiguration(String host, int serverPort, int webThreadPoolSize) {
		ChartServerConfiguration config = ChartServerConfiguration.createConfiguration(serverPort, webThreadPoolSize);
		config.setHost(host);
		return config;
	}

	/**
	 * Factory method for providing a ChartServerConfiguration instance.
	 * 
	 * @param serverPort
	 *            port at which server is listening
	 * @param webSocketPort
	 *            port at which websocket is listening 
	 * @return ChartServerConfiguration being set to the provided parameters
	 */
	public static ChartServerConfiguration createConfiguration(int serverPort, int webSocketPort) {
		ChartServerConfiguration config = ChartServerConfiguration.createConfiguration(serverPort);
		config.setWebSocketPort(webSocketPort);
		return config;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getWebThreadPoolSize() {
		return webThreadPoolSize;
	}

	public void setWebThreadPoolSize(int webThreadPoolSize) {
		this.webThreadPoolSize = webThreadPoolSize;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getWebSocketPort() {
		return webSocketPort;
	}

	public void setWebSocketPort(int webSocketPort) {
		this.webSocketPort = webSocketPort;
	}

}
