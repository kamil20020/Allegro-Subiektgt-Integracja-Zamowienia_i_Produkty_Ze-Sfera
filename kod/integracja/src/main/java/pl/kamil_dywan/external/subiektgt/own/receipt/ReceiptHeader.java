package pl.kamil_dywan.external.subiektgt.own.receipt;

import lombok.*;
import pl.kamil_dywan.external.subiektgt.own.BooleanInteger;
import pl.kamil_dywan.external.subiektgt.own.Code;
import pl.kamil_dywan.external.subiektgt.own.TypeOfTransaction;
import pl.kamil_dywan.external.subiektgt.own.document.*;
import pl.kamil_dywan.external.subiektgt.own.PriceCategory;
import pl.kamil_dywan.file.EppSerializable;

import java.math.BigDecimal;

@Getter
@ToString
public class ReceiptHeader extends EppSerializable {

    private DocType docType = DocType.RECEIPT;
    private DocStatus docStatus = DocStatus.MADE;
    private DocRegistrationStatus docRegistrationStatus = DocRegistrationStatus.NOT_REGISTERED;
    private Integer receiptIndex = 1;
    private String id;
    private String docCategory = DocCategory.RETAIL.toString();
    private String docSubCategory = DocCategory.RETAIL.getLongValue();
    private String city;
    private String creationTimestamp;
    private String sellTimestamp;
    private Integer numberOfProducts;
    private BooleanInteger docCreatedByNetto = BooleanInteger.NO;
    private PriceCategory priceCategory = PriceCategory.RETAIL;
    private BigDecimal totalPriceWithoutTax;
    private BigDecimal totalTaxValue;
    private BigDecimal totalPriceWithTax;
    private BigDecimal cost = BigDecimal.ZERO;
    private BigDecimal discountPercentage = BigDecimal.ZERO;
    private String paymentTimestamp;
    private BigDecimal totalPaidPayment;
    private BigDecimal totalPrice;
    private DocMoneyRoundMethod totalPriceRoundingMethod = DocMoneyRoundMethod.TO_SECOND_DECIMAL_PLACE;
    private DocMoneyRoundMethod totalTaxValueRoundingMethod = DocMoneyRoundMethod.TO_SECOND_DECIMAL_PLACE;
    private BooleanInteger docShouldCountAutomatically = BooleanInteger.YES;
    private DocSpecialStatus docSpecialStatus = DocSpecialStatus.NOT_USED;
    private BigDecimal costOfGivenPackages = BigDecimal.ZERO;
    private BigDecimal costOfReturnedPackages = BigDecimal.ZERO;
    private Code currencyCode = Code.PLN;
    private BigDecimal currencyRate = BigDecimal.ONE;
    private BooleanInteger docWasImported = BooleanInteger.NO;
    private BooleanInteger docIsExport = BooleanInteger.NO;
    private TypeOfTransaction typeOfTransaction = TypeOfTransaction.COUNTRY;
    private BigDecimal debitCardPayment = BigDecimal.ZERO;
    private BigDecimal creditCardPayment = BigDecimal.ZERO;
    private BooleanInteger isClientInUE = BooleanInteger.NO;

    public ReceiptHeader(String... args) {

        super(args);

        this.docType = DocType.valueOf(args[0]);
        this.docStatus = DocStatus.valueOf(args[1]);
//        this.docRegistrationStatus = docRegistrationStatus;
//        this.id = id;
//        this.docCategory = docCategory;
//        this.docSubCategory = docSubCategory;
//        this.creationTimestamp = creationTimestamp;
//        this.sellTimestamp = sellTimestamp;
//        this.numberOfProducts = numberOfProducts;
//        this.docCreatedByNetto = docCreatedByNetto;
//        this.priceCategory = priceCategory;
//        this.totalPriceWithoutTax = totalPriceWithoutTax;
//        this.totalTaxValue = totalTaxValue;
//        this.totalPriceWithTax = totalPriceWithTax;
//        this.cost = cost;
//        this.discountPercentage = discountPercentage;
//        this.paymentTimestamp = paymentTimestamp;
//        this.totalPaidPayment = totalPaidPayment;
//        this.totalPrice = totalPrice;
//        this.totalPriceRoundingMethod = totalPriceRoundingMethod;
//        this.totalTaxValueRoundingMethod = totalTaxValueRoundingMethod;
//        this.docShouldCountAutomatically = docShouldCountAutomatically;
//        this.specialStatuses = specialStatuses;
//        this.docSpecialStatus = docSpecialStatus;
//        this.costOfGivenPackages = costOfGivenPackages;
//        this.costOfReturnedPackages = costOfReturnedPackages;
//        this.currencyCode = currencyCode;
//        this.currencyRate = currencyRate;
//        this.docWasImported = docWasImported;
//        this.docIsExport = docIsExport;
//        this.typeOfTransaction = typeOfTransaction;
//        this.debitCardPayment = debitCardPayment;
//        this.creditCardPayment = creditCardPayment;
//        this.isClientInUE = isClientInUE;
    }

    public ReceiptHeader(String id, String creationTimestamp, String sellTimestamp, Integer numberOfProducts, BigDecimal totalPriceWithoutTax, BigDecimal totalTaxValue, BigDecimal totalPriceWithTax, String paymentTimestamp, BigDecimal totalPaidPayment, BigDecimal totalPrice, String city){

        super(null);

        this.id = id;
        this.creationTimestamp = creationTimestamp;
        this.sellTimestamp = sellTimestamp;
        this.numberOfProducts = numberOfProducts;
        this.totalPriceWithoutTax = totalPriceWithoutTax;
        this.totalTaxValue = totalTaxValue;
        this.totalPriceWithTax = totalPriceWithTax;
        this.paymentTimestamp = paymentTimestamp;
        this.totalPaidPayment = totalPaidPayment;
        this.totalPrice = totalPrice;
        this.city = city;
    }

    public ReceiptHeader(String id, DocType docType, DocStatus docStatus, DocRegistrationStatus docRegistrationStatus, Integer receiptIndex, String docCategory, String docSubCategory, String city, String creationTimestamp, String sellTimestamp, Integer numberOfProducts, BooleanInteger docCreatedByNetto, PriceCategory priceCategory, BigDecimal totalPriceWithoutTax, BigDecimal totalTaxValue, BigDecimal totalPriceWithTax, BigDecimal cost, BigDecimal discountPercentage, String paymentTimestamp, BigDecimal totalPaidPayment, BigDecimal totalPrice, DocMoneyRoundMethod totalPriceRoundingMethod, DocMoneyRoundMethod totalTaxValueRoundingMethod, BooleanInteger docShouldCountAutomatically, DocSpecialStatus docSpecialStatus, BigDecimal costOfGivenPackages, BigDecimal costOfReturnedPackages, Code currencyCode, BigDecimal currencyRate, BooleanInteger docWasImported, BooleanInteger docIsExport, TypeOfTransaction typeOfTransaction, BigDecimal debitCardPayment, BigDecimal creditCardPayment, BooleanInteger isClientInUE) {

        super(null);

        this.id = id;
        this.docType = docType;
        this.docStatus = docStatus;
        this.docRegistrationStatus = docRegistrationStatus;
        this.receiptIndex = receiptIndex;
        this.docCategory = docCategory;
        this.docSubCategory = docSubCategory;
        this.city = city;
        this.creationTimestamp = creationTimestamp;
        this.sellTimestamp = sellTimestamp;
        this.numberOfProducts = numberOfProducts;
        this.docCreatedByNetto = docCreatedByNetto;
        this.priceCategory = priceCategory;
        this.totalPriceWithoutTax = totalPriceWithoutTax;
        this.totalTaxValue = totalTaxValue;
        this.totalPriceWithTax = totalPriceWithTax;
        this.cost = cost;
        this.discountPercentage = discountPercentage;
        this.paymentTimestamp = paymentTimestamp;
        this.totalPaidPayment = totalPaidPayment;
        this.totalPrice = totalPrice;
        this.totalPriceRoundingMethod = totalPriceRoundingMethod;
        this.totalTaxValueRoundingMethod = totalTaxValueRoundingMethod;
        this.docShouldCountAutomatically = docShouldCountAutomatically;
        this.docSpecialStatus = docSpecialStatus;
        this.costOfGivenPackages = costOfGivenPackages;
        this.costOfReturnedPackages = costOfReturnedPackages;
        this.currencyCode = currencyCode;
        this.currencyRate = currencyRate;
        this.docWasImported = docWasImported;
        this.docIsExport = docIsExport;
        this.typeOfTransaction = typeOfTransaction;
        this.debitCardPayment = debitCardPayment;
        this.creditCardPayment = creditCardPayment;
        this.isClientInUE = isClientInUE;
    }

    public ReceiptHeader(){

        super(null);
    }
}