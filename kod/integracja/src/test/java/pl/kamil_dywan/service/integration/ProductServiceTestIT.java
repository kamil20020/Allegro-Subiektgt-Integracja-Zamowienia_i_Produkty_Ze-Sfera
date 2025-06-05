package pl.kamil_dywan.service.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.kamil_dywan.api.allegro.response.ProductOfferResponse;
import pl.kamil_dywan.external.subiektgt.own.product.ProductType;
import pl.kamil_dywan.file.read.FileReader;
import pl.kamil_dywan.file.read.JSONFileReader;
import pl.kamil_dywan.service.ProductService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTestIT {

    private static final String validProductsFilePath = "data/subiekt/product-minimalized.epp";
    private static final String validDeliveryFilePath = "data/subiekt/delivery.epp";
    private static final String allegroProductOfferFilePath = "data/allegro/product-detailed-offer-minimalized.json";

    private static String expectedProducts = "";
    private static String expectedDelivery = "";

    static {

        try {
            expectedProducts = FileReader.loadStrFromFile(validProductsFilePath);
            expectedDelivery = FileReader.loadStrFromFile(validDeliveryFilePath);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private final FileReader<ProductOfferResponse> allegroProductOfferReader = new JSONFileReader<>(ProductOfferResponse.class);

    private ProductService productService;

    @BeforeEach
    public void setUp(){

        productService = new ProductService(null, null);
    }

    @Test
    void shouldWriteDeliveryToFile(){

        //given
        String toSaveFilePath = "test-delivery.epp";

        //when
        productService.writeDeliveryToFile(toSaveFilePath);

        String gotDeliveryStr = FileReader.loadStrFromFileOutside(toSaveFilePath);

        //then
        assertEquals(expectedDelivery, gotDeliveryStr);
    }

    @Test
    void writeProductsToFile() throws URISyntaxException, IOException {

        //given
        String toSaveFilePath = "test-products.epp";

        ProductOfferResponse allegroProductOfferResponse = allegroProductOfferReader.load(allegroProductOfferFilePath);

        List<ProductOfferResponse> allegroProductsOffers = List.of(allegroProductOfferResponse);

        //when
        productService.writeProductsToFile(allegroProductsOffers, toSaveFilePath, ProductType.GOODS);

        String gotProductsStr = FileReader.loadStrFromFileOutside(toSaveFilePath);

        //then
        assertEquals(expectedProducts, gotProductsStr);
    }

}