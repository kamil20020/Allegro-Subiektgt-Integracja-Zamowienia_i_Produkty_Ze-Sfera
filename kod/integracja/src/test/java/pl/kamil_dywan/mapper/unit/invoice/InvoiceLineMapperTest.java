package pl.kamil_dywan.mapper.unit.invoice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import pl.kamil_dywan.external.allegro.generated.Cost;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;
import pl.kamil_dywan.external.allegro.generated.order_item.Offer;
import pl.kamil_dywan.external.allegro.generated.order_item.Tax;
import pl.kamil_dywan.external.allegro.own.Currency;
import pl.kamil_dywan.external.subiektgt.generated.invoice_line.*;
import pl.kamil_dywan.external.allegro.own.order.OrderItemMoneyStats;
import pl.kamil_dywan.external.subiektgt.own.product.TaxRateCodeMapping;
import pl.kamil_dywan.factory.LineTaxFactory;
import pl.kamil_dywan.factory.PercentDiscountFactory;
import pl.kamil_dywan.mapper.invoice.InvoiceLineMapper;
import pl.kamil_dywan.mapper.invoice.InvoiceLineQuantityMapper;
import pl.kamil_dywan.mapper.invoice.InvoiceProductMapper;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class InvoiceLineMapperTest {

    @Test
    void shouldMap() {

        //given
        Integer invoiceLineNumber = 1;

        Offer allegroOffer = Offer.builder()
            .name("Offer 123")
            .build();

        OrderItem allegroOrderItem = OrderItem.builder()
            .offer(allegroOffer)
            .build();

        // Mouse x 2 -> for 1 netto 30 PLN, brutto 36,9 PLN, tax 6,9 PLN
        OrderItemMoneyStats orderItemMoneyStats = new OrderItemMoneyStats(
            new BigDecimal("23.00"),
            new BigDecimal("36.90"),
            new BigDecimal("30.00"),
            new BigDecimal("73.80"),
            new BigDecimal("60.00"),
            new BigDecimal("13.80")
        );

        InvoiceLineQuantity expectedInvoiceLineQuantity = new InvoiceLineQuantity();
        LineTax expectedLineTax = new LineTax();
        Product expectedProduct = new Product();
        PercentDiscount expectedPercentDiscount = new PercentDiscount();

        //when
        try(
            MockedStatic<InvoiceLineQuantityMapper> mockedInvoiceLineQuantityMapper = Mockito.mockStatic(InvoiceLineQuantityMapper.class);
            MockedStatic<LineTaxFactory> mockedLineTaxFactory = Mockito.mockStatic(LineTaxFactory.class);
            MockedStatic<InvoiceProductMapper> mockedProductMapper = Mockito.mockStatic(InvoiceProductMapper.class);
            MockedStatic<PercentDiscountFactory> mockedPercentDiscountFactory = Mockito.mockStatic(PercentDiscountFactory.class);
        ){
            mockedInvoiceLineQuantityMapper.when(() -> InvoiceLineQuantityMapper.map(any())).thenReturn(expectedInvoiceLineQuantity);
            mockedLineTaxFactory.when(() -> LineTaxFactory.create(any(), any())).thenReturn(expectedLineTax);
            mockedProductMapper.when(() -> InvoiceProductMapper.map(any())).thenReturn(expectedProduct);
            mockedPercentDiscountFactory.when(PercentDiscountFactory::create).thenReturn(expectedPercentDiscount);

            InvoiceLine gotInvoiceLine = InvoiceLineMapper.map(invoiceLineNumber, allegroOrderItem, orderItemMoneyStats);

            //then
            assertNotNull(gotInvoiceLine);
            assertNotNull(gotInvoiceLine.getUnitPrice());
            assertEquals(invoiceLineNumber, gotInvoiceLine.getLineNumber());
            assertEquals(expectedInvoiceLineQuantity, gotInvoiceLine.getQuantity());
            assertEquals(expectedProduct, gotInvoiceLine.getProduct());
            assertEquals(expectedPercentDiscount, gotInvoiceLine.getPercentDiscount());
            assertEquals(expectedLineTax, gotInvoiceLine.getLineTax());
            assertEquals(orderItemMoneyStats.getTotalPriceWithTax(), gotInvoiceLine.getLineTotal());
            assertEquals(orderItemMoneyStats.getUnitPriceWithoutTax(), gotInvoiceLine.getUnitPrice().getUnitPrice());
            assertEquals(allegroOffer.getName(), gotInvoiceLine.getInvoiceLineInformation());

            mockedInvoiceLineQuantityMapper.verify(() -> InvoiceLineQuantityMapper.map(allegroOrderItem));
            mockedLineTaxFactory.verify(() -> LineTaxFactory.create(orderItemMoneyStats.getTotalTaxValue(), TaxRateCodeMapping.H));
            mockedProductMapper.verify(() -> InvoiceProductMapper.map(allegroOffer));
            mockedPercentDiscountFactory.verify(PercentDiscountFactory::create);
        }
    }
}