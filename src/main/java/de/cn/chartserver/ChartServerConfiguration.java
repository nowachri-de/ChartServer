package de.cn.chartserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.pmw.tinylog.Logger;

/**
 * Used for providing server configuration values
 * 
 */
public class ChartServerConfiguration {
	public static final String HTTP = "http";
	public static final String HTTPS = "https";
	public static final String WSS_PRTOCOL = "wss";
	public static final String WS_PRTOCOL = "ws";
	
	public static final String DEFAULT_KEYSTORE_LOCATION = "ssl";
	public static final String DEFAULT_KEYSTORE_FILENAME = "keystore.jks";
	public static final String DEFAULT_KEYSTORE_PASSWORD = "password";
	public static final String DEFAULT_PROCTOCOL = HTTPS;
	public static final String DEFAULT_WS_PROCTOCOL = WSS_PRTOCOL;
	public static final String DEFAULT_HOST = "localhost";
	public static final String DEFAULT_LOGGING_LEVEL = "info";
	public static final Boolean DEFAULT_USE_EXTERNAL_KEYSTORE = false;

	public static final int DEFAULT_WEB_THREAD_POOL_SIZE = 10;
	public static final int DEFAULT_WEBSOCKET_PORT = 8788;
	public static final int DEFAULT_SERVER_PORT = 8787;

	protected int port = ChartServerConfiguration.DEFAULT_SERVER_PORT;
	protected int webSocketPort = ChartServerConfiguration.DEFAULT_SERVER_PORT + 1;
	protected int webThreadPoolSize = ChartServerConfiguration.DEFAULT_WEB_THREAD_POOL_SIZE;
	protected String protocol = DEFAULT_PROCTOCOL;
	protected String host = "localhost";
	protected String keyStoreLocation = DEFAULT_KEYSTORE_LOCATION;
	protected String keyStoreFileName = DEFAULT_KEYSTORE_FILENAME;
	protected String keyStorePassword = DEFAULT_KEYSTORE_PASSWORD;
	protected String wsProtocol = DEFAULT_WS_PROCTOCOL;
	protected String loggingLevel = DEFAULT_LOGGING_LEVEL;
	protected boolean useExternalKeystore = DEFAULT_USE_EXTERNAL_KEYSTORE;
	
	protected Map<String,Object> routes = new HashMap<>();
	
	public Map<String, Object> getRoutes() {
		return routes;
	}

	public void setRoutes(Map<String, Object> routes) {
		this.routes = routes;
	}

	public void logConfiguration(){
		Logger.info("server port: " + getPort());
		Logger.info("web socket port: " + getWebSocketPort());
		Logger.info("http(s) thread pool size: " + getWebThreadPoolSize());
		Logger.info("web server protocol: " + getProtocol());
		Logger.info("web socket protocol: " + getWsProtocol());
		Logger.info("configured hostname: " + getHost());
		Logger.info("keystore file: " + getKeyStoreFileName());
		Logger.info("keystore file resource path: " + getKeyStoreLocation());
		Logger.info("keystore file password: ***********");
		Logger.info("Additional Routes (Only the custom added routes are shown here)");
		for (Entry<String, Object> entry:routes.entrySet()){
			Logger.info(entry.getKey() + " handled by " + entry.getValue().getClass().getName());
		}
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
		options.addOption("http", false, "do not use https for webserver");
		options.addOption("nowss", false, "do not ssl for websocket");
		
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
		config.setProtocol(cmdline.hasOption("http")?HTTP:HTTPS);
		config.setWsProtocol(cmdline.hasOption("nowss")?WS_PRTOCOL:WSS_PRTOCOL);
		
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

	public String getKeyStoreLocation() {
		return keyStoreLocation;
	}

	public void setKeyStoreLocation(String keyStoreLocation) {
		this.keyStoreLocation = keyStoreLocation;
	}

	public String getKeyStoreFileName() {
		return keyStoreFileName;
	}

	public void setKeyStoreFileName(String keyStoreFileName) {
		this.keyStoreFileName = keyStoreFileName;
	}

	public String getKeyStorePassword() {
		return keyStorePassword;
	}

	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}

	public boolean isWebSocketSecure() {
		return getWsProtocol().equals(WSS_PRTOCOL);
	}

	public String getWsProtocol() {
		return wsProtocol;
	}

	public void setWsProtocol(String wsProtocol) {
		this.wsProtocol = wsProtocol;
	}

    public boolean isUseExternalKeystore() {
        return useExternalKeystore;
    }

    public void setUseExternalKeystore(boolean useExternalKeystore) {
        this.useExternalKeystore = useExternalKeystore;
    }

}
