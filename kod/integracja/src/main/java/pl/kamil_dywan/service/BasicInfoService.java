package pl.kamil_dywan.service;

import pl.kamil_dywan.external.subiektgt.own.BasicInfo;

import java.util.Optional;

public class BasicInfoService {

    private static BasicInfo basicInfo = new BasicInfo();

    private static final String BASIC_INFO_PREFIX = "basic-info";

    public static void init(){

        if(!SecureStorage.doesExist(BASIC_INFO_PREFIX)){
            return;
        }

        String location = SecureStorage.getCredentialsPassword(BASIC_INFO_PREFIX);

        basicInfo.setLocation(location);
    }

    public Optional<String> getLocation(){

        return Optional.ofNullable(basicInfo.getLocation());
    }

    public void setLocation(String newLocation){

        basicInfo.setLocation(newLocation);
        SecureStorage.saveCredentials(BASIC_INFO_PREFIX, newLocation);
    }

}
