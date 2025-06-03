package pl.kamil_dywan.mapper.sfera;

import pl.kamil_dywan.api.sfera.request.CreateOrderRequest;
import pl.kamil_dywan.external.allegro.generated.invoice.Invoice;
import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.external.allegro.generated.order.Summary;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;
import pl.kamil_dywan.external.sfera.generated.Customer;
import pl.kamil_dywan.external.sfera.generated.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public interface SferaOrderMapper {

    public static CreateOrderRequest map(Order order){

        Summary orderSummary = order.getSummary();
        BigDecimal totalPrice = orderSummary.getTotalToPay().getAmount();

        Customer customer = SferaCustomerMapper.map(order);

        List<OrderItem> orderItems = order.getOrderItems();

        List<Product> products = orderItems.stream()
            .map(orderItem -> SferaProductMapper.map(orderItem))
            .collect(Collectors.toList());

        handleDelivery(order, products);

        String reference;

        if(order.hasInvoice()){

            reference = "Faktura VAT";
        }
        else {

            reference = "Paragon";
        }

        return new CreateOrderRequest(
            reference,
            order.getId().toString(),
            totalPrice,
            customer,
            products
        );
    }

    private static void handleDelivery(Order allegroOrder, List<Product> products){

        if(!allegroOrder.hasDelivery()){
            return;
        }

        int deliveryIndex = products.size() - 1;

        products.get(deliveryIndex).setCode("DOSTAWA123");
    }
}
