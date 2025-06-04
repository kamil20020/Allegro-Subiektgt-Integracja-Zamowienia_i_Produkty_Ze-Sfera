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

//        SecureStorage.saveCredentials("access_token", "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiIxMDgxNDAzODIiLCJzY29wZSI6WyJhbGxlZ3JvOmFwaTpvcmRlcnM6cmVhZCIsImFsbGVncm86YXBpOnNhbGU6b2ZmZXJzOndyaXRlIiwiYWxsZWdybzphcGk6cHJvZmlsZTpyZWFkIiwiYWxsZWdybzphcGk6c2FsZTpvZmZlcnM6cmVhZCIsImFsbGVncm86YXBpOm9yZGVyczp3cml0ZSJdLCJhbGxlZ3JvX2FwaSI6dHJ1ZSwiaXNzIjoiaHR0cHM6Ly9hbGxlZ3JvLnBsLmFsbGVncm9zYW5kYm94LnBsIiwiZXhwIjoxNzQ5MDIzNzMwLCJqdGkiOiI4NDYyZjQ4NC0wNTFlLTQ3N2ItYTdlMi0wYzBjZmEyMjJkODIiLCJjbGllbnRfaWQiOiJjNjI1MzA5OTEyMTM0ZWNmOTA0NmY3YzFjMWU0MmM2NCJ9.DamLuVK7xcSSVa-bfNS8WLHpHiKJcyUSn4XWBfrBbiVl_fvRZWegBYAbVTsJF4luwWgR0aH7f_VwOpdPWvAnpMsNmm7eAfOgqKBcsv9aEHHjwtQloqZnvWLXEN84Wpy8aqlBUht5ay0aJUat0ehQy2RpfltzibvIDe0dSEJUrGdX0HKr3P5u31H9JMPuTZSz9gNWQLu0xaDZs7YjZIyfJNhYuBWlB2q5hltBvYsBHfarJgnMcWq3OVGME5eAeH-Hhmo8B7-Puid9MRSmCMEv4rBVFYhCpRS5cKSGvZzyuCcxteSWP9uxTGA-HUl6D9cWX59-mYpCvUny8VxhNUvZkWfhU01rl0JkIKrZ0F-L3c_wu-S3LCZdMQmymRSXlww7igbovIBufX84OnEKKqLtrW9BoE7QofJMS5KmAdiutu9aNrLGBhkCMJG7MNWySLEgiDe6lXUOwSPIKHcvk80b5Toqm3-8VuPL-x2Uz1rZz2sUdTQmxnES8FM1Zhc6yDAgX_YTvff1iRCR6IgrFtU5TWhmhK62zKubw4K5lBzHl9X1Vx7RqmRwGJlyFi-rQoCEy_SJMSW2yCAGCbegQx7WfcGpoLCyujgYDdTpmCoRVgV5dCVQrNAUWBXzycbPuNR41eZXo7fVgn12iSSvXv6DvjxFf-3OZWRdev1KNXCQwP0");

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

        SferaOrderService sferaOrderService = new SferaOrderService(sferaOrderApi);
        OrderService orderService = new OrderService(orderApi, sferaOrderService);
        ProductService productService = new ProductService(productApi);
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
