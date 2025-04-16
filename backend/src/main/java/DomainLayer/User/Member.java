package DomainLayer.User;
import DomainLayer.Store.Store;
import DomainLayer.Store.StoreFacade;
import DomainLayer.Role.RoleFacade;
import Util.ExceptionsEnum;
import Util.UserDTO;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CheckedOutputStream;

@Entity
@Table(name = "member", schema = "themarketdb")
public class Member extends State{
    @Column(name = "user_id")
    private String userId;

    @Id
    @Column(name = "member_id")
    private String member_ID;

    @Column(name = "name")
    private String name;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "birthday")
    private String birthday;
    @Column(name = "country")
    private String country;
    @Column(name = "city")
    private String city;
    @Column(name = "address")
    private String address;
    @Column(name = "product_id_counter")
    private int productIdCounter;
//    @ElementCollection
//    private List<String> acquisitionIds;

    //private boolean isLogin;

    /*Member(String member_ID, UserDTO user, String password)
    {
        this.member_ID = member_ID;
        this.username = user.getUserName();
        this.password = password;
        this.birthday = user.getBirthday();
        this.country = user.getCountry();
        this.city =  user.getCity();
        this.address = user.getAddress();
        this.name = user.getName();
        this.productIdCounter = 0;
        this.receiptIdsAndStoreId = new HashMap<>();
    }*/

    public Member(String userId , String member_ID, String username,String address, String name, String password , String birthday, String country, String city )
    {
        this.userId = userId;
        this.member_ID = member_ID;
        this.username =username;
        this.password = password;
        this.birthday = birthday;
        this.country = country;
        this.city =  city;
        this.address = address;
        this.name = name;
        this.productIdCounter = 0;
//        this.acquisitionIds = new ArrayList<>();
    }

    public Member() {

    }

    public void Logout()
    {
        // do nothing
    }

    public void exitMarketSystem(){
    }

    @Override
    public void Login() throws Exception {
        throw new Exception(ExceptionsEnum.userAlreadyLoggedIn.toString());
    }

    public void validatePassword(String password){
        if (!password.equals(this.password)){
            throw new IllegalArgumentException(ExceptionsEnum.usernameOrPasswordIncorrect.toString());
        }
    }

    @Override
    public String getUsername(){
        return username;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPassword() {
        return password;
    }

    //public Map<String, String> getReceiptIdsAndStoreId(){return receiptIdsAndStoreId;}

    @Override
    public String getMemberID()
    {
        return this.member_ID;
    }

    @Override
    public boolean isMember() {
        return true;
    }


//    public void addAcquisition(String acquisitionId){
//        acquisitionIds.add(acquisitionId);
//    }

//    @Override
//    public List<String> getAcquisitionIds() {
//        return acquisitionIds;
//    }

    public String getMember_ID() {
        return member_ID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

//    @Override
//    public int removeAcquisition(String acquisitionId) {
//        if (acquisitionIds.contains(acquisitionId)) {
//            acquisitionIds.remove(acquisitionId);
//            return 1;
//        }
//        return -1;
//    }
}
