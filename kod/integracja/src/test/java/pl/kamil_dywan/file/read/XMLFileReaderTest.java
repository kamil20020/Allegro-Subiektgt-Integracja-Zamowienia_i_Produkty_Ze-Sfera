package pl.kamil_dywan.file.read;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.TestUtils;
import pl.kamil_dywan.external.subiektgt.generated.InvoiceBatch;
import pl.kamil_dywan.file.write.FileWriter;
import pl.kamil_dywan.file.write.XMLFileWriter;

import java.net.URISyntaxException;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.*;

class XMLFileReaderTest {

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

    private static final FileReader<InvoiceBatch> fileReader = new XMLFileReader<>(InvoiceBatch.class);
    private static final FileWriter<InvoiceBatch> fileWriter = new XMLFileWriter<>(InvoiceBatch.class);

    @Test
    void shouldLoad() throws Exception {

        //given

        //when
        InvoiceBatch gotBatch = fileReader.load(savedInvoiceFilePath);
        String gotBatchStr = fileWriter.writeToStr(gotBatch);
        gotBatchStr = TestUtils.removeWhiteSpace(gotBatchStr);

        //then
        assertEquals(validBatchStr, gotBatchStr);
    }

    @Test
    void shouldGetFromString() throws Exception {

        //given

        //when
        InvoiceBatch gotBatch = fileReader.loadFromStr(validBatchStrWithWhitespace);
        String gotBatchStr = fileWriter.writeToStr(gotBatch);
        gotBatchStr = TestUtils.removeWhiteSpace(gotBatchStr);

        //then
        assertEquals(validBatchStr, gotBatchStr);
    }
}