package pl.kamil_dywan;

import pl.kamil_dywan.api.BasicAuthApi;
import pl.kamil_dywan.api.BearerAuthApi;
import pl.kamil_dywan.api.allegro.LoginApi;
import pl.kamil_dywan.api.allegro.OrderApi;
import pl.kamil_dywan.api.allegro.ProductApi;
import pl.kamil_dywan.gui.MainGui;
import pl.kamil_dywan.service.*;

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

        BasicInfoService basicInfoService = new BasicInfoService();
        BasicInfoService.init();

        BearerAuthApi.init(loginApi::refreshAccessToken);
        BasicAuthApi.init();

        AuthService authService = new AuthService(loginApi);
        authService.init();

        OrderService orderService = new OrderService(orderApi);
        ProductService productService = new ProductService(productApi);
        InvoiceService invoiceService = new InvoiceService(basicInfoService);
        ReceiptService receiptService = new ReceiptService(basicInfoService);

        new MainGui(authService, productService, orderService, invoiceService, receiptService, basicInfoService);
    }

}
