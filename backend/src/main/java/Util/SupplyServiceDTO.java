package Util;
import java.util.Set;

public class SupplyServiceDTO {

    private String licensedDealerNumber;
    private String supplyServiceName;
    private Set<String> countries;
    private Set<String> cities ;

    public SupplyServiceDTO(){

    }


    public SupplyServiceDTO(String licensedDealerNumber, String supplyServiceName,Set<String> countries, Set<String> cities ){
        this.cities = cities;
        this.countries = countries;
        this.supplyServiceName = supplyServiceName;
        this.licensedDealerNumber = licensedDealerNumber;
    }

    public Set<String> getCities() {
        return cities;
    }

    public Set<String> getCountries() {
        return countries;
    }

    public String getLicensedDealerNumber() {
        return licensedDealerNumber;
    }

    public String getSupplyServiceName() {
        return supplyServiceName;
    }
}
