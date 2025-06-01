package pl.kamil_dywan.file.read;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface FileReader<T> {

    static String loadStrFromFileOutside(String filePath){

        return loadStrFromFileOutside(filePath, StandardCharsets.UTF_8);
    }

    static String loadStrFromFileOutside(String filePath, Charset charset){

        File file = loadFileFromOutside(filePath);

        try {
            return Files.readString(file.toPath(), charset);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    static File loadFileFromOutside(String filePath){

        Path foundFilePath = Paths.get(filePath);

        return foundFilePath.toFile();
    }

    static String loadStrFromFile(String filePath) throws URISyntaxException{

        return loadStrFromFile(filePath, StandardCharsets.UTF_8);
    }

    static String loadStrFromFile(String filePath, Charset charset) throws URISyntaxException{

        File file = loadFile(filePath);

        try {
            return Files.readString(file.toPath(), charset);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    static File loadFile(String filePath) throws URISyntaxException{

        URL fileURL = XMLFileReader.class.getClassLoader().getResource(filePath);
        URI fileURI = fileURL.toURI();
        Path foundFilePath = Path.of(fileURI);

        return foundFilePath.toFile();
    }

    T load(String filePath) throws URISyntaxException, IOException;
    T loadFromOutside(String filePath) throws URISyntaxException, IOException;
    T loadFromStr(String value) throws Exception;
}
