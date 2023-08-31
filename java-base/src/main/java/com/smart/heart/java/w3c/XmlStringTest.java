package com.smart.heart.java.w3c;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author Arnold.zhao
 * @version XmlTest.java, v 0.1 2021-12-06 15:41 Arnold.zhao Exp $$
 */
public class XmlStringTest {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);
        factory.setExpandEntityReferences(false);

        DocumentBuilder documentBuilder = factory.newDocumentBuilder();

        InputStream source = new ByteArrayInputStream(lionLog4jXml.getBytes(StandardCharsets.UTF_8));
        Document document = documentBuilder.parse(source);
        System.out.println(document);
        document.createElement("tagName");
        
    }

    private static String lionLog4jXml = "<Configuration status=\"info\" packages=\"com.ymm.common.log4j2,io.manbang.common.logging\">\n" +
            "    <Appenders>\n" +
            "\n" +
            "        <YmmConsole name=\"ymmConsole\" target=\"SYSTEM_OUT\">\n" +
            "            <PatternLayout pattern=\"%d %-5p [%c] [%t] %m%n\"/>\n" +
            "        </YmmConsole>\n" +
            "\n" +
            "        <YmmFile name=\"ymmFile\"></YmmFile>\n" +
            "\n" +
            "        <YmmFileMetric name=\"ymmFileMetric\"></YmmFileMetric>\n" +
            "\n" +
            "        <YmmFileGalileoMetric name=\"ymmFileGalileoMetric\"></YmmFileGalileoMetric>\n" +
            "\n" +
            "        <CatAppender name=\"cat\"></CatAppender>\n" +
            "    </Appenders>\n" +
            "    <Loggers>\n" +
            "        <Root level=\"debug\" includeLocation=\"true\">\n" +
            "            <AppenderRef ref=\"cat\"/>\n" +
            "            <AppenderRef ref=\"ymmConsole\"/>\n" +
            "            <AppenderRef ref=\"ymmFile\"/>\n" +
            "        </Root>\n" +
            "        <logger name=\"com.ymm.common.log.keylog.YmmMetricLogger\" additivity=\"false\">\n" +
            "            <AppenderRef ref=\"ymmFileMetric\"/>\n" +
            "        </logger>\n" +
            "        <logger name=\"__galileo.log.metric__\" additivity=\"false\">\n" +
            "            <AppenderRef ref=\"ymmFileGalileoMetric\"/>\n" +
            "        </logger>\n" +
            "    </Loggers>\n" +
            "</Configuration>";
}
