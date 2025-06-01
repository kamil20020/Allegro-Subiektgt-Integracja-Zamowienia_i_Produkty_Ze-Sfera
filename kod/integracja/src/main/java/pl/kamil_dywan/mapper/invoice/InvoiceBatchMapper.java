package pl.kamil_dywan.mapper.invoice;

import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.factory.InvoiceBatchTrailerFactory;
import pl.kamil_dywan.external.subiektgt.generated.InvoiceBatch;
import pl.kamil_dywan.external.subiektgt.generated.InvoiceBatchTrailer;
import pl.kamil_dywan.external.subiektgt.generated.Invoice;
import pl.kamil_dywan.external.subiektgt.own.Code;
import pl.kamil_dywan.external.subiektgt.own.document.DocType;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public interface InvoiceBatchMapper {

    static InvoiceBatch map(String supplierName, String supplierCity, List<Order> allegroOrders){

        List<Invoice> invoices = allegroOrders.stream()
            .map(allegroOrder -> InvoiceMapper.map(allegroOrder, supplierCity))
            .collect(Collectors.toList());

        InvoiceBatchTrailer batchTrailer = InvoiceBatchTrailerFactory.create(Code.PLN);

        return InvoiceBatch.builder()
            .date(LocalDate.now())
            .number(1)
            .supplierName(supplierName)
            .docType(DocType.INVOICE)
            .invoices(invoices)
            .batchTrailer(batchTrailer)
            .build();
    }
}
