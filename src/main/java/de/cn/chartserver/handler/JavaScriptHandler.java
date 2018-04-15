package de.cn.chartserver.handler;

import java.io.IOException;
import java.util.Map;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.pmw.tinylog.Logger;

import de.cn.chartserver.resource.ResourceFileHandler;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;

/**
 * Handler to send an example page to the client (browser)
 *
 */
public class JavaScriptHandler extends BaseHandler {

	protected final static String CONTENT_FILE="html/javascript/chartserver.js";
	@Override
	public String getText(Map<String, String> urlParams, IHTTPSession session) {
		try {
			String content =  ResourceFileHandler.getResourceAsString(CONTENT_FILE);
			if (configuration != null){
				JtwigTemplate template = JtwigTemplate.inlineTemplate(content);
			    JtwigModel model = JtwigModel.newModel().with("wsport", configuration.getWebSocketPort());

			    return template.render(model);
			}
			return "Internal Error";
		        
		} catch (IOException e) {
			Logger.error(e);
			return "An error occured when attempting to load the resource file " + CONTENT_FILE;
		}
	}
}
