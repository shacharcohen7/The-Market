package DomainLayer.User;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class Guest extends State{
    public Guest(){
        super();
    }

    @Override
    public String getMemberID()
    {
        return null;
    }

    @Override
    public void Logout() {
        throw new IllegalArgumentException("only member can log out");
    }

    @Override
    public void exitMarketSystem() {
    }

    @Override
    public void Login() {
        //do nothing
        return;
    }

    @Override
    public boolean isMember() {
        return false;
    }


    @Override
    public String getUsername() {
        return null;
    }


//    @Override
//    public void addAcquisition(String acquisitionId) {
//        return;
//    }
//
//    @Override
//    public List<String> getAcquisitionIds() {
//        return null;
//    }
//
//    @Override
//    public int removeAcquisition(String acquisitionId)  {
//        return -1;
//    }

}
