package de.cn.chartserver;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.concurrent.Executors;

import javax.net.ssl.KeyManagerFactory;

import org.apache.commons.cli.ParseException;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;

import de.cn.chartserver.handler.ExampleHandler;
import de.cn.chartserver.handler.JavaScriptHandler;
import de.cn.chartserver.handler.LineChart2DHandler;
import de.cn.chartserver.resource.ResourceFileHandler;
import de.cn.chartserver.threadpool.BoundRunner;
import de.cn.chartserver.websocket.ChartWebSocket;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import fi.iki.elonen.util.ServerRunner;

public class ChartServer extends RouterNanoHTTPD {
	protected ChartServerConfiguration configuration;
	protected ChartWebSocket webSocket;
	
	/**
	 * Constructor expecting server configuration as parameter.
	 * 
	 * @param config
	 *            Configuration object of server
	 * @throws Exception 
	 */
	protected ChartServer(ChartServerConfiguration config){
		super(config.getHost(), config.getPort());
		this.configuration = config;
		
		setupSSL(config);
		setupHandlers();
		setAsyncRunner(new BoundRunner(Executors.newFixedThreadPool(this.configuration.getWebThreadPoolSize())));

		try {
			setupWebSocket(config);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}


	/**
	 * Instantiates a secure websocket 
	 * @throws Exception 
	 */
	protected void setupWebSocket(ChartServerConfiguration config) throws Exception{
		
		webSocket = new ChartWebSocket(this.configuration.getWebSocketPort());
		if (config.isWebSocketSecure()){
			InputStream keystoreStream = ResourceFileHandler.getInputStream(config.getKeyStoreLocation()+"/"+config.getKeyStoreFileName());
			String password = config.getKeyStorePassword();
			webSocket.setupSSL(keystoreStream,password);
		}
		webSocket.start();
	}
	
	/**
	 * Instantiates the websocket at the port specified in the configuration
	 * @throws Exception 
	 */
	protected void setupWebSocket() throws Exception{
		webSocket = new ChartWebSocket(this.configuration.getWebSocketPort());
		webSocket.start();
	}
	
	/**
	 * Instantiate and return a ChartServer object based on the default configuration
	 * 
	 * @return ChartServer instance
	 */
	public static ChartServer createNewInstance() {
		return ChartServer.createNewInstance(ChartServerConfiguration.createConfiguration());
	}

	/**
	 * @param config ChartServerConfiguration
	 * @return ChartServer instance
	 */
	public static ChartServer createNewInstance(ChartServerConfiguration config) {
		return new ChartServer(config);
	}

	/**
	 * @param args Commandline arguments
	 * @return ChartServer instance created based on the given arguments
	 * @throws ParseException In case wrong or misspelled arguments have been provided
	 */
	public static ChartServer createNewInstance(String[] args) throws ParseException {
		return ChartServer.createNewInstance(ChartServerConfiguration.createConfiguration(args));
	}

	/**
	 * Add a route to this server. A route will execute the specified handler if the path is matched
	 */
	protected void setupHandlers() {
		addRoute("/",de.cn.chartserver.handler.IndexHandler.class);
		addRoute("/js/chartserver.js",JavaScriptHandler.class);
		addRoute(LineChart2DHandler.URL, LineChart2DHandler.class);
		addRoute("/example",ExampleHandler.class);
	}

	/**
     * Add the routes Every route is an absolute path Parameters starts with ":"
     * Handler class should implement @UriResponder interface If the handler not
     * implement UriResponder interface - toString() is used
     */
    @Override
    public void addMappings() {
       
    }
	/**
	 * @see fi.iki.elonen.router.RouterNanoHTTPD#addRoute(java.lang.String,
	 *      java.lang.Class, java.lang.Object[])
	 */
	@Override
	public void addRoute(String url, Class<?> handler, Object... initParameter) {
		configuration.routes.put(url, handler);
		super.addRoute(url, handler, this.getConfiguration());
	}

	/**
	 * Sets up a secure socket which makes it possible to handle TLS (SSL)
	 * requests.
	 */
	protected void setupSSL() {
		try {
			setupSecureSocket("/"+configuration.getKeyStoreLocation(),
					configuration.getKeyStoreFileName(),
					configuration.getKeyStorePassword());
			
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	protected void setupSSL(ChartServerConfiguration config) {
		if (config.getProtocol().equals(ChartServerConfiguration.HTTP)){
			return;
		}
		try {
			InputStream keystoreStream = ResourceFileHandler.getInputStream(config.getKeyStoreLocation() + "/" + config.getKeyStoreFileName());
			String password = config.getKeyStorePassword();
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(keystoreStream, password.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keystore, password.toCharArray());
            makeSecure(makeSSLSocketFactory(keystore, keyManagerFactory.getKeyManagers()),null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}

	/**
	 * Start the server.
	 * 
	 * @throws IOException
	 *             if the socket is in use.
	 */
	@Override
	public void stop() {
		super.stop();
		try {
			this.webSocket.stop();
		} catch (InterruptedException | IOException e) {
			Logger.error(e);
		}
	}
	
	
	protected void setupSecureSocket(String pathToKeyStore, String keyStoreFileName, String keyStorePassword)
			throws IOException {
		makeSecure(
				NanoHTTPD.makeSSLSocketFactory(pathToKeyStore + "/" + keyStoreFileName, keyStorePassword.toCharArray()),
				null);
	}

	public ChartServerConfiguration getConfiguration() {
		return configuration;
	}
	
	public ChartWebSocket getWebSocket() {
		return webSocket;
	}

	public static void main(String[] args) throws ParseException {
		Configurator.currentConfig().formatPattern("{date:yyyy-MM-dd HH:mm:ss} {level}:   {message}").level(Level.DEBUG)
				.activate();

		ChartServer server = ChartServer.createNewInstance(args);

		Logger.info("Server port: " + server.getConfiguration().getPort());
		Logger.info("Websocket port " + server.getConfiguration().getWebSocketPort());
		Logger.info("Number of web threads: " + server.getConfiguration().getWebThreadPoolSize());

		for (String path : new ArrayList<String>(server.getConfiguration().getRoutes().keySet())) {
			Logger.info("registered path " + path);
		}
		ServerRunner.executeInstance(server);
	}

}
