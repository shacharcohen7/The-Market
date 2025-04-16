package DomainLayer.Market;

import DomainLayer.Repositories.InitializedDBRepository;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;

@Entity
@Table(name = "initialized_status")
public class InitializedStatus {

    @Id
    private String name;

    @Column(name = "is_initialized")
    private boolean initialized;

    public InitializedStatus() {
    }

    public InitializedStatus(boolean initialized) {
        this.name = "market";
        this.initialized = initialized;
    }

    public String getName() {
        return name;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
}
