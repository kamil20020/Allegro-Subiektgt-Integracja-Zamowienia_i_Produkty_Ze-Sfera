package pl.kamil_dywan.api.allegro;

import pl.kamil_dywan.api.Api;
import pl.kamil_dywan.service.AppProperties;

public abstract class AllegroApi extends Api {

    protected static final String HOST_KEY = "allegro.api.host";
    protected static final String SALES_CENTER_HOST_KEY = "allegro.sales-center.host";

    public AllegroApi(String subDomain, String laterPrefix) {

        super(subDomain, laterPrefix, HOST_KEY);
    }

    public AllegroApi(String laterPrefix) {

        this(null, laterPrefix);
    }

    protected static String getSalesCenterUrl(){

        return "https://" + AppProperties.getProperty(SALES_CENTER_HOST_KEY);
    }
}
