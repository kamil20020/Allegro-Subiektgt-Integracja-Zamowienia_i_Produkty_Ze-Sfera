package pl.kamil_dywan.factory;

import pl.kamil_dywan.external.subiektgt.generated.settlement.Settlement;
import pl.kamil_dywan.external.subiektgt.generated.settlement.SettlementTerms;
import pl.kamil_dywan.external.subiektgt.own.Code;

import java.time.LocalDate;

public interface SettlementFactory {

    static Settlement create(LocalDate dueDate){

        return new Settlement(
            SettlementTerms.builder()
                .value(dueDate)
                .code(Code.Code14I)
                .build()
        );
    }
}
