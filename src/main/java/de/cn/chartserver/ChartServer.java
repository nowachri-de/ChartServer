package de.cn.chartserver;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.pmw.tinylog.Logger;

import de.cn.chartserver.handler.LineChart2DHandler;
import de.cn.chartserver.handler.TestHandler;
import de.cn.chartserver.threadpool.BoundRunner;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

public class ChartServer extends RouterNanoHTTPD {
    protected Options options;

    protected ChartServer(int port)  {
        
        super(port);
        
        setupSSL();
        setOptions(setupOptions());
        setupHandlers();
        setAsyncRunner(new BoundRunner(Executors.newFixedThreadPool(ChartServerConfiguration.DEFAULT_WEB_THREAD_POOL_SIZE)));    
    }

    protected void setupHandlers(){
        addRoute(LineChart2DHandler.URL, LineChart2DHandler.class);
        addRoute("/test", TestHandler.class);
    }
    
    protected String getHostAndPort(){
        return this.getHostname() + ":" + this.getListeningPort();
    }
    
    protected ChartServer(ChartServerConfiguration config) {
        super(config.getPort());
        setAsyncRunner(new BoundRunner(Executors.newFixedThreadPool(config.getWebThreadPoolSize())));
    }

    /**
     * Factory method for creating a ChartServer instance which listens at the given port.
     * 
     * @param port Listening Port of server
     * @return ChartServer instance
     */
    public static ChartServer createNewInctance(int port) {
        return new ChartServer(port);
    }
    
    /**
     * Factory method for creating a ChartServer instance
     * 
     * @return ChartServer instance
     */
    public static ChartServer createNewInctance() {
        return new ChartServer(ChartServerConfiguration.DEFAULT_SERVER_PORT);
    }

    /**
     * Provided arguments will be used as options.
     * 
     * @param args arguments
     * @return CommandLine object
     * @throws ParseException In case invalid options are provided
     */
    public CommandLine parseCommandLine(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        return cmd;
    }

    /**
     * 
     * Define the options that can be defined.
     * 
     * @return The org.apache.commons.cli.Options object
     */
    private Options setupOptions() {
        Options options = new Options();
        options.addOption("port", true, "port number to use");
        options.addOption("web_threads", true, "maximum number of threads for the web server");

        return options;
    }

    /**
     * Sets up a secure socket which makes it possible to handle TLS (SSL) requests.
     */
    protected void setupSSL()  {
        try {
            setupSecureSocket(ChartServerConfiguration.DEFAULT_KEYSTORE_LOCATION, ChartServerConfiguration.DEFAULT_KEYSTORE_FILENAME,
                    ChartServerConfiguration.DEFAULT_KEYSTORE_PASSWORD);
        } catch (IOException e) {
            Logger.error(e);
        }
    }
    
    /**
     * Start the server.
     * 
     * @throws IOException
     *             if the socket is in use.
     */
    @Override
    public void start() throws IOException {
        start(NanoHTTPD.SOCKET_READ_TIMEOUT);
    }
    
    protected void setupSecureSocket(String pathToKeyStore, String keyStoreFileName, String keyStorePassword) throws IOException {
        System.setProperty("javax.net.ssl.trustStore", new File(pathToKeyStore + "/" + keyStoreFileName).getAbsolutePath());
        makeSecure(NanoHTTPD.makeSSLSocketFactory(pathToKeyStore + "/" + keyStoreFileName, keyStorePassword.toCharArray()), null);
    }
    
    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

}
