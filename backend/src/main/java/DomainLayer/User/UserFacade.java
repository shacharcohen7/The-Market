package DomainLayer.User;


import DomainLayer.Repositories.*;
import Util.CartDTO;
import Util.ExceptionsEnum;
import Util.UserDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.jose4j.jwk.Use;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import DomainLayer.Repositories.UserMemoryRepository;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserFacade {
    private static UserFacade userFacadeInstance;
    UserRepository userRepository;
    MemberRepository members;
    Map<String, Cart> guestCarts;

    @Autowired
    public UserFacade(UserRepository userRepository, MemberRepository members) throws Exception {
        this.userRepository = userRepository;
        this.members = members;
        guestCarts = new HashMap<>();
        //test();
    }

/*
    //TODO: remove this after done testing DB
    @Transactional
    public void test() throws Exception {
        String newUserId = addUser();
        //String newUserId2 = addUser();
        UserDTO userDTO = new UserDTO(newUserId, "testUser", "01/01/2000", "Test Country", "Test City", "123 Test St", "Test Name");
        register(newUserId, userDTO, "testPass");
        //Login(newUserId, "testUser", "testPass");
        //Login(newUserId2, "testUser", "testPass");

        addItemsToBasket("product1", 1, "store83afa8b9-2e7c-48a3-ad9f-498c8ad18eb9", newUserId, 100);
        addItemsToBasket("product2", 3, "store83afa8b9-2e7c-48a3-ad9f-498c8ad18eb", newUserId, 50);
        modifyBasketProduct("product1", 2, "store83afa8b9-2e7c-48a3-ad9f-498c8ad18eb9", newUserId, 200);
        removeItemFromUserCart("product2", "store83afa8b9-2e7c-48a3-ad9f-498c8ad18eb", newUserId);
        //userRepository.save(getUserByID(newUserId));
    }
*/

    public UserFacade()
    {
        userRepository = new UserMemoryRepository();
        members = new MemberMemoryRepository();
        guestCarts = new HashMap<>();
    }

    public void reset(){
        userRepository.deleteAll();
        members.deleteAll();
    }

    public String getMemberName(String memberId){
        return members.getById(memberId).getUsername();
    }

    public synchronized static UserFacade getInstance() {
        if (userFacadeInstance == null) {
            userFacadeInstance = new UserFacade();
        }
        return userFacadeInstance;
    }

    public UserFacade newForTest(){
        userFacadeInstance= new UserFacade();
        return userFacadeInstance;
    }

    public String getCurrentUserID (){
        UUID uuid = UUID.randomUUID();
        String uniqueId = "user-" + uuid;
        return uniqueId;
    }

    public String getCurrentMemberID (){
        UUID uuid = UUID.randomUUID();
        String uniqueId = "member-" + uuid;
        return uniqueId;
    }

    public User getUserByID(String userID){
        Optional<User> user = userRepository.findById(userID);
        User userToReturn = user.orElse(null);
        Optional<Member> member = null;

        if(userToReturn != null){
            if(!userToReturn.getIsGuest() && !userToReturn.isMember()) {
                member = members.findById(userToReturn.getMember_ID());
                Member memberToUpdate = member.orElse(null);
                members.save(memberToUpdate);
                userToReturn.setState(memberToUpdate);
                userRepository.save(userToReturn);
            }
            else if(userToReturn.getIsGuest()) //if user is guest cart is not saved in DB
            {
                if(guestCarts.containsKey(userID))
                    userToReturn.setCart(guestCarts.get(userID));
            }
        }
        return userToReturn;
    }

    public void errorIfUserNotExist(String userID) throws Exception {
        if (getUserByID(userID) == null){
            throw new Exception(ExceptionsEnum.userNotExist.toString());
        }
    }

    public void isUserLoggedInError(String userID) {
        if (!isMember(userID)) {
            throw new IllegalArgumentException(ExceptionsEnum.userIsNotMember.toString());
        }
    }

    /*public String getUsernameByUserID(String userID)
    {
        if(!userRepository.contain(userID)){
            return -1;
        }
        User user = getUserByID(userID);
        return ((Member)user.getState()).getMemberID();
    }*/

    public List<UserDTO> getUserDTOByMemberId(List<String> memberIdList){
        List<UserDTO> userDTOList = new ArrayList<>();
        for (String memberID : memberIdList){
            userDTOList.add(getUserDTOById(members.getById(memberID).getUserId()));
        }
        return userDTOList;
    }

    public String getUserIdByMemberId(String memberId){
        Member curr  = members.getById(memberId);
        if (curr!=null){
            return curr.getUserId();
        }
        return null;
    }

    @Transactional
    public void emptyCart(String userId){
        //Long cartId = getUserByID(userId).getCart().getCartId();
        //userRepository.deleteBasketsByCartId(cartId);
        //userRepository.updateCartPriceToZero(cartId);
        getUserByID(userId).emptyCart();
    }



    public boolean isMember(String userId){
        if(getUserByID(userId) == null){
            return false;
        }
        return getUserByID(userId).isMember();
    }


    public void errorIfUserNotMember(String userId) throws Exception {
        if(!isMember(userId)){
            throw new Exception(ExceptionsEnum.userIsNotMember.toString());
        }
    }


    public String getMemberIdByUserId(String userID) throws Exception {
        if(isMember(userID)){
            String username = getUserByID(userID).getState().getUsername();
            return getMemberByUsername(username).getMemberID();
        }
        else{
            return getUserByID(userID).getMember_ID();
        }
        //else {
       //     return null;
            //throw new Exception("User is not a member");
        //}
    }

    public void exitMarketSystem(String userID){

        userRepository.getById(userID).exitMarketSystem();
        userRepository.deleteById(userID); //todo do i need to remove the user from the list of users ?
    }


    public String addUser(){
        String userId;
        userId = getCurrentUserID();
        userRepository.save(new User(userId));
        return userId;
    }

    @Transactional
    public void addItemsToBasket(String productName, int quantity, String storeId, String userId, int totalPrice)
    {
        User user = getUserByID(userId);
        user.addToCart(productName, quantity, storeId, totalPrice);
        user.updateCartPrice();

        //save cart if user state is member
        if(user.isMember())
            members.save((Member) user.getState());
        else
            guestCarts.put(userId, user.getCart());

        userRepository.save(user);
    }

    public void modifyBasketProduct(String productName, int quantity, String storeId, String userId, int totalPrice)
    {
        User user = getUserByID(userId);
        user.modifyProductInCart(productName, quantity, storeId, totalPrice);
        user.updateCartPrice();

        //save cart if user state is member
        if (user.isMember())
            members.save((Member) user.getState());
        else
            guestCarts.put(userId, user.getCart());
        userRepository.save(user);
    }

    public void checkIfCanRemove(String productName, String storeId, String userId)
    {
        User user = getUserByID(userId);
        if(!user.checkIfProductInUserCart(productName, storeId))
            throw new IllegalArgumentException(ExceptionsEnum.productNotExistInCart.toString());
    }

    public void removeItemFromUserCart(String productName, String storeId, String userId)
    {
        User user = getUserByID(userId);
        user.removeItemFromUserCart(productName, storeId);
        user.updateCartPrice();

        //save cart if user state is member
        if (user.isMember())
            members.saveAndFlush((Member) user.getState());
        else
            guestCarts.put(userId, user.getCart());
    }


    @Transactional
    public String register(String userID, UserDTO user,String password) throws Exception {
        if(userRepository.existsById(userID)&& getUserByID(userID).isMember()) {
            throw new Exception(ExceptionsEnum.memberCannotRegister.toString());
        }
        else {
            validateRegistrationDetails(user, password);
            String memberId = getCurrentMemberID();

            Member newMember = new Member(userID, memberId,user.getUserName(), user.getAddress(), user.getName(), password, user.getBirthday(), user.getCountry(), user.getCity());
            members.save(newMember);
            User userToUpdate = getUserByID(userID);
            userToUpdate.addInfo(user);
            userToUpdate.setMember_ID(memberId);
            this.userRepository.save(userToUpdate);
            //todo pass the user to login page.
            return memberId;
        }
    }

    /*public String registerSystemAdmin(UserDTO user, String password) throws Exception {

        validateRegistrationDetails(user,password);
        String memberId = getCurrentMemberID();

        Member newMember = new Member(user.getUserId(), memberId,user.getUserName(), user.getAddress(), user.getName(), password, user.getBirthday(), user.getCountry(), user.getCity());
            members.add(memberId, newMember);

        return memberId;
        //todo pass the user to login page.
    }*/



    private void validateRegistrationDetails(UserDTO user, String password) throws Exception {
        if (user.getUserName() == null || password == null || user.getBirthday() == null || user.getCountry() ==null || user.getCity() == null ||
                user.getAddress() == null || user.getName() == null) {
            throw new Exception(ExceptionsEnum.emptyField.toString());
        }
        //checking empty fields
        else if (user.getUserName().equals("") || password.equals("") || user.getBirthday().equals("") || user.getCountry().equals("") || user.getCity().equals("") ||
                user.getAddress().equals("") || user.getName().equals("")) {
            throw new Exception(ExceptionsEnum.emptyField.toString());
        }
        //checking if username is already exist
        Member mem = members.getByUserName(user.getUserName());
        if (mem!=null) {
            throw new Exception(ExceptionsEnum.usernameAlreadyExist.toString());

        }
        // Check validation of the birthday
        if (!isValidDate(user.getBirthday())) {
            throw new Exception(ExceptionsEnum.invalidFormatDate.toString());
        }
        if (isFutureDate(user.getBirthday())) {
            throw new Exception(ExceptionsEnum.futureDate.toString());
        }
//        // Check validation of the country and city
//        if (!isValidCountry(user.getCountry())) {
//            throw new Exception(ExceptionsEnum.invalidCountry.toString());
//        }
//        if (!isValidCity(user.getCity(), user.getCountry())) {
//            throw new Exception(ExceptionsEnum.invalidCity.toString());
//        }
    }

    private boolean isValidDate(String date) {
        List<SimpleDateFormat> dateFormats = new ArrayList<>();
        dateFormats.add(new SimpleDateFormat("dd/MM/yyyy"));
        dateFormats.add(new SimpleDateFormat("dd/MM/yy"));

        for (SimpleDateFormat sdf : dateFormats) {
            sdf.setLenient(false);
            try {
                sdf.parse(date);
                return true;
            } catch (ParseException e) {
                // Ignore and try the next format
            }
        }
        return false;
    }

    private boolean isFutureDate(String date) throws ParseException {
        List<SimpleDateFormat> dateFormats = new ArrayList<>();
        dateFormats.add(new SimpleDateFormat("dd/MM/yyyy"));
        dateFormats.add(new SimpleDateFormat("dd/MM/yy"));

        for (SimpleDateFormat sdf : dateFormats) {
            try {
                Date birthday = sdf.parse(date);
                Date today = new Date();
                return birthday.after(today);
            } catch (ParseException e) {
                // Ignore and try the next format
            }
        }

        throw new ParseException("Invalid date format", 0);
    }


    public String Login(String userID, String username, String password) throws Exception {
        Member loginMember = getMemberByUsername(username);
        if (loginMember == null){
            throw new Exception(ExceptionsEnum.usernameOrPasswordIncorrect.toString());
        }
        /*else if (!loginMember.getPassword().equals(password)){
            throw new Exception("Username or password is incorrect");
        }*/
        loginMember.validatePassword(password);
        User user = getUserByID(userID);

        //remove guest cart from GuestCarts
        if(user.getIsGuest())
            guestCarts.remove(userID);

        user.Login(loginMember);
        loginMember.setUserId(userID);
        this.userRepository.save(user);
        this.members.save(loginMember);
        return loginMember.getMemberID();
    }

    public void logout(String userID) {
        User user = getUserByID(userID);
        user.Logout();
        this.userRepository.save(user);
    }

    public Member getMemberByUsername(String userName) {
        return members.getByUserName(userName);
    }

    public void errorIfUsernameNotFound(String userName) throws Exception {
        if(getMemberByUsername(userName) == null){
            throw new Exception(ExceptionsEnum.usernameNotFound.toString());
        }
    }

    public List<String> getCartStoresByUser(String user_ID)
    {
        User user = getUserByID(user_ID);

        if(user != null)
            return user.getCartStores();
        else
            return null;
    }

    public Map<String, List<Integer>> getCartProductsByStoreAndUser(String store_ID, String user_ID)
    {
        User user = getUserByID(user_ID);

        if(user != null)
            return user.getCartProductsByStore(store_ID);
        else
            return null;
    }

    public void isUserCartEmpty(String user_ID) throws Exception
    {
        if(getUserByID(user_ID).isCartEmpty())
            throw new Exception(ExceptionsEnum.userCartIsEmpty.toString());
    }

    public String getUserAddress(String user_ID)
    {
        return getUserByID(user_ID).getAddress();
    }

    public int getCartPriceByUser(String user_ID)
    {
        /*this function returns the cart total price before discounts, of a specific user*/
        return getUserByID(user_ID).getCartTotalPriceBeforeDiscount();
    }

    public List<UserDTO> getAllUsers(){
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user:users ){
            userDTOList.add(new UserDTO(user));
        }
        return userDTOList;
    }

    public UserDTO getUserDTOById(String userID){
        return new UserDTO(getUserByID(userID));
    }

    public UserDTO updateUser( UserDTO userDTO){
        User user = getUserByID(userDTO.getUserId());
        user.updateByDTO(userDTO);
        userRepository.save(user);
        return new UserDTO(user);
    }


    public void setUserConfirmationPurchase(String userId){
        User user = getUserByID(userId);
        user.setReadyToPay(true);
        userRepository.save(user);
    }

    public boolean getUserConfirmationPurchase(String userID){
        User user = getUserByID(userID);
        return userRepository.getReadyToPay(userID);
    }


    public void removeUser(String userId){
        userRepository.deleteById(userId);
        guestCarts.remove(userId);
    }

    public CartDTO getCartDTO(String userId){
        return getUserByID(userId).getCartDTO();
    }

    public MemberRepository getMembers() {
        return members;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

//    public void addAcquisitionToUser(String userId, String acquisitionId) {
//        User user = getUserByID(userId);
//        user.addAcquisition(acquisitionId);
//
//        //TODO: check if correct
//        //save acquisition only if member
//        if(user.isMember())
//            members.save((Member) user.getState());
//        userRepository.save(user);
//    }

//    public List<String> getUserAcquisitionsHistory(String userId) {
//        return getUserByID(userId).getAcquisitionIds();
//    }

//    public int cancelPaynmet(String userId, String acquisitionId){
//        User user = getUserByID(userId);
//        int result = user.cancelAcquisition(acquisitionId);
//
//        //todo check if correct
//        //save acquisition only if member
//        if(user.isMember())
//            members.save((Member) user.getState());
//        userRepository.save(user);
//
//        return result;
//    }

//    public void checkIfUserHasAcquisition(String userId, String acquisitionId) {
//        if(!getUserByID(userId).getAcquisitionIds().contains(acquisitionId))
//            throw new IllegalArgumentException(ExceptionsEnum.AcquisitionNotExist.toString());
//    }
}
