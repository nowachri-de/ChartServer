package de.cn.chartserver.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

/**
 * Class to load files being located under src/main/resources
 *
 */
public class ResourceFileHandler {

    /**
     * @param resourceName Name of the file beeing located in the resources folder (assuming maven project layout)
     * @return Inputstream
     */
    public static InputStream getInputStream(String resourceFileName) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(resourceFileName);
        return is;
    }

    /**
     * Return the content of the inputstream as String.
     * 
     * @param inputStream Inputstream to be read
     * @param charset Charset of the inputstream underlying characters
     * @return content of Inputstream as String
     * @throws IOException
     */
    public static String inputStreamToString(InputStream inputStream, Charset charset) throws IOException {
        return IOUtils.toString(inputStream, charset);
    }

    /**
     * @param resourceFileName Ressource file to be loaded. If the resource is located in a subfolder of src/main/resources like
     *        src/main/resources/example/somefile.txt then specify the path to the file like: example/somefile.txt. Do not start the path with a leading /
     * @return content of file as String
     * @throws IOException
     */
    public static String getResourceAsString(String resourceFileName) throws IOException {
        return ResourceFileHandler.inputStreamToString(ResourceFileHandler.getInputStream(resourceFileName), StandardCharsets.UTF_8);
    }
}
