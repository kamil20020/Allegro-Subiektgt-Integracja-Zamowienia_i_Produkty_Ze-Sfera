package pl.kamil_dywan.service.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.kamil_dywan.App;
import pl.kamil_dywan.TestUtils;
import pl.kamil_dywan.service.AppProperties;

import java.lang.reflect.Field;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class AppPropertiesTestIT {

    @BeforeAll
    public static void setUp(){

        AppProperties.loadProperties();
    }

    @Test
    void shouldLoadProperties(){

        //given
        String expectedProperty = "allegro.pl.allegrosandbox.pl";

        //when
        Properties gotProperties = TestUtils.getPrivateStaticField(AppProperties.class, "properties", Properties.class);

        String gotProperty = gotProperties.getProperty("allegro.api.host");

        //then
        assertEquals(expectedProperty, gotProperty);
    }

    @Test
    void shouldGetProperty() {

        String expectedPropertyKey = "allegro.api.host";

        String expectedProperty = "allegro.pl.allegrosandbox.pl";

        String gotProperty = AppProperties.getProperty(expectedPropertyKey, String.class);

        assertEquals(expectedProperty, gotProperty);
    }

    @Test
    void shouldGetPropertyStr() {

        String expectedPropertyKey = "allegro.api.host";

        String expectedProperty = "allegro.pl.allegrosandbox.pl";

        String gotProperty = AppProperties.getProperty(expectedPropertyKey);

        assertEquals(expectedProperty, gotProperty);
    }
}