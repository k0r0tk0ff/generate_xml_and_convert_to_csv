package ru.k0r0tk0ff.utils;

import ru.k0r0tk0ff.dao.DbDao;
import ru.k0r0tk0ff.domain.RawEntry;
import ru.k0r0tk0ff.storage.StorageException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.List;

public class XmlCreator {
    private DbDao dao;

    public XmlCreator(DbDao dao) {
        this.dao = dao;
    }

    public void createXmlFile(String filename, String tmpFileName) throws StorageException, IOException, XMLStreamException, TransformerException {
        List<RawEntry> dataFromDb = dao.getDataFromDb();

        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = factory.createXMLStreamWriter(new FileWriter(tmpFileName));
        //ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        //XMLStreamWriter writer = factory.createXMLStreamWriter(buffer);

        writer.writeStartDocument("UTF8", "1.0");
        writer.writeStartElement("articles");
        for (RawEntry rawEntry : dataFromDb) {
            writer.writeStartElement("article");
            writer.writeAttribute("id_art", String.valueOf(rawEntry.getId_art()));
            writer.writeAttribute("name", rawEntry.getName());
            writer.writeAttribute("code", String.valueOf(rawEntry.getCode()));
            writer.writeAttribute("username", rawEntry.getUserName());
            writer.writeAttribute("guid", rawEntry.getGuid());
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeEndDocument();
        writer.close();

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform(new StreamSource(
                        new BufferedInputStream(new FileInputStream(tmpFileName))),
                new StreamResult(new FileOutputStream(filename))
        );
    }
}
