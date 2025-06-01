package pl.kamil_dywan.mapper.receipt;

import pl.kamil_dywan.external.allegro.own.order.OrderTotalMoneyStats;
import pl.kamil_dywan.external.subiektgt.own.receipt.ReceiptHeader;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public interface ReceiptHeaderMapper {

    public static ReceiptHeader map(OrderTotalMoneyStats orderTotalMoneyStats, String city){

        OffsetDateTime actualDate = OffsetDateTime.now();

        int actualYear = actualDate.getYear();
        String id = "1/" + actualYear;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd000000");

        String timestamp = actualDate.format(formatter);

        return new ReceiptHeader(
            id,
            timestamp,
            timestamp,
            orderTotalMoneyStats.getNumberOfOrderItems(),
            orderTotalMoneyStats.getTotalWithoutTax(),
            orderTotalMoneyStats.getTaxValue(),
            orderTotalMoneyStats.getTotalWithTax(),
            timestamp,
            orderTotalMoneyStats.getTotalWithTax(),
            orderTotalMoneyStats.getTotalWithTax(),
            city
        );
    }
}
