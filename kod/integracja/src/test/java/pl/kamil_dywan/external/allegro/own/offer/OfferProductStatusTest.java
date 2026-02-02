package pl.kamil_dywan.external.allegro.own.offer;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class OfferProductStatusTest {

    @Test
    void shouldGetToString() {

        //given
        List<String> names = List.of("ACTIVE");

        //when
        //then
        for(OfferProductStatus status : OfferProductStatus.values()){

            String statusName = status.toString();

            assertTrue(names.contains(statusName));
        }
    }
}