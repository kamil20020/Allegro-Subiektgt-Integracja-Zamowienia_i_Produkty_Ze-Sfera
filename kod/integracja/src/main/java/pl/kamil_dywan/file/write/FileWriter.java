package pl.kamil_dywan.file.write;

import pl.kamil_dywan.file.read.XMLFileReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public interface FileWriter<T> {

    void save(String filePath, T toSave) throws IOException, URISyntaxException;
    String writeToStr(T value) throws Exception;
}
