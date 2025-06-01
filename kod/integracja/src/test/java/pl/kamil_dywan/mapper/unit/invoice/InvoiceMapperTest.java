package pl.kamil_dywan.mapper.unit.invoice;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import pl.kamil_dywan.external.allegro.generated.Cost;
import pl.kamil_dywan.external.allegro.generated.Payment;
import pl.kamil_dywan.external.allegro.generated.buyer.Buyer;
import pl.kamil_dywan.external.allegro.generated.delivery.Delivery;
import pl.kamil_dywan.external.allegro.generated.delivery.DeliveryTime;
import pl.kamil_dywan.external.allegro.generated.invoice.Invoice;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceAddress;
import pl.kamil_dywan.external.allegro.generated.order_item.Offer;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;
import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.external.allegro.own.Currency;
import pl.kamil_dywan.external.allegro.own.order.OrderMoneyStats;
import pl.kamil_dywan.external.allegro.own.order.OrderTaxSummary;
import pl.kamil_dywan.external.allegro.own.order.OrderTotalMoneyStats;
import pl.kamil_dywan.external.subiektgt.generated.InvoiceReferences;
import pl.kamil_dywan.external.subiektgt.generated.InvoiceTotal;
import pl.kamil_dywan.external.subiektgt.generated.TaxRate;
import pl.kamil_dywan.external.subiektgt.generated.TaxSubTotal;
import pl.kamil_dywan.external.subiektgt.generated.invoice_head.InvoiceHead;
import pl.kamil_dywan.external.subiektgt.generated.invoice_line.InvoiceLine;
import pl.kamil_dywan.external.subiektgt.generated.invoice_line.LineTax;
import pl.kamil_dywan.external.subiektgt.generated.invoice_line.Product;
import pl.kamil_dywan.external.subiektgt.generated.invoice_line.UnitPriceHolder;
import pl.kamil_dywan.external.subiektgt.generated.settlement.Settlement;
import pl.kamil_dywan.external.subiektgt.generated.supplier.Supplier;
import pl.kamil_dywan.external.subiektgt.own.Code;
import pl.kamil_dywan.external.allegro.own.order.OrderItemMoneyStats;
import pl.kamil_dywan.external.subiektgt.own.product.TaxRateCodeMapping;
import pl.kamil_dywan.factory.InvoiceHeadFactory;
import pl.kamil_dywan.factory.InvoiceReferencesFactory;
import pl.kamil_dywan.factory.SettlementFactory;
import pl.kamil_dywan.mapper.*;
import pl.kamil_dywan.mapper.invoice.InvoiceBuyerMapper;
import pl.kamil_dywan.mapper.invoice.InvoiceLineMapper;
import pl.kamil_dywan.mapper.invoice.InvoiceMapper;
import pl.kamil_dywan.mapper.invoice.InvoiceTotalMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

class InvoiceMapperTest {

    @Test
    void shouldMap() {

        String expectedCity = "City 123";

        InvoiceAddress allegroInvoiceAddress = InvoiceAddress.builder()
            .city(expectedCity)
            .build();

        Payment allegroPayment = Payment.builder()
            .finishedAt(OffsetDateTime.now())
            .build();

        Invoice allegroInvoice = Invoice.builder()
            .address(allegroInvoiceAddress)
            .dueDate(LocalDate.now())
            .build();

        Buyer allegroBuyer = Buyer.builder()
            .build();

        Delivery allegroDelivery = Delivery.builder()
            .cost(new Cost(new BigDecimal("0.00"), Currency.PLN))
            .time(new DeliveryTime(OffsetDateTime.now(), null, null, null))
            .build();

        OrderItem orderItem1 = OrderItem.builder()
            .quantity(1)
            .offer(new Offer())
            .build();

        OrderItem orderItem2 = OrderItem.builder()
            .quantity(2)
            .offer(new Offer())
            .build();

        List<OrderItem> allegroOrderItems = new ArrayList<>(List.of(orderItem1, orderItem2));

        // Item x 70 -> for 1 netto 119,10 PLN, brutto 146,49 PLN, tax 23%, tax value 27,39 PLN
        OrderItemMoneyStats orderItemMoneyStats1 = new OrderItemMoneyStats(
            new BigDecimal("23.00"),
            new BigDecimal("146.49"),
            new BigDecimal("119.10"),
            new BigDecimal("10254.51"),
            new BigDecimal("8337.00"),
            new BigDecimal("1917.51")
        );

        // Item x 70 -> for 1 netto 35.98 PLN, brutto 35.98 PLN, tax 0%, tax value 0 PLN
        OrderItemMoneyStats orderItemMoneyStats2 = new OrderItemMoneyStats(
            new BigDecimal("0.00"),
            new BigDecimal("35.98"),
            new BigDecimal("35.98"),
            new BigDecimal("2518.60"),
            new BigDecimal("2518.60"),
            new BigDecimal("0.00")
        );

        OrderItemMoneyStats deliveryOrderItemMoneyStats = new OrderItemMoneyStats(
            new BigDecimal("23"),
            new BigDecimal("0.00"),
            new BigDecimal("0.00"),
            new BigDecimal("0.00"),
            new BigDecimal("0.00"),
            new BigDecimal("0.00")
        );

        List<OrderItemMoneyStats> orderItemMoneyStatsLists = List.of(orderItemMoneyStats1, orderItemMoneyStats2, deliveryOrderItemMoneyStats);

        OrderTaxSummary orderTaxSummary1 = new OrderTaxSummary(
            new BigDecimal(TaxRateCodeMapping.H.getValue()),
            new BigDecimal("12.34"),
            new BigDecimal("22.42"),
            new BigDecimal("36.58")
        );

        OrderTaxSummary orderTaxSummary2 = new OrderTaxSummary(
            new BigDecimal(TaxRateCodeMapping.L.getValue()),
            new BigDecimal("8.36"),
            new BigDecimal("12.48"),
            new BigDecimal("24.68")
        );

        List<OrderTaxSummary> orderTaxSummaries = List.of(orderTaxSummary1, orderTaxSummary2);

        OrderTotalMoneyStats orderTotalMoneyStats = new OrderTotalMoneyStats(
            2,
            2,
            new BigDecimal("12.34"),
            new BigDecimal("24.56"),
            new BigDecimal("36.48")
        );

        OrderMoneyStats orderMoneyStats = new OrderMoneyStats(orderItemMoneyStatsLists, orderTaxSummaries, orderTotalMoneyStats);

        Order allegroOrder = Order.builder()
            .payment(allegroPayment)
            .invoice(allegroInvoice)
            .buyer(allegroBuyer)
            .delivery(allegroDelivery)
            .orderItems(allegroOrderItems)
            .build();

        pl.kamil_dywan.external.subiektgt.generated.buyer.Buyer expectedBuyer = new pl.kamil_dywan.external.subiektgt.generated.buyer.Buyer();

        InvoiceLine expectedInvoiceLine = InvoiceLine.builder()
            .product(new Product())
            .build();

        InvoiceTotal expectedInvoiceTotal = new InvoiceTotal();
        InvoiceHead expectedInvoiceHead = new InvoiceHead();
        InvoiceReferences expectedInvoiceReferences = new InvoiceReferences();
        Settlement expectedSettlement = null;
        TaxSubTotal expectedTaxSubTotal = new TaxSubTotal();

        //when
        try(
            MockedStatic<OrderMoneyStats> mockedOrderMoneyStats = Mockito.mockStatic(OrderMoneyStats.class);
            MockedStatic<InvoiceBuyerMapper> mockedBuyerMapper = Mockito.mockStatic(InvoiceBuyerMapper.class);
            MockedStatic<InvoiceLineMapper> mockedInvoiceLineMapper = Mockito.mockStatic(InvoiceLineMapper.class);
            MockedStatic<TaxSubTotalMapper> mockedTaxSubtotalMapper = Mockito.mockStatic(TaxSubTotalMapper.class);
            MockedStatic<InvoiceTotalMapper> mockedInvoiceTotalMapper = Mockito.mockStatic(InvoiceTotalMapper.class);
            MockedStatic<InvoiceHeadFactory> mockedInvoiceHeadFactory = Mockito.mockStatic(InvoiceHeadFactory.class);
            MockedStatic<InvoiceReferencesFactory> mockedInvoiceReferencesFactory = Mockito.mockStatic(InvoiceReferencesFactory.class);
//            MockedStatic<SettlementFactory> mockedSettlementFactory = Mockito.mockStatic(SettlementFactory.class)
        ){
            mockedBuyerMapper.when(() -> InvoiceBuyerMapper.map(any())).thenReturn(expectedBuyer);
            mockedOrderMoneyStats.when(() -> OrderMoneyStats.getSummary(any())).thenReturn(orderMoneyStats);

            for(int i = 0; i < allegroOrderItems.size(); i++){

                int finalI = i;

                mockedInvoiceLineMapper
                    .when(() -> InvoiceLineMapper.map(eq(finalI + 1), any(), any()))
                    .thenReturn(expectedInvoiceLine);
            }

            for(int i = 0; i < orderTaxSummaries.size(); i++){

                OrderTaxSummary orderTaxSummary = orderTaxSummaries.get(i);

                mockedTaxSubtotalMapper.when(() -> TaxSubTotalMapper.map(orderTaxSummary)).thenReturn(expectedTaxSubTotal);
            }

            mockedInvoiceTotalMapper.when(() -> InvoiceTotalMapper.map(any())).thenReturn(expectedInvoiceTotal);
            mockedInvoiceHeadFactory.when(() -> InvoiceHeadFactory.create(any())).thenReturn(expectedInvoiceHead);
            mockedInvoiceReferencesFactory.when(() -> InvoiceReferencesFactory.create()).thenReturn(expectedInvoiceReferences);
//            mockedSettlementFactory.when(() -> SettlementFactory.create(any())).thenReturn(expectedSettlement);

            pl.kamil_dywan.external.subiektgt.generated.Invoice gotInvoice = InvoiceMapper.map(allegroOrder, expectedCity);

            //then
            assertNotNull(gotInvoice);
            assertNotNull(gotInvoice.getInvoiceLines());
            assertNotNull(gotInvoice.getTaxSubTotals());
            assertNotNull(gotInvoice.getInvoiceTotal());
            assertEquals(expectedInvoiceHead, gotInvoice.getInvoiceHead());
            assertEquals(allegroPayment.getFinishedAt().toLocalDate(), gotInvoice.getInvoiceDate());
            assertEquals(expectedInvoiceReferences, gotInvoice.getInvoiceReferences());
            assertEquals(expectedCity, gotInvoice.getCityOfIssue());
            assertEquals(allegroPayment.getFinishedAt().toLocalDate(), gotInvoice.getTaxPointDate());
            assertEquals(expectedBuyer, gotInvoice.getBuyer());

            for(InvoiceLine invoiceLine : gotInvoice.getInvoiceLines()){

                assertEquals(expectedInvoiceLine, invoiceLine);
            }

            assertEquals("", gotInvoice.getNarrative());
            assertEquals("dokument liczony wg cen brutto", gotInvoice.getSpecialInstructions());
//            assertEquals(expectedSettlement, gotInvoice.getSettlement());
            assertNull(expectedSettlement);

            for(TaxSubTotal taxSubTotal : gotInvoice.getTaxSubTotals()){

                assertEquals(expectedTaxSubTotal, taxSubTotal);
            }

            assertEquals(expectedInvoiceTotal, gotInvoice.getInvoiceTotal());
//            assertEquals("DOSTAWA123", gotInvoice.getInvoiceLines().get(0).getProduct().getSuppliersProductCode());

            mockedBuyerMapper.verify(() -> InvoiceBuyerMapper.map(allegroInvoice));
            mockedOrderMoneyStats.verify(() -> OrderMoneyStats.getSummary(allegroOrder));

            for(int i = 0; i < allegroOrderItems.size() - 1; i++){

                OrderItem allegroOrderItem = allegroOrderItems.get(i);
                OrderItemMoneyStats orderItemMoneyStats = orderItemMoneyStatsLists.get(i);

                int finalI = i + 1;

                mockedInvoiceLineMapper.verify(() -> InvoiceLineMapper.map(finalI, allegroOrderItem, orderItemMoneyStats));
            }

            for(int i = 0; i < orderTaxSummaries.size(); i++){

                OrderTaxSummary orderTaxSummary = orderTaxSummaries.get(i);

                mockedTaxSubtotalMapper.verify(() -> TaxSubTotalMapper.map(orderTaxSummary));
            }

            mockedInvoiceTotalMapper.verify(() -> InvoiceTotalMapper.map(orderTotalMoneyStats));
            mockedInvoiceHeadFactory.verify(() -> InvoiceHeadFactory.create(Code.PLN));
            mockedInvoiceReferencesFactory.verify(() -> InvoiceReferencesFactory.create());
//            mockedSettlementFactory.verify(() -> SettlementFactory.create(allegroInvoice.getDueDate()));
        }
    }
}