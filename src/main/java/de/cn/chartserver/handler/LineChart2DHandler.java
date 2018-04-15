package de.cn.chartserver.handler;

import java.io.IOException;
import java.util.Map;

import org.pmw.tinylog.Logger;

import de.cn.chartserver.resource.ResourceFileHandler;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;

/**
 * Handler to send an linechart2d html page to the client (browser)
 *
 */
public class LineChart2DHandler extends BaseHandler {
    public static final String URL="/linechart2d";
    protected static final String CONTENT_FILE = "html/chart/linechart2d.html";
    
    @Override
	public String getText(Map<String, String> urlParams, IHTTPSession session) {
		try {
			return ResourceFileHandler.getResourceAsString(CONTENT_FILE);
		} catch (IOException e) {
			Logger.error(e);
			return "An error occured when attempting to load the resource file " + CONTENT_FILE;
		}
	}
}
