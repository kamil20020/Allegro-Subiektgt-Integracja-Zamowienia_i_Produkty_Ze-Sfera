package pl.kamil_dywan.file.write;

import lombok.Getter;
import pl.kamil_dywan.external.subiektgt.own.product.Product;
import pl.kamil_dywan.external.subiektgt.own.product.ProductDetailedPrice;
import pl.kamil_dywan.file.EppGroupSpecialType;
import pl.kamil_dywan.file.EppSerializable;
import pl.kamil_dywan.file.read.EppFileReader;
import pl.kamil_dywan.file.read.FileReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class EppFileWriter<T> implements FileWriter<T>{

    private List<String> headersNames = new ArrayList<>();
    private List<Integer> toWriteHeadersIndexes = new ArrayList<>();
    private List<Integer> rowsLengths = new ArrayList<>();
    private LinkedHashMap<String, Integer[]> writeIndexes = new LinkedHashMap<>();

    private static final String BEFORE_HEADER_HEADER = "[NAGLOWEK]";
    private static final String BEFORE_INFO_HEADER = "[INFO]";
    private static final String BEFORE_CONTENT_HEADER = "[ZAWARTOSC]";

    public EppFileWriter(List<String> headersNames, List<Integer> toWriteHeadersIndexes, List<Integer> rowsLengths, LinkedHashMap<String, Integer[]> writeIndexes){

        this.headersNames = headersNames;
        this.toWriteHeadersIndexes = toWriteHeadersIndexes;
        this.rowsLengths = rowsLengths;
        this.writeIndexes = writeIndexes;
    }

    public EppFileWriter(){


    }

    @Override
    public void save(String filePath, Object toSaveObj) throws IOException, IllegalStateException {

        File newFile = new File(filePath);

        if(!newFile.exists()){

            newFile.createNewFile();
        }

        if(!(toSaveObj instanceof List<?>)){

            throw new IllegalStateException("Epp root object should be list");
        }

        List<?> toSaveObjects = (List<?>) toSaveObj;

        try(BufferedWriter bufferedWriter = new BufferedWriter(new java.io.FileWriter(newFile, Charset.forName("windows-1250")))){

            bufferedWriter.write(BEFORE_INFO_HEADER);
            bufferedWriter.newLine();
            bufferedWriter.newLine();

            if(toWriteHeadersIndexes == null){

                drawAllRows(toSaveObjects, bufferedWriter);
            }
            else{

                drawSelectedRows(toSaveObjects, bufferedWriter);
            }
        }
    }

    private void drawAllRows(List<?> rootObjects, BufferedWriter bufferedWriter) throws IOException {

        for(int headerIndex = 0, fieldIndex = 0; headerIndex < headersNames.size(); headerIndex++, fieldIndex++){

            drawHeader(headerIndex, bufferedWriter);

            Object toSave = rootObjects.get(headerIndex);

            handleRootFieldException(toSave, headerIndex, bufferedWriter);

            bufferedWriter.newLine();
        }
    }

    private void drawSelectedRows(List<?> rootObjects, BufferedWriter bufferedWriter) throws IOException {

        for(int headerIndex = 0, cellIndex = 0; headerIndex < headersNames.size(); headerIndex++){

            drawHeader(headerIndex, bufferedWriter);

            if(cellIndex < toWriteHeadersIndexes.size()){

                int toWriteHeaderIndex = toWriteHeadersIndexes.get(cellIndex);

                if(headerIndex == toWriteHeaderIndex){

                    Object toSave = rootObjects.get(toWriteHeaderIndex);

                    handleRootFieldException(toSave, headerIndex, bufferedWriter);
                    cellIndex++;
                }
            }

            bufferedWriter.newLine();
        }
    }

    private void drawHeader(int headerIndex, BufferedWriter bufferedWriter) throws IOException {

        String headerName = headersNames.get(headerIndex);

        if(headerName.equals(EppGroupSpecialType.EMPTY_HEADER.toString())){

            bufferedWriter.write(BEFORE_CONTENT_HEADER);
            bufferedWriter.newLine();

            return;
        }

        bufferedWriter.write(BEFORE_HEADER_HEADER);
        bufferedWriter.newLine();

        if(headerName.equals(EppGroupSpecialType.EMPTY_CONTENT.toString())){

            return;
        }

        bufferedWriter.write('"' + headerName + '"');
        bufferedWriter.newLine();
        bufferedWriter.newLine();
        bufferedWriter.write(BEFORE_CONTENT_HEADER);
        bufferedWriter.newLine();
    }

    private void handleRootFieldException(Object toSave, int headerIndex, BufferedWriter bufferedWriter){

        try {
            handleRootField(toSave, headerIndex, bufferedWriter);
        }
        catch (IllegalAccessException | IOException e) {

            e.printStackTrace();

            throw new IllegalStateException(e.getMessage());
        }
    }

    private void handleRootField(Object toSave, int headerIndex, BufferedWriter bufferedWriter) throws IllegalAccessException, IOException {

        if(toSave instanceof Collection<?> objCollection){

            for(Object childObj : objCollection){

                handleObjFields(headerIndex, childObj, bufferedWriter);
            }
        }
        else{
            handleObjFields(headerIndex, toSave, bufferedWriter);
        }
    }

    private void handleObjFields(int headerIndex, Object toSave, BufferedWriter bufferedWriter) throws IOException, IllegalAccessException {

        String headerName = headersNames.get(headerIndex);

        Class<?> toSaveObjType = toSave.getClass();

        Field[] childFields = toSaveObjType.getDeclaredFields();

        if(writeIndexes.containsKey(headerName)){

            drawSelectedChildFields(headerIndex, childFields, bufferedWriter, toSave);
        }
        else{

            drawAllChildFields(childFields, bufferedWriter, toSave);
        }

        bufferedWriter.newLine();
    }

    private void drawSelectedChildFields(int headerIndex, Field[] childFields, BufferedWriter bufferedWriter, Object childObj) throws IOException, IllegalAccessException {

        String headerName = headersNames.get(headerIndex);

        Integer[] writeIndexesForContent = writeIndexes.get(headerName);

        int rowLength = rowsLengths.get(headerIndex);

        for(int i = 0, j = 0; i < rowLength; i++){

            if(j < writeIndexesForContent.length){

                int minimumWriteIndex = writeIndexesForContent[j];

                if(i == minimumWriteIndex){

                    Field childField = childFields[j];

                    handleDrawChildField(childField, bufferedWriter, childObj);
                    j++;
                }
            }

            if(i < rowsLengths.get(headerIndex) - 1){
                bufferedWriter.write(',');
            }
        }
    }

    private void drawAllChildFields(Field[] childFields, BufferedWriter bufferedWriter, Object childObj) throws IOException, IllegalAccessException {

        for(int i = 0; i < childFields.length; i++){

            Field childField = childFields[i];

            handleDrawChildField(childField, bufferedWriter, childObj);

            if(i < childFields.length - 1){
                bufferedWriter.write(',');
            }
        }
    }

    private void handleDrawChildField(Field childField, BufferedWriter bufferedWriter, Object childObj) throws IllegalAccessException, IOException {

        childField.setAccessible(true);

        Object value = childField.get(childObj);

        if(value != null){

            bufferedWriter.write(value.toString());
        }
    }

    @Override
    public String writeToStr(Object value) throws Exception {


        return null;
    }

    public void append(EppFileWriter<?> otherFileWriter, int toWriteHeadersIndexesOffset){

        List<Integer> toWriteHeadersIndexes = otherFileWriter.toWriteHeadersIndexes.stream()
            .map(toWriteIndex -> toWriteIndex + toWriteHeadersIndexesOffset)
            .toList();

        this.headersNames.addAll(otherFileWriter.headersNames);
        this.toWriteHeadersIndexes.addAll(toWriteHeadersIndexes);
        this.rowsLengths.addAll(otherFileWriter.rowsLengths);
        this.writeIndexes.putAll(otherFileWriter.writeIndexes);
    }

    public void appendHeaderNames(EppFileWriter<?> otherFileWriter){

        this.headersNames.addAll(otherFileWriter.headersNames);
    }

    public static void main(String[] args) throws URISyntaxException, IOException {

//        LinkedHashMap<String, Class<? extends EppSerializable>> schema = new LinkedHashMap<>();
//        schema.put("TOWARY", Product.class);
//        schema.put("CENNIK", ProductDetailedPrice.class);
//
//        LinkedHashMap<String, Integer[]> readIndexes = new LinkedHashMap<>();
//        readIndexes.put("TOWARY", new Integer[]{0, 1, 4, 11, 14});
//
//        FileReader<ProductRelatedData> eppFileReader = new EppFileReader<>(schema, readIndexes, ProductRelatedData.class);
//
//        ProductRelatedData productRelatedData = eppFileReader.load("data/subiekt/product.epp");
//        System.out.println(productRelatedData);
    }

}
