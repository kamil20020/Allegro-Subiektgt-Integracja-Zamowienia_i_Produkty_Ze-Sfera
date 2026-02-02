package pl.kamil_dywan.api.sfera.request;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.sfera.generated.Customer;
import pl.kamil_dywan.external.sfera.generated.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreateOrderRequestTest {

    @Test
    void shouldCreate() {

        //given
        String reference = "ref";
        String externalId = "external";
        BigDecimal amount = new BigDecimal("22.2");
        Customer customer = new Customer();
        List<Product> products = new ArrayList<>();

        //when
        CreateOrderRequest request = new CreateOrderRequest(reference, externalId, amount, customer, products);

        //then
        assertEquals(reference, request.getReference());
        assertEquals(externalId, request.getExternalId());
        assertEquals(0, amount.compareTo(request.getAmount()));
        assertEquals(customer, request.getCustomer());
        assertEquals(products, request.getProducts());
    }
}