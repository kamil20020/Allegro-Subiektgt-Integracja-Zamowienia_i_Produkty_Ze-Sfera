package pl.kamil_dywan.factory;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.subiektgt.generated.settlement.Settlement;
import pl.kamil_dywan.external.subiektgt.generated.settlement.SettlementTerms;
import pl.kamil_dywan.external.subiektgt.own.Code;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SettlementFactoryTest {

    @Test
    void shouldCreate() {

        //given
        LocalDate dueDate = LocalDate.now();

        //when
        Settlement gotSettlement = SettlementFactory.create(dueDate);

        //then
        assertNotNull(gotSettlement);
        assertNotNull(gotSettlement.getSettlementTerms());

        SettlementTerms gotSettlementTerms = gotSettlement.getSettlementTerms();

        assertEquals(dueDate, gotSettlementTerms.getValue());
        assertEquals(Code.Code14I, gotSettlementTerms.getCode());
    }
}