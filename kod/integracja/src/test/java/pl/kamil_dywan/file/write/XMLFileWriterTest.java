package pl.kamil_dywan.file.write;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.TestUtils;
import pl.kamil_dywan.external.subiektgt.generated.InvoiceBatch;
import pl.kamil_dywan.file.read.FileReader;
import pl.kamil_dywan.file.read.XMLFileReader;

import java.net.URISyntaxException;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.*;

class XMLFileWriterTest {

    private static final String savedInvoiceFilePath = "./data/subiekt/order-minimalized.xml";

    private static final Charset charset = Charset.forName("windows-1250");

    private static String validBatchStr = "";
    private static String validBatchStrWithWhitespace = "";

    static {

        try {
            validBatchStrWithWhitespace = FileReader.loadStrFromFile(savedInvoiceFilePath, charset);
            validBatchStr = TestUtils.removeWhiteSpace(validBatchStrWithWhitespace);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static final FileWriter<InvoiceBatch> fileWriter = new XMLFileWriter<>(InvoiceBatch.class);
    private static final FileReader<InvoiceBatch> fileReader = new XMLFileReader<>(InvoiceBatch.class);

    @Test
    void shouldSave() throws Exception {

        //given
        String toSaveNormalFilePath = "./target/classes/subiekt-test.xml";
        String savedMavenFilePath = "subiekt-test.xml";

        //when
        InvoiceBatch toSaveBatch = fileReader.loadFromStr(validBatchStrWithWhitespace);
        fileWriter.save(toSaveNormalFilePath, toSaveBatch);

        String gotSavedBatchStr = FileReader.loadStrFromFile(savedMavenFilePath, charset);
        gotSavedBatchStr = TestUtils.removeWhiteSpace(gotSavedBatchStr);

        //then
        assertEquals(validBatchStr, gotSavedBatchStr);
    }

    @Test
    public void shouldSaveToString() throws Exception {

        //given

        //when
        InvoiceBatch toSaveBatch = fileReader.loadFromStr(validBatchStrWithWhitespace);
        String gotBatchStr = fileWriter.writeToStr(toSaveBatch);
        gotBatchStr = TestUtils.removeWhiteSpace(gotBatchStr);

        //then
        assertEquals(validBatchStr, gotBatchStr);
    }
}