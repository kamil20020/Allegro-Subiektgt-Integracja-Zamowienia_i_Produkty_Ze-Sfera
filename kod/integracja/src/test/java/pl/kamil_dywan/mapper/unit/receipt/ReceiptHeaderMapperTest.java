package pl.kamil_dywan.mapper.unit.receipt;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.allegro.own.order.OrderTotalMoneyStats;
import pl.kamil_dywan.external.subiektgt.own.receipt.ReceiptHeader;
import pl.kamil_dywan.mapper.receipt.ReceiptHeaderMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptHeaderMapperTest {

    @Test
    void shouldMap() {

        //given
        OrderTotalMoneyStats orderTotalMoneyStats = new OrderTotalMoneyStats(
            2,
            3,
            new BigDecimal("23.34"),
            new BigDecimal("34.36"),
            new BigDecimal("48.36")
        );

        int actualYear = LocalDate.now().getYear();
        String expectedId = "1/" + actualYear;

        String expectedTimestamp = "20250601";

        String expectedSellerCity = "Miasto 123";

        //when
        ReceiptHeader gotHeader = ReceiptHeaderMapper.map(orderTotalMoneyStats, expectedSellerCity);

        //then
        assertNotNull(gotHeader);
        assertEquals(expectedId, gotHeader.getId());
        assertEquals(expectedTimestamp, gotHeader.getCreationTimestamp().substring(0, 8));
        assertEquals(expectedTimestamp, gotHeader.getSellTimestamp().substring(0, 8));
        assertEquals(orderTotalMoneyStats.getNumberOfOrderItems(), gotHeader.getNumberOfProducts());
        assertEquals(orderTotalMoneyStats.getTotalWithoutTax(), gotHeader.getTotalPriceWithoutTax());
        assertEquals(orderTotalMoneyStats.getTaxValue(), gotHeader.getTotalTaxValue());
        assertEquals(orderTotalMoneyStats.getTotalWithTax(), gotHeader.getTotalPriceWithTax());
        assertEquals(expectedTimestamp, gotHeader.getPaymentTimestamp().substring(0, 8));
        assertEquals(orderTotalMoneyStats.getTotalWithTax(), gotHeader.getTotalPaidPayment());
        assertEquals(orderTotalMoneyStats.getTotalWithTax(), gotHeader.getTotalPrice());
        assertEquals(expectedSellerCity, gotHeader.getCity());
    }
}