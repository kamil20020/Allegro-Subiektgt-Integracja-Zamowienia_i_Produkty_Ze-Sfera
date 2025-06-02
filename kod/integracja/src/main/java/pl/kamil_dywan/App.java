package pl.kamil_dywan;

import pl.kamil_dywan.api.allegro.BasicAuthApi;
import pl.kamil_dywan.api.allegro.BearerAuthApi;
import pl.kamil_dywan.api.allegro.LoginApi;
import pl.kamil_dywan.api.allegro.OrderApi;
import pl.kamil_dywan.api.allegro.ProductApi;
import pl.kamil_dywan.api.sfera.SferaOrderApi;
import pl.kamil_dywan.api.sfera.request.CreateOrderRequest;
import pl.kamil_dywan.external.sfera.generated.Customer;
import pl.kamil_dywan.external.sfera.generated.Product;
import pl.kamil_dywan.gui.MainGui;
import pl.kamil_dywan.service.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {

        AppProperties.loadProperties();
        SecureStorage.load();

        LoginApi loginApi = new LoginApi();
        OrderApi orderApi = new OrderApi();
        ProductApi productApi = new ProductApi();
        SferaOrderApi sferaOrderApi = new SferaOrderApi();

        BasicInfoService basicInfoService = new BasicInfoService();
        BasicInfoService.init();

        BearerAuthApi.init(loginApi::refreshAccessToken);
        BasicAuthApi.init();
        SferaOrderApi.init();

        AuthService authService = new AuthService(loginApi);
        authService.init();

        OrderService orderService = new OrderService(orderApi);
        ProductService productService = new ProductService(productApi);
        SferaOrderService sferaOrderService = new SferaOrderService(sferaOrderApi);

        new MainGui(authService, productService, orderService, sferaOrderService, basicInfoService);
//
//        Customer customer = Customer.builder()
//            .name("Kamil Nowak")
//            .street("Ulica jakaś")
//            .nip("123")
//            .city("Katowice")
//            .postCode("12-345")
//            .isInvoiceRequired(false)
//            .build();
//
//        Product product1 = Product.builder()
//            .code("BANAW200")
//            .name("Balsam do ciała nawilżający 200 ml")
//            .priceWithTax(new BigDecimal("20.90"))
//            .quantity(1)
//            .build();
//
//        Product product2 = Product.builder()
//            .code("DOSTAWA123")
//            .name("Dostawa")
//            .priceWithTax(new BigDecimal("9.50"))
//            .quantity(1)
//            .build();
//
//        List<Product> products = List.of(product1, product2);
//
//        CreateOrderRequest createOrderRequest = new CreateOrderRequest(
//            new BigDecimal("30.40"),
//            customer,
//            products
//        );
//
//        sferaOrderApi.create(createOrderRequest);
    }

}
