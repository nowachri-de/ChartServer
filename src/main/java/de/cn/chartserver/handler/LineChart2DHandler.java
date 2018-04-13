package de.cn.chartserver.handler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import org.pmw.tinylog.Logger;

import de.cn.chartserver.util.ResourceFileHandler;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.IStatus;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import fi.iki.elonen.router.RouterNanoHTTPD.DefaultHandler;
import fi.iki.elonen.router.RouterNanoHTTPD.UriResource;

/**
 * Handler to send an linechart2d html page to the client (browser)
 *
 */
public class LineChart2DHandler extends DefaultHandler {
    public static final String URL="/linechart2d";
    protected static final String CONTENT_FILE = "html/linechart2d.html";
    
    @Override
    public String getText() {
        return "not implemented";
    }

    /**
     * @param urlParams these parameters are provided by the framework 
     * @param session the session object is provided by the framework
     * @return content of the resource file as string, an empty string if the file could not be loaded.
     */
    public String getText(Map<String, String> urlParams, IHTTPSession session) {
        try {
            return ResourceFileHandler.getResourceAsString(CONTENT_FILE);
        } catch (IOException e) {
            Logger.error(e);
            return "An error occured when attempting to load the resource file " + CONTENT_FILE;
        }
    }

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
        String text = getText(urlParams, session);
        ByteArrayInputStream inp = new ByteArrayInputStream(text.getBytes());
        int size = text.getBytes().length;
        return NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), inp, size);
    }

}
