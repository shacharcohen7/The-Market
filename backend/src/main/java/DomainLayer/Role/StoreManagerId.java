package DomainLayer.Role;

import jakarta.persistence.Column;

import jakarta.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;

@Embeddable
public class StoreManagerId implements Serializable {
    @Column(name = "member_ID")
    private String member_ID;

    @Column(name = "store_ID")
    private String store_ID;


    public StoreManagerId() {
        // default constructor needed for JPA
    }

    public StoreManagerId(String member_ID, String store_ID) {
        this.member_ID = member_ID;
        this.store_ID = store_ID;
    }

    public String getMember_ID() {
        return member_ID;
    }

    public String getStore_ID() {
        return store_ID;
    }

    public void setStore_ID(String store_ID) {
        this.store_ID = store_ID;
    }
}