package pl.kamil_dywan.file.write;

import java.io.IOException;
import java.net.URISyntaxException;

public interface FileWriter<T> {

    void save(String filePath, T toSave) throws IOException, URISyntaxException;
    String writeToStr(T value) throws Exception;
}
