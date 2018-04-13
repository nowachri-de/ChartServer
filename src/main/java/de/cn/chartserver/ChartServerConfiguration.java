package de.cn.chartserver;

/**
 * Used for providing server configuration values
 * 
 */
public class ChartServerConfiguration {

	public static final String DEFAULT_KEYSTORE_LOCATION = "/ssl";
	public static final String DEFAULT_KEYSTORE_FILENAME = "keystore.jks";
	public static final String DEFAULT_KEYSTORE_PASSWORD = "password";

	protected static final int DEFAULT_WEB_THREAD_POOL_SIZE = 10;
	protected static final int DEFAULT_SERVER_PORT = 8080;
	protected static final String DEFAULT_PROCTOCOL = "https";
	protected static final String DEFAULT_HOST = "localhost";

	protected int port = ChartServerConfiguration.DEFAULT_SERVER_PORT;
	protected int webThreadPoolSize = ChartServerConfiguration.DEFAULT_WEB_THREAD_POOL_SIZE;
	protected String protocol = DEFAULT_PROCTOCOL;
	protected String host = "localhost";

	/**
	 * Private constructor
	 */
	private ChartServerConfiguration() {
	}

	/**
	 * Factory method for providing a ChartServerConfiguration instance. The
	 * configurations server listening port will be set to 8787 which is the
	 * default.
	 * 
	 * @return ChartServerConfiguration
	 */
	public static ChartServerConfiguration createConfiguration() {
		return ChartServerConfiguration.createConfiguration(DEFAULT_SERVER_PORT);
	}

	/**
	 * Factory method for providing a ChartServerConfiguration instance.
	 * 
	 * @param serverPort
	 * @return ChartServerConfiguration being set to the provided parameters
	 */
	public static ChartServerConfiguration createConfiguration(int serverPort) {
		ChartServerConfiguration config = new ChartServerConfiguration();
		config.setPort(serverPort);
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
	 * @param webThreadPoolSize
	 *            number of web threads
	 * @return ChartServerConfiguration being set to the provided parameters
	 */
	public static ChartServerConfiguration createConfiguration(int serverPort, int webThreadPoolSize) {
		ChartServerConfiguration config = ChartServerConfiguration.createConfiguration(serverPort);
		config.setWebThreadPoolSize(webThreadPoolSize);
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
}
