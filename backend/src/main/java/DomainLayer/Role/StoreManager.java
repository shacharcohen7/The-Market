package DomainLayer.Role;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "storeManager", schema = "themarketdb")
public class StoreManager implements Role {

    @EmbeddedId
    private StoreManagerId id;

    @Transient
    private List<Integer> authorizations;

    @Column(name = "inventoryPermissions")
    private boolean inventoryPermissions;

    @Column(name = "purchasePermissions")
    private boolean purchasePermissions;

    @Column(name = "nominatorMemberId")
    private String nominatorMemberId;

    @Column(name = "inProposal")
    private boolean inProposal;


    public StoreManager(String member_ID, String store_ID, boolean inventoryPermissions, boolean purchasePermissions,
                 String nominatorMemberId, boolean inProposal)
    {
        this.id = new StoreManagerId(member_ID, store_ID);
        this.inventoryPermissions = inventoryPermissions;
        this.purchasePermissions = purchasePermissions;
        this.nominatorMemberId = nominatorMemberId;
        this.inProposal = inProposal;
    }

    public StoreManager() {

    }

    public void setPermissions(boolean inventoryPermissions, boolean purchasePermissions){
        this.inventoryPermissions = inventoryPermissions;
        this.purchasePermissions = purchasePermissions;
    }

    public String getNominatorMemberId()
    {
        return this.nominatorMemberId;
    }

    public String getStore_ID()
    {
        return this.id.getStore_ID();
    }

    public String getMember_ID()
    {
        return this.id.getMember_ID();
    }

    public List<Integer> getAuthorizations(){
        return this.authorizations;
    }

    public boolean hasInventoryPermissions(){
        return this.inventoryPermissions;
    }

    public boolean hasPurchasePermissions(){
        return this.purchasePermissions;
    }

    public boolean isInProposal() {return inProposal;}

    public void setInProposal(boolean inProposal) {this.inProposal = inProposal;}

    public StoreManagerId getId() {
        return id;
    }
}