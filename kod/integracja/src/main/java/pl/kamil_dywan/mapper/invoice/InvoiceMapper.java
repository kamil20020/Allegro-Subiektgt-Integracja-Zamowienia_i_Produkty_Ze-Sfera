package pl.kamil_dywan.mapper.invoice;

import pl.kamil_dywan.external.allegro.generated.Payment;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;
import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.external.allegro.own.order.OrderMoneyStats;
import pl.kamil_dywan.external.allegro.own.order.OrderTaxSummary;
import pl.kamil_dywan.external.allegro.own.order.OrderTotalMoneyStats;
import pl.kamil_dywan.external.subiektgt.generated.settlement.Settlement;
import pl.kamil_dywan.factory.InvoiceHeadFactory;
import pl.kamil_dywan.external.subiektgt.generated.Invoice;
import pl.kamil_dywan.external.subiektgt.generated.InvoiceTotal;
import pl.kamil_dywan.external.subiektgt.generated.TaxSubTotal;
import pl.kamil_dywan.external.subiektgt.generated.buyer.Buyer;
import pl.kamil_dywan.external.subiektgt.generated.invoice_line.InvoiceLine;
import pl.kamil_dywan.external.subiektgt.own.Code;
import pl.kamil_dywan.external.allegro.own.order.OrderItemMoneyStats;
import pl.kamil_dywan.factory.InvoiceReferencesFactory;
import pl.kamil_dywan.factory.SettlementFactory;
import pl.kamil_dywan.mapper.TaxSubTotalMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface InvoiceMapper {

    static Invoice map(Order allegroOrder, String sellerCity){

        pl.kamil_dywan.external.allegro.generated.invoice.Invoice allegroInvoice = allegroOrder.getInvoice();

        Buyer buyer = InvoiceBuyerMapper.map(allegroInvoice);
        List<OrderItem> allegroOrderItems = allegroOrder.getOrderItems();

        OrderMoneyStats allegroOrderMoneyStats = OrderMoneyStats.getSummary(allegroOrder);
        List<OrderItemMoneyStats> orderItemsMoneyStats = allegroOrderMoneyStats.orderItemsMoneyStats();
        List<OrderTaxSummary> orderTaxesSummaries = allegroOrderMoneyStats.orderTaxesSummaries();
        OrderTotalMoneyStats orderTotalMoneyStats = allegroOrderMoneyStats.orderTotalMoneyStats();

        List<InvoiceLine> subiektInvoiceLines = mapInvoiceLines(allegroOrderItems, orderItemsMoneyStats);
        handleDelivery(allegroOrder, subiektInvoiceLines);

        List<TaxSubTotal> taxSubTotals = orderTaxesSummaries.stream()
            .map(TaxSubTotalMapper::map)
            .collect(Collectors.toList());

        LocalDate actualDate = LocalDate.now();

        Settlement settlement = SettlementFactory.create(actualDate);

        InvoiceTotal invoiceTotal = InvoiceTotalMapper.map(orderTotalMoneyStats);

        return Invoice.builder()
            .invoiceHead(InvoiceHeadFactory.create(Code.PLN))
            .invoiceDate(actualDate)
            .invoiceReferences(InvoiceReferencesFactory.create())
            .cityOfIssue(sellerCity)
            .taxPointDate(actualDate)
            .buyer(buyer)
            .invoiceLines(subiektInvoiceLines)
            .narrative("")
            .specialInstructions("dokument liczony wg cen brutto")
            .settlement(settlement)
            .taxSubTotals(taxSubTotals)
            .invoiceTotal(invoiceTotal)
            .build();
    }

    private static List<InvoiceLine> mapInvoiceLines(List<OrderItem> allegroOrderItems, List<OrderItemMoneyStats> orderItemsMoneyStats){

        List<InvoiceLine> subiektInvoiceLines = new ArrayList<>();

        for(int i=0; i < allegroOrderItems.size(); i++){

            Integer newOrderItemNumber = i + 1;
            OrderItem orderItem = allegroOrderItems.get(i);
            OrderItemMoneyStats orderItemMoneyStats = orderItemsMoneyStats.get(i);

            InvoiceLine subiektInvoiceLine = InvoiceLineMapper.map(newOrderItemNumber, orderItem, orderItemMoneyStats);

            subiektInvoiceLines.add(subiektInvoiceLine);
        }

        return subiektInvoiceLines;
    }

    private static void handleDelivery(Order allegroOrder, List<InvoiceLine> subiektInvoiceLines){

        if(!allegroOrder.hasDelivery()){
            return;
        }

        int deliveryIndex = subiektInvoiceLines.size() - 1;

        subiektInvoiceLines.get(deliveryIndex).getProduct().setSuppliersProductCode("DOSTAWA123");
    }
}
