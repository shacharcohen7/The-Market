package PresentationLayer.WAF;

import DomainLayer.Market.Market;
import DomainLayer.Role.RoleFacade;
import DomainLayer.Store.StoreFacade;
import DomainLayer.User.UserFacade;
import Util.StoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MemberService {



    private final UserFacade userFacade;
    private final Market market;
    private final RoleFacade roleFacade;

    @Autowired
    public MemberService(UserFacade userFacade, Market market, RoleFacade roleFacade) {
        this.userFacade = userFacade;
        this.market = market;
        this.roleFacade= roleFacade;
    }

    public String getMemberNane(String memberID){
        return userFacade.getMemberName(memberID);
    }

        public boolean isMember(String userId) {
            return userFacade.isMember(userId);
        }

    public boolean isStoreOwner(String memberId,String storeId){
        return roleFacade.verifyStoreOwner(storeId, memberId);
    }

    public boolean isStoreManager(String memberId,String storeId){
        return roleFacade.verifyStoreManager(storeId, memberId);
    }

    public boolean hasInventoryPermission(String memberID, String storeId){
        return roleFacade.managerHasInventoryPermissions(memberID, storeId);
    }

    public boolean hasPurchasePermission(String memberID, String storeId){
        return roleFacade.managerHasPurchasePermissions(memberID, storeId);
    }

    public boolean isAdmin(String memberId){
        return roleFacade.verifyMemberIsSystemManager(memberId);
    }


}

