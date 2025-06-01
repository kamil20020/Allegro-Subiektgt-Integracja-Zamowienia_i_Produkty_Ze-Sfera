package pl.kamil_dywan.mapper.unit.invoice;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.allegro.generated.order_item.ExternalId;
import pl.kamil_dywan.external.allegro.generated.order_item.Offer;
import pl.kamil_dywan.external.subiektgt.generated.invoice_line.Product;
import pl.kamil_dywan.mapper.invoice.InvoiceProductMapper;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceProductMapperTest {

    @Test
    void shouldMapWithoutExternalId() {

        //given
        Offer allegroOffer = Offer.builder()
            .id("123")
            .name("Offer name")
            .build();

        //when
        Product product = InvoiceProductMapper.map(allegroOffer);

        //then
        assertNotNull(product);
        assertEquals(allegroOffer.getId(), product.getSuppliersProductCode());
        assertEquals(allegroOffer.getName(), product.getDescription());
    }

    @Test
    void shouldMapWithoutPartiallyExternalId() {

        //given
        Offer allegroOffer = Offer.builder()
            .id("123")
            .name("Offer name")
            .external(new ExternalId())
            .build();

        //when
        Product product = InvoiceProductMapper.map(allegroOffer);

        //then
        assertNotNull(product);
        assertEquals(allegroOffer.getId(), product.getSuppliersProductCode());
        assertEquals(allegroOffer.getName(), product.getDescription());
    }

    @Test
    void shouldMapWithExternalId() {

        //given
        String expectedId = "Id 123";

        ExternalId externalId = new ExternalId(expectedId);

        Offer allegroOffer = Offer.builder()
            .id("123")
            .name("Offer name")
            .external(externalId)
            .build();

        //when
        Product product = InvoiceProductMapper.map(allegroOffer);

        //then
        assertNotNull(product);
        assertEquals(expectedId, product.getSuppliersProductCode());
        assertEquals(allegroOffer.getName(), product.getDescription());
    }

}