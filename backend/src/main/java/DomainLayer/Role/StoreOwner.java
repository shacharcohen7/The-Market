package DomainLayer.Role;
import DomainLayer.Role.StoreOwnerId;

import jakarta.persistence.*;

@Entity
@Table(name = "storeOwner", schema = "themarketdb")
public class StoreOwner implements Role {

    @EmbeddedId
    private StoreOwnerId id;

    @Column(name = "founder")
    private boolean founder;

    @Column(name = "nominatorId")
    private String nominatorId;

    @Column(name = "inProposal")
    private boolean inProposal;

    public StoreOwner(String member_ID, String store_ID, boolean founder, String nominatorId, boolean inProposal) {
        this.id = new StoreOwnerId(member_ID, store_ID);
        this.founder = founder;
        this.nominatorId = nominatorId;
        this.inProposal = inProposal;
    }

    public StoreOwner() {

    }

    // Getters and setters
    @Override
    public String getStore_ID()
    {
        return this.id.getStore_ID();
    }

    public String getNominatorId() {
        return nominatorId;
    }

    public boolean getFounder() {
        return founder;
    }

    @Override
    public String getMember_ID() {
        return this.id.getMember_ID();
    }

    public boolean verifyStoreOwnerIsFounder()
    {
        return founder;
    }

    public StoreOwnerId getId() {
        return id;
    }

    public void setId(StoreOwnerId id) {
        this.id = id;
    }

    public boolean isInProposal() {return inProposal;}

    public void setInProposal(boolean inProposal) {this.inProposal = inProposal;}

}
