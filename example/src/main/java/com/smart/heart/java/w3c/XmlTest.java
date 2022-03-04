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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Arnold.zhao
 * @version XmlTest.java, v 0.1 2021-12-06 15:41 Arnold.zhao Exp $$
 */
public class XmlTest {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

//        disableDtdProcessing(factory);
        factory.setValidating(false);
        factory.setExpandEntityReferences(false);

        /*if (xIncludeAware) {
            enableXInclude(factory);
        }*/
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();

        byte[] buffer = null;
        String location = "W:\\JAVA\\ManBangWrokSpace\\commons\\common-logger-log4j2\\src\\test\\resources\\log4j2-test-20211202.xml";
        FileInputStream inputStream = new FileInputStream(new File(location));

        final InputStream configStream = inputStream;
        try {
            buffer = toByteArray(configStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            Closer.closeSilently(configStream);
        }
        final InputSource source = new InputSource(new ByteArrayInputStream(buffer));
        source.setSystemId(location);

        Document document = documentBuilder.parse(source);
        System.out.println(document);

    }

    protected static byte[] toByteArray(final InputStream is) throws IOException {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        final byte[] data = new byte[16384];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        return buffer.toByteArray();
    }
}
