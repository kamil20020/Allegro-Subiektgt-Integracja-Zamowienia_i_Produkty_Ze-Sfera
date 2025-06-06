package pl.kamil_dywan.service;

import pl.kamil_dywan.api.Api;
import pl.kamil_dywan.api.allegro.ProductApi;
import pl.kamil_dywan.exception.UnloggedException;
import pl.kamil_dywan.api.allegro.response.OfferProductResponse;
import pl.kamil_dywan.api.allegro.response.ProductOfferResponse;
import pl.kamil_dywan.external.allegro.generated.offer_product.ProductOfferProduct;
import pl.kamil_dywan.external.allegro.generated.offer_product.SellingMode;
import pl.kamil_dywan.external.allegro.generated.order_item.ExternalId;
import pl.kamil_dywan.external.subiektgt.own.product.*;
import pl.kamil_dywan.factory.ProductDetailedPriceFactory;
import pl.kamil_dywan.factory.AllegroProductOfferFactory;
import pl.kamil_dywan.file.write.EppFileWriter;
import pl.kamil_dywan.file.write.FileWriter;
import pl.kamil_dywan.mapper.ProductOfferMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

public class ProductService {

    private final ProductApi productApi;

    private final SferaProductService sferaProductService;

    private static final FileWriter<Object> subiektProductFileWriter;
    private static final ExecutorService productsExecutorService = Executors.newFixedThreadPool(8);

    static {

        List<String> headersNames = List.of("TOWARY", "CENNIK", "GRUPYTOWAROW", "CECHYTOWAROW", "DODATKOWETOWAROW", "TOWARYKODYCN", "TOWARYGRUPYJPKVAT");
        List<Integer> toWriteHeadersIndexes = List.of(0, 1);
        List<Integer> rowsLengths = List.of(43, 7);
        LinkedHashMap<String, Integer[]> writeIndexes = new LinkedHashMap<>();
        writeIndexes.put("TOWARY", new Integer[]{0, 1, 4, 11, 14});

        subiektProductFileWriter = new EppFileWriter<>(headersNames, toWriteHeadersIndexes, rowsLengths, writeIndexes);
    }

    public ProductService(ProductApi productApi, SferaProductService sferaProductService){

        this.productApi = productApi;
        this.sferaProductService = sferaProductService;
    }

    public OfferProductResponse getGeneralProductsPage(int offset, int limit) throws UnloggedException {

        HttpResponse<String> gotResponse = productApi.getOffersProducts(offset, limit);

        return Api.extractBody(gotResponse, OfferProductResponse.class);
    }

    public List<ProductOfferResponse> getDetailedProductsByIds(List<Long> productsIds) throws UnloggedException, IllegalStateException{

        List<Callable<ProductOfferResponse>> productsOffersTasks = new ArrayList<>(productsIds.size());

        for(Long productId : productsIds){

            Callable<ProductOfferResponse> productOfferCallable = () -> {

                return getDetailedProductById(productId);
            };

            productsOffersTasks.add(productOfferCallable);
        }

        List<ProductOfferResponse> gotProductsOffers = new ArrayList<>();

        try{
            List<Future<ProductOfferResponse>> gotProductsOffersFutures = productsExecutorService.invokeAll(productsOffersTasks);

            for(Future<ProductOfferResponse> gotProductOfferFuture : gotProductsOffersFutures){

                if(gotProductOfferFuture.isCancelled()){

                    throw new IllegalStateException("Product detailed fetch was canceled");
                }

                gotProductsOffers.add(gotProductOfferFuture.get());
            }
        }
        catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();

            throw new IllegalStateException("Could not fetch detailed products");
        }

        return gotProductsOffers;
    }

    public ProductOfferResponse getDetailedProductById(Long id) throws UnloggedException{

        HttpResponse<String> gotResponse = productApi.getProductOfferById(id);

        ProductOfferResponse gotProduct = Api.extractBody(gotResponse, ProductOfferResponse.class);

        setSubiektId(gotProduct);

        return gotProduct;
    }

    private void setSubiektId(ProductOfferResponse gotProduct){

        ExternalId externalId = gotProduct.getExternalId();

        String code = gotProduct.getId().toString();

        String producerCode = null;
        String ean = null;

        if(gotProduct.getExternalIdValue() != null){

            producerCode = externalId.getProducerCode();
            ean = externalId.getEanCode();
        }

        if(producerCode != null){

            code = producerCode;
        }

        String foundSubiektId = sferaProductService.getSubiektIdByCodeOrEan(code, ean);

        gotProduct.setSubiektId(foundSubiektId);
    }

    public void setExternalIdForAllOffers(List<ProductOfferResponse> productOfferResponses) throws IllegalStateException {

        if(productOfferResponses == null || productOfferResponses.isEmpty()){

            return;
        }

        List<Callable<Void>> productsOffersTasks = new ArrayList<>(productOfferResponses.size());

        for(ProductOfferResponse productOffer : productOfferResponses){

            Callable<Void> task = () -> {

                setExternalIdForOffer(productOffer);

                return null;
            };

            productsOffersTasks.add(task);
        }

        try{
            List<Future<Void>> gotProductsOffersFutures = productsExecutorService.invokeAll(productsOffersTasks);

            for(Future<Void> gotProductOfferFuture : gotProductsOffersFutures){

                if(gotProductOfferFuture.isCancelled()){

                    throw new IllegalStateException("Product external id fetch was canceled");
                }

                gotProductOfferFuture.get();
            }
        }
        catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();

            throw new IllegalStateException("Could not patch products externals ids");
        }
    }

    public void setExternalIdForOffer(ProductOfferResponse productOffer) throws IllegalStateException{

        String gotProducerCode = productOffer.getProducerCode();
        String gotEanCode = productOffer.getEANCode();

        String combinedKey = ExternalId.getCombinedCode(gotProducerCode, gotEanCode);

        if(combinedKey == null || combinedKey.isEmpty()){

            return;
        }

        HttpResponse<String> gotResponse = productApi.patchOfferExternalById(productOffer.getId(), combinedKey);

        if(gotResponse.statusCode() != 200){
            throw new IllegalStateException("Nie udało się zaktualizować zewnętrznego id dla produktu: " + productOffer.getId());
        }

        ExternalId externalId = new ExternalId(combinedKey);

        productOffer.setExternalId(externalId);
    }

    private List<Object> getEmptyProductRelatedDataForEpp() {

        List<Product> gotConvertedSubiektProducts = new ArrayList<>();
        List<ProductDetailedPrice> productsDetailedPrices = new ArrayList<>();

        List<Object> productRelatedData = new ArrayList<>();

        productRelatedData.add(gotConvertedSubiektProducts);
        productRelatedData.add(productsDetailedPrices);

        return productRelatedData;
    }

    public void writeDeliveryToFile(String filePath) throws IllegalStateException{

        ProductOfferResponse deliveryProductOfferResponse = AllegroProductOfferFactory.createDeliveryProductOffer();

        List<Object> productRelatedData = getEmptyProductRelatedDataForEpp();

        appendProduct(productRelatedData, deliveryProductOfferResponse, ProductType.SERVICES);

        ((List<Product>) productRelatedData.get(0)).get(0).setId("DOSTAWA123");

        writeProductsToFile(productRelatedData, filePath);
    }

    public void writeProductsToFile(List<ProductOfferResponse> productsOffers, String filePath, ProductType productsTypes) throws IllegalStateException{

        List<Object> productRelatedData = getEmptyProductRelatedDataForEpp();

        for(ProductOfferResponse productOfferResponse : productsOffers){

            appendProduct(productRelatedData, productOfferResponse, productsTypes);
        }

        writeProductsToFile(productRelatedData, filePath);
    }

    private void appendProduct(List<Object> productRelatedData, ProductOfferResponse productOfferResponse, ProductType productType){

        Product gotSubiektProduct = ProductOfferMapper.map(productOfferResponse, productType);

        SellingMode sellingMode = productOfferResponse.getSellingMode();
        BigDecimal unitPriceWithTax = sellingMode.getPrice().getAmount();

        ProductDetailedPrice productDetailedRetailPrice = ProductDetailedPriceFactory.create(
            gotSubiektProduct.getId(),
            gotSubiektProduct.getUnitPriceWithoutTax(),
            unitPriceWithTax
        );

        ((List<Product>) productRelatedData.get(0)).add(gotSubiektProduct);
        ((List<ProductDetailedPrice>) productRelatedData.get(1)).add(productDetailedRetailPrice);
    }

    private static void writeProductsToFile(List<Object> productRelatedData, String filePath){

        try {
            subiektProductFileWriter.save(filePath, productRelatedData);
        }
        catch (IOException | URISyntaxException e) {

            e.printStackTrace();

            throw new IllegalStateException(e.getMessage());
        }
    }

}
