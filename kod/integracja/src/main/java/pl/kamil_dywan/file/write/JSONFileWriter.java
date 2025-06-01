package pl.kamil_dywan.file.write;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.kamil_dywan.external.allegro.own.serialization.JavaTimeObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JSONFileWriter<T> implements pl.kamil_dywan.file.write.FileWriter<T> {

    private static final ObjectMapper objectMapper = new JavaTimeObjectMapper();

    public void save(String filePath, T toSave) throws IOException {

        File file = new File(filePath);

        if(!file.exists()){
            file.createNewFile();
        }

        String toSaveStr = objectMapper.writeValueAsString(toSave);

        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))){

            bufferedWriter.write(toSaveStr);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public String writeToStr(T value) throws IOException {

        return objectMapper.writeValueAsString(value);
    }
}
