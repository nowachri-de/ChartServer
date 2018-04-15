package de.cn.chartserver.handler;

import java.io.IOException;
import java.util.Map;

import org.pmw.tinylog.Logger;

import de.cn.chartserver.resource.ResourceFileHandler;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;

/**
 * Handler to send an example page to the client (browser)
 *
 */
public class ExampleHandler extends BaseHandler {

	protected final static String CONTENT_FILE = "html/samples/example1.html";

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
