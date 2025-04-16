package PresentationLayer.WAF;

import DomainLayer.Market.Market;
import DomainLayer.Store.StoreFacade;
import DomainLayer.User.UserFacade;
import Util.StoreDTO;
import Util.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class StoreService {


    private final StoreFacade storeFacade;
    private final Market market;

    @Autowired
    public StoreService(StoreFacade storeFacade, Market market) {
        this.storeFacade = storeFacade;
        this.market = market;
    }

    @Bean
    public RestTemplate restTemplate() {
            return new RestTemplate();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
    public List<StoreDTO> getAllStores(){
        return storeFacade.getAllDTOs();
    }

    public StoreDTO getStore(String storeId) throws Exception {
        return storeFacade.getStoreDTOById(storeId);
    }


    public boolean isStoreOpen(String storeID) throws Exception {
        return storeFacade.isStoreOpen(storeID);
    }

}


