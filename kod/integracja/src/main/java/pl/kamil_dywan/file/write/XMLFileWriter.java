package pl.kamil_dywan.file.write;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import pl.kamil_dywan.file.write.FileWriter;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

public class XMLFileWriter<T> implements FileWriter<T> {

    private Marshaller marshaller;

    public XMLFileWriter(Class<T> type) {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(type);
            marshaller = jaxbContext.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_ENCODING, "windows-1250");
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
//            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        }
        catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void save(String filePath, T toSave) throws IOException {

        File file = new File(filePath);

        if(!file.exists()){

            file.createNewFile();
        }

        try {
            marshaller.marshal(toSave, file);
        }
        catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String writeToStr(T value) throws Exception {

        try(StringWriter stringWriter = new StringWriter()){

            marshaller.marshal(value, stringWriter);

            return stringWriter.toString();
        }
        catch (JAXBException e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
