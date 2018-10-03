package ru.k0r0tk0ff.utils;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

public class XsltTransformator {
    public void doTransformXmlFile(String sourceFilePath, String xsltFilePath, String outFilePath) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(new StreamSource(new File(xsltFilePath)));
        StreamSource source = new StreamSource(new File(sourceFilePath));
        StreamResult result = new StreamResult(new File(outFilePath));
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","4");
        transformer.setOutputProperty(OutputKeys.STANDALONE,"yes");
        transformer.transform(source, result);
    }
}
