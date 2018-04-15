package de.cn.chartserver.handler;

import java.io.ByteArrayInputStream;
import java.util.Map;

import de.cn.chartserver.ChartServerConfiguration;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.IStatus;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import fi.iki.elonen.router.RouterNanoHTTPD.DefaultHandler;
import fi.iki.elonen.router.RouterNanoHTTPD.UriResource;

/**
 * Handler to send an example page to the client (browser)
 *
 */
public abstract class BaseHandler extends DefaultHandler {
	protected ChartServerConfiguration configuration = null;
	
    @Override
    public String getText() {
        return "not implemented";
    }

    /**
     * Load the content to be send to the client from the specified file
     * 
     * @param urlParams these parameters are provided by the framework 
     * @param session the session object is provided by the framework
     * @return content of the resource file as string, an empty string if the file could not be loaded.
     */
    public abstract String getText(Map<String, String> urlParams, IHTTPSession session);

    @Override
    public String getMimeType() {
        return "text/html";
    }

    @Override
    public IStatus getStatus() {
        return Status.OK;
    }

    @Override
    public Response get(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
    	configuration = uriResource.initParameter(ChartServerConfiguration.class);
        String text = getText(urlParams, session);
        ByteArrayInputStream inp = new ByteArrayInputStream(text.getBytes());
        int size = text.getBytes().length;
        return NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), inp, size);
    }

}
