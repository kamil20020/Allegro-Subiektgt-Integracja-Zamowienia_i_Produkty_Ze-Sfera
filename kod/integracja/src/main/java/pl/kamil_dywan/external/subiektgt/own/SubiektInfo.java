package pl.kamil_dywan.external.subiektgt.own;

import java.math.BigDecimal;

public record SubiektInfo(

    BigDecimal version,
    Integer schema,
    Integer encoding,
    String programName,
    String shortProgramName,
    String instanceName,
    String fullName,
    String city,
    String postCode,
    String street,
    String nip,
    Code magazineSymbol,
    String magazineName,
    String magazineFullName,
    boolean dataFormPeriod,
    String communicationCreator,
    String dateOfCommunication,
    String country,
    Code countryPrefix,
    String ueNip,
    boolean isCreatorInUe
){}
