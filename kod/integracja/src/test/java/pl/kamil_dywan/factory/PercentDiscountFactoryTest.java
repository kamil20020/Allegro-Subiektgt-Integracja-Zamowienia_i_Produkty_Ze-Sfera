package pl.kamil_dywan.factory;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.subiektgt.generated.Type;
import pl.kamil_dywan.external.subiektgt.generated.invoice_line.PercentDiscount;
import pl.kamil_dywan.external.subiektgt.own.Code;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PercentDiscountFactoryTest {

    @Test
    void shouldCreate() {

        //given

        //when
        PercentDiscount gotPercentDiscount = PercentDiscountFactory.create();

        //then
        assertNotNull(gotPercentDiscount);
        assertNotNull(gotPercentDiscount.getType());

        Type gotType = gotPercentDiscount.getType();

        assertEquals("", gotType.getValue());
        assertEquals(Code.LID, gotType.getCode());

        assertEquals(BigDecimal.ZERO, gotPercentDiscount.getPercentage());
    }
}