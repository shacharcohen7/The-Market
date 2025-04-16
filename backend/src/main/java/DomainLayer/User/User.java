package DomainLayer.User;
//import  DomainLayer.Notifications.Observable;
//import  DomainLayer.Notifications.Observer;
//import  DomainLayer.Notifications.Notification;


import jakarta.persistence.*;

import Util.CartDTO;
import Util.UserDTO;
import jakarta.transaction.Transactional;
import org.bouncycastle.crypto.generators.BaseKDFBytesGenerator;
import org.hibernate.annotations.Cascade;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "user")
public class User   {

    @Id
    private String userID;

    @Transient
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private State state;

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "name")
    private String name;

    @Column(name = "ready_to_pay")
    private boolean readyToPay;

    @Column(name = "is_guest")
    private boolean isGuest;

    @Column(name = "member_id")
    private String member_ID;

    //@Transient
    //private Observer observer;
    // maps notification to a bool value: true - if was published to user, false - if wasn't

    //@ElementCollection
   // private Map<Notification,Boolean> notifications;

    public User(String userID){
        this.userID = userID;
        this.birthday = null;
        this.country = null;
        this.city = null;
        this.address = null;
        this.state = new Guest(); //default state
        this.name = null;
        this.readyToPay = false;
        this.isGuest = !state.isMember();
        this.member_ID = null;
        //this.cart = new Cart();

    }

    public User() {
        this.state = new Guest();
    }

    public void updateByDTO(UserDTO userDTO){
        //this.userID = userDTO.getUserId();
        this.birthday = userDTO.getBirthday();
        this.country = userDTO.getCountry();
        this.city = userDTO.getCity();
        this.address = userDTO.getAddress();
        this.name = userDTO.getName();
        this.isGuest = !state.isMember();
    }

    /*@Override
    public void registerObserver(Observer observer) {
        this.observer=observer;
        notifyObserver();
    }

    @Override
    public boolean notifyObserver(Notification notification) {
        if (notifications.get(notification)!=null && notifications.get(notification))
            return false; //was already published...
        boolean flag=notifications.putIfAbsent(notification, false)==null;
        if(observer.update(notification))
        {
            if(flag) {
                notifications.put(notification, true);
               // DAO.getInstance().merge(this);
                return true;
            }
            return true;
        }
        return false;
        //DAO.getInstance().merge(this);
    }*/
/*
    @Override
    public void notifyObserver() {
        LinkedList<Notification> published=new LinkedList<>();
        for(Notification notification:notifications.keySet()){
            if(notifyObserver(notification))
                published.add(notification);
        }

        for(Notification n: published){
            notifications.put(n,true);
        }
      //  if(published.size()>0)
          //  DAO.getInstance().merge(this);
    }

 */



    public String getUserID(){
        return userID;
    }

    public void setState(State state) {
        this.state = state;
        this.isGuest = !state.isMember();
        member_ID = state.getMemberID();
    }

    public String getCountry(){
        return this.country;
    }

    public String getName(){return this.name;}

    public String getCity(){return this.city;}

    public boolean isReadyToPay() {
        return readyToPay;
    }

    public void setReadyToPay(boolean readyToPay) {
        this.readyToPay = readyToPay;
    }

    public boolean isMember(){ return this.state.isMember();}

    public void Logout() {
        state.Logout();
        state = new Guest();
        this.isGuest = !state.isMember();
        this.member_ID = null;
    }

    public void exitMarketSystem() {
        //state.exitMarketSystem(this);
    }

    //@Transactional
    public void addToCart(String productName, int quantity, String storeId, int totalPrice)
    {
        state.addItemsToCart(productName, quantity, storeId, totalPrice);
    }

    public void modifyProductInCart(String productName, int quantity, String storeId, int totalPrice)
    {
        state.modifyProductInCart(productName, quantity, storeId, totalPrice);
    }

    @Transactional
    public void updateCartPrice()
    {
        state.calcCartTotal();
    }

    public void Login(Member loginMember) throws Exception {
        state.Login();
        setState(loginMember);
        this.birthday = loginMember.getBirthday();
        this.city = loginMember.getCity();
        this.name = loginMember.getName();
        this.country = loginMember.getCountry();
        this.address = loginMember.getAddress();
    }

    public State getState()
    {
        return state;
    }

    public boolean checkIfProductInUserCart(String productName, String storeId)
    {
        return state.checkIfProductInUserCart(productName, storeId);
    }

    public void removeItemFromUserCart(String productName, String storeId)
    {
        state.removeItemFromUserCart(productName, storeId);
    }
    public void setCart(Cart cart) {
        state.setCart(cart);
    }

    public void emptyCart(){
        getCart().emptyCart();
    }

    public Cart getCart() {
        return state.getCart();
    }


    public void addInfo(UserDTO userDTO){
        this.name = userDTO.getName();
        this.address = userDTO.getAddress();
        this.city = userDTO.getCity();
        this.country = userDTO.getCountry();
        this.birthday = userDTO.getBirthday();
    }



    public Map<String, List<Integer>> getCartProductsByStore(String storeId)
    {
        return state.getCartProductsByStore(storeId);
    }

    public List<String> getCartStores()
    {
        return state.getCartStores();
    }

    public boolean isCartEmpty()
    {
        return state.isCartEmpty();
    }

    public String getAddress(){
        return this.address;
    }

    public int getCartTotalPriceBeforeDiscount()
    {
        return state.getCartTotalPriceBeforeDiscount();
    }


    public String getBirthday(){
        return birthday;
    }

    public CartDTO getCartDTO(){
        return getCart().getDTO(userID);

    }

//    public void addAcquisition(String acquisitionId) {
//        state.addAcquisition(acquisitionId);
//    }

//    public int cancelAcquisition(String acquisitionId){
//        return state.removeAcquisition(acquisitionId);
//    }

//    public List<String> getAcquisitionIds() {
//        return state.getAcquisitionIds();
//    }

    public boolean getIsGuest(){
        return isGuest;
    }

    public void setMember_ID(String member_ID) {
        this.member_ID = member_ID;
    }

    public String getMember_ID() {
        return member_ID;
    }
}
