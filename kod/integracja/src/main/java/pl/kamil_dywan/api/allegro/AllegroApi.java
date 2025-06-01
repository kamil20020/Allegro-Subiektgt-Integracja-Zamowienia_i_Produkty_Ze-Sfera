package pl.kamil_dywan.api.allegro;

import pl.kamil_dywan.api.Api;

public abstract class AllegroApi extends Api {

    private static final String HOST_KEY = "allegro.api.host";

    public AllegroApi(String subDomain, String laterPrefix) {

        super(subDomain, laterPrefix, HOST_KEY);
    }

    public AllegroApi(String laterPrefix) {

        this(null, laterPrefix);
    }
}
