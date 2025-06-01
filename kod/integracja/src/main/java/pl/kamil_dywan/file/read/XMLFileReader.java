package pl.kamil_dywan.file.read;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import pl.kamil_dywan.file.read.FileReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class XMLFileReader<T> implements FileReader<T> {

    private Unmarshaller unmarshaller;

    public XMLFileReader(Class<T> type){

        try {
            // unmarshall, note that it's better to reuse JAXBContext, as newInstance()
            // calls are pretty expensive

            JAXBContext jc = JAXBContext.newInstance(type);
            unmarshaller = jc.createUnmarshaller();
        }
        catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private static class XMLReaderWithoutNamespace extends StreamReaderDelegate {

        public XMLReaderWithoutNamespace(XMLStreamReader reader) {

            super(reader);
        }

        @Override
        public String getAttributeNamespace(int arg0) {

            return "";
        }

        @Override
        public String getNamespaceURI() {

            return "";
        }
    }

    @Override
    public T load(String filePath) throws URISyntaxException, IOException {

        File foundFile = FileReader.loadFile(filePath);

       return load(foundFile);
    }

    @Override
    public T loadFromOutside(String filePath) throws URISyntaxException, IOException {

        File file = FileReader.loadFileFromOutside(filePath);

        return load(file);
    }

    public T load(File file) throws URISyntaxException, IOException {

        T result = null;

        try(FileInputStream is = new FileInputStream(file)){

            XMLStreamReader xsr = XMLInputFactory.newFactory()
                .createXMLStreamReader(is, "windows-1250");

            XMLReaderWithoutNamespace xr = new XMLReaderWithoutNamespace(xsr);

            result = (T) unmarshaller.unmarshal(xr);
        }
        catch(JAXBException | XMLStreamException e){
            e.printStackTrace();
        }

        return result;
    }

    public T loadFromStr(String value) {

        value = value.stripLeading();

        if(value.startsWith("<?xml")){

            int endIndex = value.indexOf("?>");

            value = value.substring(endIndex + 2);
        }

        try(StringReader stringReader = new StringReader(value)) {

            XMLStreamReader xsr = XMLInputFactory.newFactory().createXMLStreamReader(stringReader);
            XMLReaderWithoutNamespace xr = new XMLReaderWithoutNamespace(xsr);

            return (T) unmarshaller.unmarshal(xr);
        }
        catch (JAXBException | XMLStreamException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
