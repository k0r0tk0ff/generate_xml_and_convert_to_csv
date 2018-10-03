package ru.k0r0tk0ff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import ru.k0r0tk0ff.dao.DbDao;
import ru.k0r0tk0ff.storage.DbDataSource;
import ru.k0r0tk0ff.storage.StorageException;
import ru.k0r0tk0ff.utils.CsvCreator;
import ru.k0r0tk0ff.utils.XsltTransformator;
import ru.k0r0tk0ff.utils.XmlCreator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class Main {
    private final static int MAX_COUNT_OF_ENTRIES = 105000;
    private final static String RESULT_XML_FILE_LOCATION = "data\\result.xml";
    private final static String XML_FILE_PATH = "data\\1.xml";
    private final static String XSLT_FILE_PATH = "data\\xmlTransformation.xslt";
    private final static String TMP_FILE_PATH = "data\\tmp.xml";
    private final static String CSV_TEMPLATE_FILE_PATH = "data\\csvTransformation.xsl";
    private final static String RESULT_CSV_FILE_PATH = "data\\result.csv";
    private static final Logger LOGGER  = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        doCreateXmlFile();
        doTransformXmlFileWithUseXslt();
        doTransformXml2CsvWithCsvTemplate();
    }

    private static void doCreateXmlFile() {
        DbDataSource dbDataSource = new DbDataSource();
        DbDao dao = new DbDao(dbDataSource);
        dao.doCreateAndFillDb(MAX_COUNT_OF_ENTRIES);
        XmlCreator xmlCreator = new XmlCreator(dao);
        try {
            xmlCreator.createXmlFile(XML_FILE_PATH, TMP_FILE_PATH);
            dbDataSource.closeConnection();
        } catch (StorageException e) {
            LOGGER.error("DbDao error!", e);
        } catch (IOException e) {
            LOGGER.error("IO error!", e);
        } catch (XMLStreamException e) {
            LOGGER.error("XML error!", e);
        } catch (TransformerException e) {
            LOGGER.error("Transformer error!", e);
        }
    }

    private static void doTransformXmlFileWithUseXslt() {
        XsltTransformator transformator = new XsltTransformator();
        try {
            transformator.doTransformXmlFile(XML_FILE_PATH,
                    XSLT_FILE_PATH,
                    RESULT_XML_FILE_LOCATION);
        } catch (TransformerException e) {
            LOGGER.error("XsltTransformator error!", e);
        }
    }

    private static void doTransformXml2CsvWithCsvTemplate() {
        CsvCreator csvCreator = new CsvCreator();
        try {
            csvCreator.doTransformXlsToCsv(
                    RESULT_XML_FILE_LOCATION,
                    RESULT_CSV_FILE_PATH,
                    CSV_TEMPLATE_FILE_PATH
            );
        } catch (TransformerException e) {
            LOGGER.error("CSV Transformer error!", e);
        } catch (IOException e) {
            LOGGER.error("CSV IO error!", e);
        } catch (SAXException e) {
            LOGGER.error("CSV SAX error!", e);
        } catch (ParserConfigurationException e) {
            LOGGER.error("CSV Parser error!", e);
        }
    }
}
