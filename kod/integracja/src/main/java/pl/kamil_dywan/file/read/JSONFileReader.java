package pl.kamil_dywan.file.read;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.kamil_dywan.external.allegro.own.serialization.JavaTimeObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class JSONFileReader<T> implements FileReader<T> {

    private static final ObjectMapper objectMapper = new JavaTimeObjectMapper();

    private final Class<T> type;

    public JSONFileReader(Class<T> type){

        this.type = type;
    }

    public T load(String filePath) throws URISyntaxException, IOException {

        File foundFile = FileReader.loadFile(filePath);
        String allegroOrderStr = Files.readString(foundFile.toPath());

        return objectMapper.readValue(allegroOrderStr, type);
    }

    @Override
    public T loadFromOutside(String filePath) throws URISyntaxException, IOException {

        File foundFile = FileReader.loadFileFromOutside(filePath);
        String allegroOrderStr = Files.readString(foundFile.toPath());

        return objectMapper.readValue(allegroOrderStr, type);
    }

    public T loadFromStr(String value) {

        try {
            return objectMapper.readValue(value, type);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
