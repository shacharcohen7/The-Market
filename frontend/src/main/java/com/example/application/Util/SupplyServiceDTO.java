package com.example.application.Util;

import java.util.HashSet;

public class SupplyServiceDTO {
    private String licensedDealerNumber;
    private String supplyServiceName;
    private HashSet<String> countries;
    private HashSet<String> cities ;

    public SupplyServiceDTO(){

    }


    public SupplyServiceDTO(String licensedDealerNumber, String supplyServiceName,HashSet<String> countries, HashSet<String> cities ){
        this.cities = cities;
        this.countries = countries;
        this.supplyServiceName = supplyServiceName;
        this.licensedDealerNumber = licensedDealerNumber;
    }

    public HashSet<String> getCities() {
        return cities;
    }

    public HashSet<String> getCountries() {
        return countries;
    }

    public String getLicensedDealerNumber() {
        return licensedDealerNumber;
    }

    public String getSupplyServiceName() {
        return supplyServiceName;
    }
}
