package DomainLayer.Role;

import DomainLayer.Repositories.MemoryStoreManagerRepository;
import DomainLayer.Repositories.MemoryStoreOwnerRepository;
import DomainLayer.Repositories.StoreManagerRepository;
import DomainLayer.Repositories.StoreOwnerRepository;
import Util.ExceptionsEnum;
import Util.StoreManagerDTO;
import Util.StoreOwnerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleFacade {

    private static RoleFacade roleFacadeInstance;


    //private List<SystemManager> systemManagers;
    //private List<SystemManager> systemManagersNominators;
    private final Object systemManagerLock;
    //private final Object systemManagerNominatorsLock;
    StoreOwnerRepository storeOwnerRepository;
    StoreManagerRepository storeManagerRepository;



    public RoleFacade() {
        //systemManagers = new ArrayList<>();
        //systemManagersNominators= new ArrayList<>();
        //systemManagerNominatorsLock = new Object();
//        managerNominators = new MemoryStoreManagerRepository() ;
//        ownersNominators = new MemoryStoreOwnerRepository() ;
        storeManagerRepository = new MemoryStoreManagerRepository();
        storeOwnerRepository = new MemoryStoreOwnerRepository();
        systemManagerLock = new Object();


    }

    @Autowired
    public RoleFacade(StoreManagerRepository storeManagerRepository, StoreOwnerRepository storeOwnerRepository) {
        //systemManagers = new ArrayList<>();
        //systemManagersNominators= new ArrayList<>();
//        this.managerNominators = managerNominators;
//        this.ownersNominators = ownersNominators;
        this.storeManagerRepository = storeManagerRepository;
        this.storeOwnerRepository = storeOwnerRepository;
        systemManagerLock = new Object();
        //systemManagerNominatorsLock = new Object();
    }

    public static synchronized RoleFacade getInstance() {
        if (roleFacadeInstance == null) {
            roleFacadeInstance = new RoleFacade();
        }
        return roleFacadeInstance;
    }

    public RoleFacade newForTest(){
        roleFacadeInstance = new RoleFacade();
        return roleFacadeInstance;
    }

    public void reset(){
        storeManagerRepository.deleteAll();
        storeManagerRepository.deleteAllSystemManager();
        storeOwnerRepository.deleteAll();
    }


    public boolean verifyStoreOwner(String storeID, String memberID) {
        return getStoreOwner(storeID, memberID) != null;
    }


    public void verifyStoreOwnerError(String storeID, String memberID) throws Exception {
        if (!verifyMemberIsSystemManager(memberID) && !verifyStoreOwner(storeID, memberID))
            throw new Exception(ExceptionsEnum.userIsNotStoreOwner.toString());
    }

    public void verifyMangerNominatorError(String nominatorMemberID, String nominatedMemberID, String storeID) throws Exception {
        StoreManager storeManager =storeManagerRepository.getStoreManagerNominator(storeID, nominatedMemberID);
        if (storeManager== null  || !storeManager.getNominatorMemberId().equals(nominatorMemberID))
            throw new Exception(ExceptionsEnum.notNominatorOfThisEmployee.toString());
    }

    public void verifyOwnerNominatorError(String nominatorMemberID, String nominatedMemberID, String storeID) throws Exception {
        StoreOwner storeOwner =storeOwnerRepository.getStoreOwnerNominator(storeID, nominatedMemberID);
        if (storeOwner==null || !storeOwner.getNominatorId().equals( nominatorMemberID))
            throw new Exception(ExceptionsEnum.notNominatorOfThisEmployee.toString());
    }

    public StoreOwner getStoreOwner(String storeID, String memberID) {
        return storeOwnerRepository.getStoreOwner(storeID, memberID);
    }

    public boolean verifyStoreManager(String storeID, String memberID) {
        return getStoreManager(storeID, memberID) != null;
    }

    public StoreManager getStoreManager(String storeID, String memberID) {
        return storeManagerRepository.getStoreManager(storeID, memberID);
    }

    public boolean verifyStoreOwnerIsFounder(String storeID, String memberID) {
        StoreOwner storeOwner = getStoreOwner(storeID, memberID);
        return verifyMemberIsSystemManager(memberID) || storeOwner != null
                && storeOwner.verifyStoreOwnerIsFounder();
    }

    public void createStoreOwnerWithoutAsk(String memberId, String storeId, boolean founder, String nominatorMemberId) throws Exception {
        if (verifyStoreOwner(storeId, memberId))
            throw new Exception(ExceptionsEnum.memberIsAlreadyStoreOwner.toString());
        StoreOwner newStoreOwner = new StoreOwner(memberId, storeId, founder, nominatorMemberId, false);
        addNewStoreOwnerToTheMarket(newStoreOwner);
    }

    public void createStoreManagerWithoutAsk(String memberId, String storeId,
                                             boolean inventoryPermissions, boolean purchasePermissions, String nominatorMemberId) throws Exception {
        if (!verifyStoreOwner(storeId, memberId) && !verifyStoreManager(storeId, memberId)) {
            StoreManager newStoreManager = new StoreManager(memberId, storeId, inventoryPermissions, purchasePermissions, nominatorMemberId, false);
            addNewStoreManagerToTheMarket(newStoreManager);
        } else {
            throw new Exception(ExceptionsEnum.memberAlreadyHasRoleInThisStore.toString());
        }
    }


    public String approveInvitationStoreOwner(String memberId, String storeId) throws Exception {
        if (verifyStoreOwner(storeId, memberId)) {
            throw new Exception(ExceptionsEnum.memberIsAlreadyStoreOwner.toString());
        }
        StoreOwner newStoreOwner = storeOwnerRepository.getStoreOwnerNominator(storeId, memberId);
        if (newStoreOwner==null){
            throw new IllegalArgumentException("the invitation no longer exist");
        }
        newStoreOwner.setInProposal(false);
        addNewStoreOwnerToTheMarket(newStoreOwner);
        return newStoreOwner.getNominatorId();
    }

    public String declineInvitationStoreOwner(String memberId, String storeId) throws Exception {
        if (verifyStoreOwner(storeId, memberId)) {
            throw new Exception(ExceptionsEnum.memberIsAlreadyStoreOwner.toString());
        }

        StoreOwner newStoreOwner = storeOwnerRepository.getStoreOwnerNominator(storeId, memberId);

        if (newStoreOwner== null){
            throw new IllegalArgumentException("no available offer for this member Id and store Id");
        }

        storeOwnerRepository.delete(newStoreOwner);
        return newStoreOwner.getNominatorId();

    }

    public String approveInvitationStoreManager(String memberId, String storeId) throws Exception {
        if (verifyStoreOwner(storeId, memberId) || verifyStoreManager(storeId, memberId)) {
            throw new Exception("member already manager or owner in this store");
        }
        StoreManager newStoreManager = storeManagerRepository.getStoreManagerNominator(storeId, memberId);
        if (newStoreManager==null){
            throw new IllegalArgumentException("the invitation no longer exist");
        }
        newStoreManager.setInProposal(false);
        //storeManagerRepository.save(newStoreManager);
        addNewStoreManagerToTheMarket(newStoreManager);
        return newStoreManager.getNominatorMemberId();
    }

    public String declineInvitationStoreManager(String memberId, String storeId) throws Exception {
        if (verifyStoreOwner(storeId, memberId) || verifyStoreManager(storeId, memberId)) {
            throw new Exception("member already manager or owner in this store");
        }
        StoreManager newStoreManager = storeManagerRepository.getStoreManagerNominator(storeId, memberId);
        if (newStoreManager== null){
            throw new IllegalArgumentException("no available offer for this member Id and store Id ");
        }
        storeManagerRepository.delete(newStoreManager);
        return newStoreManager.getNominatorMemberId();
        //addNewStoreOwnerToTheMarket(newStoreOwner);
    }


//    public void proposeSystemManager(String newSystemManagerMemberId ,String currSystemManager ){
//        verifyMemberIsSystemManager(currSystemManager);
//        if (verifyMemberIsSystemManager(newSystemManagerMemberId)){
//            throw new IllegalArgumentException("member is already system manager");
//        }
//        if (verifyAlreadySystemManagerNominator(newSystemManagerMemberId)){
//            throw new IllegalArgumentException("system is already nominated to be system manager");
//        }
//        synchronized (systemManagerNominatorsLock){
//           systemManagersNominators.add(new SystemManager(newSystemManagerMemberId, currSystemManager));
//        }
//
//    }

//    public boolean verifyAlreadySystemManagerNominator(String memberID){
//        synchronized (systemManagerNominatorsLock) {
//            for (SystemManager systemManager : systemManagersNominators) {
//                if (systemManager.getMember_ID()==memberID){
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

//    public SystemManager getSystemManagerNominator(String memberId){
//        synchronized (systemManagerNominatorsLock) {
//            for (SystemManager systemManager : systemManagersNominators) {
//                if (systemManager.getMember_ID() == memberId) {
//                    return systemManager;
//                }
//            }
//        }
//        return  null;
//    }


    public void addSystemManager(String systemManagerMemberId) {
        storeManagerRepository.addSystemManager(systemManagerMemberId);
    }

    public void updateStoreManagerPermissions(String memberId, String storeId,
                                              boolean inventoryPermissions, boolean purchasePermissions, String nominatorMemberID) throws Exception {
        if (verifyStoreManager(storeId, memberId)) {
            StoreManager storeManager =getStoreManager(storeId, memberId);
            if (storeManager!=null && storeManager.getNominatorMemberId().equals(nominatorMemberID)) {
                //StoreManager storeManager = getStoreManager(storeId, memberId);
                storeManager.setPermissions(inventoryPermissions, purchasePermissions);
                //storeManager = getStoreManager(storeId, memberId);
                //storeManagerRepository.updateStoreManagerPermissions(memberId,storeId,inventoryPermissions,purchasePermissions,nominatorMemberID);
                storeManagerRepository.save(storeManager);
            } else {
                throw new Exception(ExceptionsEnum.notNominatorOfThisEmployee.toString());
            }
        } else {
            throw new Exception(ExceptionsEnum.notManager.toString());
        }
    }

    public boolean managerHasInventoryPermissions(String member_ID, String store_ID) {
        return verifyStoreManager(store_ID, member_ID) && getStoreManager(store_ID, member_ID).hasInventoryPermissions();
    }

    public boolean managerHasPurchasePermissions(String member_ID, String store_ID) {
        return verifyStoreManager(store_ID, member_ID) && getStoreManager(store_ID, member_ID).hasPurchasePermissions();
    }

    private void addNewStoreManagerToTheMarket(StoreManager storeManager) {
        storeManagerRepository.save(storeManager);
    }

    private void addNewStoreManagerNominatorToTheMarket(StoreManager storeManager) {
        storeManagerRepository.save(storeManager);
    }

    private void addNewStoreOwnerNominatorToTheMarket(StoreOwner storeOwner) {
        storeOwnerRepository.save(storeOwner);
    }

    private void addNewStoreOwnerToTheMarket(StoreOwner storeOwner) {
        storeOwnerRepository.save(storeOwner);
    }

    public void addNewStoreManagerToTheMarketForTests(StoreManager storeManager) {
        if(!verifyStoreManager(storeManager.getStore_ID(), storeManager.getMember_ID()))
            storeManagerRepository.save(storeManager);
    }

    public void addNewStoreOwnerToTheMarketForTests(StoreOwner storeOwner) {
        if(!verifyStoreOwner(storeOwner.getStore_ID(), storeOwner.getMember_ID()))
            storeOwnerRepository.save(storeOwner);
    }


    public Map<String, String> getInformationAboutStoreRoles(String store_ID) {
        List<String> storeManagers = getAllStoreManagers(store_ID);
        List<String> storeOwners = getAllStoreOwners(store_ID);

        Map<String, String> storeRoles = new HashMap<>();

        for (String managerId : storeManagers) {
            storeRoles.put(managerId, "manager");
        }

        for (String ownerId : storeOwners) {
            storeRoles.put(ownerId, "owner");
        }

        return storeRoles;
    }

    public Map<String, List<Integer>> getStoreManagersAuthorizations(String storeID) {
        Map<String, List<Integer>> managersAuthorizations = new HashMap<>();
        for (String memberId : storeManagerRepository.getAllMemberId()) {
            for (StoreManager currStoreManager : storeManagerRepository.getAllMemberIdManagers(memberId)) {
                if (currStoreManager.getStore_ID().equals(storeID)) {
                    managersAuthorizations.put(memberId, currStoreManager.getAuthorizations());
                }
            }
        }
        return managersAuthorizations;
    }

    public List<String> getAllStoreManagers(String storeID) {
        List<String> storeManagers = new ArrayList<>();
        for (String memberId : storeManagerRepository.getAllMemberId()) {
            for (StoreManager currStoreManager : storeManagerRepository.getAllMemberIdManagers(memberId)) {
                if (currStoreManager.getStore_ID().equals(storeID)) {
                    storeManagers.add(currStoreManager.getMember_ID());
                }
            }
        }

        return storeManagers;
    }

    public List<String> getAllStoreOwners(String storeID) {
        List<String> storeOwners = new ArrayList<>();

        for (String memberId : storeOwnerRepository.getAllMemberId()) {
            for (StoreOwner currStoreOwner : storeOwnerRepository.getAllMemberIdOwners(memberId)) {
                if (currStoreOwner.getStore_ID().equals(storeID)) {
                    storeOwners.add(currStoreOwner.getMember_ID());
                }
            }
        }
        return storeOwners;
    }


    public List<String> getStoresByOwner(List<String> stores, String member_ID) {
        //this function gets list of stores id and member id, and return only stores id in which the member is owner/

        List<String> storesOwned = new ArrayList<>();

        for (String storeID : stores) {
            if (verifyStoreOwner(storeID, member_ID))
                storesOwned.add(storeID);
        }
        return storesOwned;
    }

    /*public void getStoresByOwners(List<StoreOwner> storeOwnersList) {
        synchronized (storeOwnersList) {
            this.storeOwnersList = storeOwnersList;
        }
    }*/


    public void addManagerNominator(String memberId, String storeId,
                                    boolean inventoryPermissions, boolean purchasePermissions, String nominatorMemberId) throws Exception {
        StoreManager storeManager = storeManagerRepository.getStoreManagerNominator(storeId,memberId);
        StoreOwner storeOwner = storeOwnerRepository.getStoreOwnerNominator(storeId,memberId);
        if (storeManager!=null || storeOwner!=null ){
            throw new IllegalArgumentException("this member already nominated to be store manager in this store");
        }
        if (!verifyStoreOwner(storeId, memberId) && !verifyStoreManager(storeId, memberId)) {
            if ((!verifyStoreManagerNominator(memberId, storeId)) && !(verifyStoreOwnerNominator(memberId, storeId))) {
                StoreManager newStoreManager = new StoreManager(memberId, storeId, inventoryPermissions, purchasePermissions, nominatorMemberId, true);
                addNewStoreManagerNominatorToTheMarket(newStoreManager);
            }
            else {
                throw new Exception("member is already nominator to job in this store");
            }
        }
        else {
            throw new Exception(ExceptionsEnum.memberAlreadyHasRoleInThisStore.toString());
        }
    }

    public boolean verifyStoreOwnerNominator(String memberId, String storeId){
        return storeOwnerRepository.getStoreOwnerNominator(storeId,memberId)!=null;
    }

    public boolean verifyStoreManagerNominator(String memberId, String storeId){
        return storeManagerRepository.getStoreManagerNominator(storeId,memberId)!=null;
    }


    public void addOwnerNominator(String memberId, String storeId, boolean founder, String nominatorMemberId) throws Exception {
        StoreOwner storeOwner = storeOwnerRepository.getStoreOwnerNominator(storeId,memberId);
        StoreManager storeManager = storeManagerRepository.getStoreManagerNominator(memberId,storeId);
        if (storeOwner!=null || storeManager!=null) {
            throw new IllegalArgumentException("this member already nominated to be store owner in this store");
        }
        if (verifyStoreOwner(storeId, memberId))
            throw new Exception(ExceptionsEnum.memberIsAlreadyStoreOwner.toString());

        StoreOwner newStoreOwner = new StoreOwner(memberId, storeId, founder, nominatorMemberId, true);
        addNewStoreOwnerNominatorToTheMarket(newStoreOwner);
    }

    public List<String> getSystemManagers(){
        List<String> systemManagersList = new ArrayList<>();
        for (String systemManager :storeManagerRepository.getAllSystemManagers())
        {
            systemManagersList.add(systemManager);
        }
        return systemManagersList;
    }

    public boolean verifyMemberIsSystemManager(String member_ID) {
        for (String systemManagerID :storeManagerRepository.getAllSystemManagers()){
            if (systemManagerID.equals(member_ID)){
                return true;
            }
        }
        return false;
    }

    public void verifyMemberIsSystemManagerError(String member_ID) throws Exception {
        if (!verifyMemberIsSystemManager(member_ID))
            throw new Exception(ExceptionsEnum.notSystemManager.toString());
    }

    public List<StoreManagerDTO> getAllManagerProposal(String memberId){
        List <StoreManagerDTO> dtos= new ArrayList<>();
        List<StoreManager> storeManagerList = storeManagerRepository.getAllMemberIdNominatorsManagers(memberId);
        if (storeManagerList!= null) {
            for (StoreManager storeManager : storeManagerList) {
                dtos.add(new StoreManagerDTO(storeManager.getMember_ID(), storeManager.getStore_ID()
                        , storeManager.hasInventoryPermissions(), storeManager.hasPurchasePermissions(),
                        storeManager.getNominatorMemberId()));

            }
        }
        return dtos;
    }


    public List<StoreOwnerDTO> getAllOwnersProposal(String memberId){
        List <StoreOwnerDTO> dtos= new ArrayList<>();
        List<StoreOwner> storeOwnerList = storeOwnerRepository.getAllMemberIdNominatorsOwners(memberId);
        if (storeOwnerList!=null) {
            for (StoreOwner storeOwner : storeOwnerList) {
                dtos.add(new StoreOwnerDTO(storeOwner.getMember_ID(), storeOwner.getStore_ID(),
                        storeOwner.getFounder(), storeOwner.getNominatorId()));
            }
        }
        return dtos;
    }

    public static void resetInstanceForTests() {
        roleFacadeInstance = null;
    }

    public void fireStoreOwner(String memberIdToFire, String storeID){
        List<String> ownersOfTheStore = getAllStoreOwners(storeID);
        for(int i=0 ; i<ownersOfTheStore.size() ; i++){
            String storeOwnerId = ownersOfTheStore.get(i);
            StoreOwner storeOwner= storeOwnerRepository.get(storeID, storeOwnerId);
            if(storeOwner!=null && storeOwner.getNominatorId().equals(memberIdToFire)){
                fireStoreOwner(storeOwnerId, storeID);
            }
        }
        List<String> managersOfTheStore = getAllStoreManagers(storeID);
        for(int i=0 ; i<managersOfTheStore.size() ; i++){
            String storeManagerId = managersOfTheStore.get(i);
            StoreManager storeManager= storeManagerRepository.get(storeID, storeManagerId);
            if(storeManager!= null && storeManager.getNominatorMemberId().equals(memberIdToFire)){
                fireStoreManager(storeManagerId, storeID);
            }
        }
//        List<StoreOwner> nominatorsOwners = ownersNominators.getAllMemberIdOwners(storeID);
//        for(StoreOwner storeOwner: nominatorsOwners){
//            if(storeOwner.getNominatorId().equals(memberIdToFire)){
//                nominatorsOwners.remove(storeOwner);
//            }
//        }
//        List<StoreManager> nominatorsManagers = managerNominators.getAllMemberIdManagers(storeID);
//        for(StoreManager storeManager: nominatorsManagers){
//            if(storeManager.getNominatorMemberId().equals(memberIdToFire)){
//                nominatorsManagers.remove(storeManager);
//            }
//        }
        StoreOwner storeOwner =storeOwnerRepository.get(storeID, memberIdToFire);
        if (storeOwner!=null) {
            storeOwnerRepository.delete(storeOwner);
        }
    }

    public void fireStoreManager(String memberIdToFire, String storeID){
        StoreManager storeManager = storeManagerRepository.get(storeID, memberIdToFire);
        if (storeManager==null){
            throw new IllegalArgumentException("this user is no longer store manager in this store");
        }
        storeManagerRepository.delete(storeManager);
    }
}
