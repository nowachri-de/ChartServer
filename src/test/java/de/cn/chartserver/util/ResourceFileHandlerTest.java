package de.cn.chartserver.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

public class ResourceFileHandlerTest {
    @Test
    public void loadResourceFileTest() throws IOException{
        String content = ResourceFileHandler.inputStreamToString(ResourceFileHandler.getInputStream("resourceloading/testresource.html"), StandardCharsets.UTF_8);
        Assert.assertEquals("Chartserver Testresource", content);
    }
}
