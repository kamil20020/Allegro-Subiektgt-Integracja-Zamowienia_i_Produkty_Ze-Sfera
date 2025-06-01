package pl.kamil_dywan.model.unit;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.allegro.generated.buyer.Buyer;

import static org.junit.jupiter.api.Assertions.*;

class BuyerTest {

    @Test
    public void shouldGetHasCompanyForPerson(){

        //given
        Buyer buyer = Buyer.builder()
            .firstName("Adam")
            .lastName("Nowak")
            .build();

        //when
        boolean result = buyer.hasCompany();

        //then
        assertFalse(result);
    }

    @Test
    public void shouldGetHasCompanyForCompany(){

        //given
        Buyer buyer = Buyer.builder()
            .companyName("Company 123")
            .build();

        //when
        boolean result = buyer.hasCompany();

        //then
        assertTrue(result);
    }

    @Test
    void shouldGetNameForPerson() {

        //given
        String firstName = "Adam";
        String lastName = "Nowak";
        String expectedName = firstName + " " + lastName;

        Buyer buyer = Buyer.builder()
            .firstName(firstName)
            .lastName(lastName)
            .build();

        //when
        String gotName = buyer.getName();

        //then
        assertEquals(expectedName, gotName);
    }

    @Test
    void shouldGetNameForCompany() {

        //given
        String expectedName = "Company 123";

        Buyer buyer = Buyer.builder()
            .companyName(expectedName)
            .build();

        //when
        String gotName = buyer.getName();

        //then
        assertEquals(expectedName, gotName);
    }
}