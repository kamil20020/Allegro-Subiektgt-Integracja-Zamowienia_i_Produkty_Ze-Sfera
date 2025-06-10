package pl.kamil_dywan.external.allegro.generated.order;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import pl.kamil_dywan.external.allegro.generated.Cost;
import pl.kamil_dywan.external.allegro.generated.Payment;
import pl.kamil_dywan.external.allegro.generated.buyer.Buyer;
import pl.kamil_dywan.external.allegro.generated.delivery.Delivery;
import pl.kamil_dywan.external.allegro.generated.invoice.Invoice;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;
import pl.kamil_dywan.external.allegro.own.order.*;
import pl.kamil_dywan.mapper.AllegroOrderItemMapper;

import javax.annotation.processing.Generated;
import java.beans.Transient;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "id",
    "messageToSeller",
    "buyer",
    "payment",
    "status",
    "fulfillment",
    "delivery",
    "invoice",
    "orderItems",
    "surcharges",
    "discounts",
    "note",
    "marketplace",
    "summary",
    "updatedAt",
    "revision"
})
@Generated("jsonschema2pojo")
public class Order {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("messageToSeller")
    private String messageToSeller;

    @JsonProperty("buyer")
    private Buyer buyer;

    @JsonProperty("payment")
    private Payment payment;

    @JsonProperty("status")
    private OrderStatus status;

    @JsonProperty("fulfillment")
    private Fulfillment fulfillment;

    @JsonProperty("delivery")
    private Delivery delivery;

    @JsonProperty("invoice")
    private Invoice invoice;

    @JsonProperty("lineItems")
    private List<OrderItem> orderItems = new ArrayList<>();

    @JsonProperty("surcharges")
    private List<Surcharge> surcharges = new ArrayList<>();

    @JsonProperty("discounts")
    private List<Discount> discounts = new ArrayList<>();

    @JsonProperty("note")
    private Note note;

    @JsonProperty("marketplace")
    private Marketplace marketplace;

    @JsonProperty("summary")
    private Summary summary;

    @JsonProperty("updatedAt")
    private OffsetDateTime updatedAt;

    @JsonProperty("revision")
    private String revision;

    @JsonIgnore
    private boolean hasDocument = false;

    @JsonIgnore
    private String externalId;

    public Order(UUID id, String messageToSeller, Buyer buyer, Payment payment, OrderStatus status, Fulfillment fulfillment, Delivery delivery, Invoice invoice, List<OrderItem> orderItems, List<Surcharge> surcharges, List<Discount> discounts, Note note, Marketplace marketplace, Summary summary, OffsetDateTime updatedAt, String revision, boolean hasDocument, String externalId) {

        this.id = id;
        this.messageToSeller = messageToSeller;
        this.buyer = buyer;
        this.payment = payment;
        this.status = status;
        this.fulfillment = fulfillment;
        this.delivery = delivery;
        this.invoice = invoice;
        this.orderItems = orderItems;
        this.surcharges = surcharges;
        this.discounts = discounts;
        this.note = note;
        this.marketplace = marketplace;
        this.summary = summary;
        this.updatedAt = updatedAt;
        this.revision = revision;
        this.hasDocument = hasDocument;
        this.externalId = externalId;

        addDeliveryToOrderItems();
    }

    @JsonIgnore
    public void addDeliveryToOrderItems(){

        if(!hasDelivery()){
            return;
        }

        if(!orderItems.isEmpty()){

            int lastItemIndex = orderItems.size() - 1;
            OrderItem lastItem = orderItems.get(lastItemIndex);

            String lastOfferName = lastItem.getOffer().getName();

            if(lastOfferName != null && lastOfferName.startsWith("DOSTAWA")){

                orderItems.remove(lastItemIndex);
            }
        }

        OrderItem allegroDeliveryItem = AllegroOrderItemMapper.mapDeliveryToLineItem(delivery);

        this.orderItems.add(allegroDeliveryItem);
    }

    @JsonIgnore
    public boolean hasInvoice(){

        return invoice.isRequired();
    }

    @JsonIgnore
    public boolean isBuyerCompany(){

        if(hasInvoice()){

            return invoice.hasCompany();
        }

        return buyer.hasCompany();
    }

    @JsonIgnore
    public String getClientName(){

        if(hasInvoice()){

            return invoice.getClientName();
        }

        return buyer.getName();
    }

    @JsonIgnore
    public boolean hasDelivery(){

        if(delivery == null){
            return false;
        }

        Cost deliveryCost = delivery.getCost();

        if(deliveryCost == null || deliveryCost.getAmount() == null){
            return false;
        }

        return deliveryCost.getAmount().intValue() > 0;
    }

    @JsonIgnore
    public void setExternalId(String externalId){

        this.externalId = externalId;

        if(externalId == null){

            return;
        }

        this.externalId = externalId.replaceAll("\"", "");
    }

    @JsonIgnore
    public boolean isCancelled(){

        if(fulfillment == null){
            return false;
        }

        FulFillmentStatus fulFillmentStatus = fulfillment.getStatus();

        return fulFillmentStatus == FulFillmentStatus.CANCELLED;
    }

}
