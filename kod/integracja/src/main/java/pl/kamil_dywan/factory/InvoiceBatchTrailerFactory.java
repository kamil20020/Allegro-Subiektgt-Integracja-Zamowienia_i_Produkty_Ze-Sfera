package pl.kamil_dywan.factory;

import pl.kamil_dywan.external.subiektgt.generated.InvoiceBatchTrailer;
import pl.kamil_dywan.external.subiektgt.generated.Currency;
import pl.kamil_dywan.external.subiektgt.generated.CurrencyHolder;
import pl.kamil_dywan.external.subiektgt.own.Code;

public interface InvoiceBatchTrailerFactory {

    static InvoiceBatchTrailer create(Code currencyCode){

        return InvoiceBatchTrailer.builder()
            .itemCurrency(new CurrencyHolder(new Currency(currencyCode)))
            .build();
    }
}
