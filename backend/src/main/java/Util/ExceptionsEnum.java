package Util;



    public enum ExceptionsEnum{
        sessionOver{
            public String toString() {
                return "your session was over please log in again";
            }
        },
        userIsNotMember{
            public String toString() {
                return "User is not logged in, so he can't perform this operation";
            }
        },
        storeNotExist{
            public String toString() {
                return "Store does not exist";
            }
        },
        userNotExist{
            public String toString() {
                return "User does not exist";
            }
        },
        usernameNotFound{
            public String toString(){ return "Username was not found"; }
        },
        usernameAlreadyExist{
            public String toString(){ return  "Username already exists."; }
        },
        productNotExistInStore{
            public String toString() {
                return "product does not exist in store";
            }
        },
        productAlreadyExistInStore{
            public String toString() {
                return "Product already exist in this store";
            }
        },
        productQuantityNotExist{
            public String toString() {return "The quantity you entered isn't available in the store";}
        },
        productQuantityIsNegative{
            public String toString() {return "The quantity you entered is negative or zero";}
        },
        productNotExistInCart{
            public String toString() {
                return "product does not exist in cart";
            }
        },
        purchasePolicyIsNotMet{
            public String toString() {return "The product doesn't meet the store purchase policy";}
        },
        discountPolicyIsNotMet{
            public String toString() {return "The product doesn't meet the store discount policy";}
        },
        userCartIsEmpty{
            public String toString() {return "User cart is empty, there's nothing to purchase";}
        },
        ExternalSupplyServiceIsNotAvailable{
            public String toString() {return "Unfortunately, there is no shipping for the user address";}
        },

        NoExternalSupplyService{
            public String toString() {return "There is no external supply service in the system";}
        },
        ExternalSupplyServiceIsNotAvailableForArea{
            public String toString() {return "There is no external supply service for this area";}
        },

        createShiftingError{
            public String toString() {return "There is no external supply service for this area";}
        },
        productNotExistInMarket{
            public String toString() {
                return "product does not exist in market";
            }
        },
        userIsNotStoreOwner{
            public String toString() {
                return "User is not a store owner, only store owner can perform this operation";}
        },
        storeOwnerIsNotFounder {
            public String toString() {
                return "Only store founder can close a store";
            }
        },
        illegalStoreName{
            public String toString() {
                return "Illegal store name. Store name is empty.";}
        },
        memberIsAlreadyStoreOwner{
            public String toString() {
                return "Member is already owner of this store";
            }
        },
        memberAlreadyHasRoleInThisStore{
            public String toString() {
                return "Member already has a role in this store";
            }
        },
        notNominatorOfThisEmployee{
            public String toString() {
                return "Store owner is not the employee's nominator";
            }
        },
        notManager{
            public String toString() {
                return "User is not a manager of this store";
            }
        },
        usernameOrPasswordIncorrect{
            public String toString(){ return "username or password is incorrect"; }
        },
        userAlreadyLoggedIn{
            public String toString(){ return "user is already logged in"; }
        },
        priceRangeInvalid{
            public String toString(){ return "The price range you entered is invalid"; }
        },
        productRateInvalid{
            public String toString(){ return "The product rate you entered is invalid"; }
        },
        storeRateInvalid{
            public String toString(){ return "The store rate you entered is invalid"; }
        },
        categoryNotExist{
            public String toString(){ return "The product category you entered is invalid"; }
        },
        noInventoryPermissions{
            public String toString(){ return "User has no inventory permissions"; }
        },
        notSystemManager{
            public String toString(){ return "The user is not a system manager"; }
        },
        emptyField{
            public String toString(){ return "Fields cannot be empty"; }
        },
        passwordInvalid{
            public String toString(){ return "password must contains at least one digit, lowercase letter and uppercase letter.\n password must contains at least 8 characters"; }
        },
        memberCannotRegister{
            public String toString(){ return "member cannot register"; }
        },
        invalidFormatDate{
            public String toString(){ return "Invalid date format."; }
        },
        futureDate{
            public String toString(){ return "Birthday cannot be in the future."; }
        },
        invalidCountry{
            public String toString(){ return "Country is not exist"; }
        },
        invalidCity{
            public String toString(){ return "City is not exist"; }
        },
        rulesNotMatchOpeators{
            public String toString(){ return "The number of operators must be one less than the number of rules"; }
        },
        InvalidOperator{
            public String toString(){ return "The operator you entered is invalid"; }
        },
        InvalidDiscountValueParameters{
            public String toString(){ return "The discount must apply to the basket or some of store's the products or to a category"; }
        },
        SystemManagerPaymentAuthorization{
            public String toString(){
                return "Only system manager is allowed to add new external payment service";}
        },
        SystemManagerSupplyAuthorization{
            public String toString(){
                return "Only system manager is allowed to add new external supply service";}
        },

        SystemManagerSupplyAuthorizationRemove{
            public String toString(){
                return "Only system manager is allowed to remove external supply service";}
        },

        SystemManagerPaymentAuthorizationRemove{
            public String toString(){
                return "Only system manager is allowed to remove external payment service";}
        },

        InvalidPaymentServiceParameters{
            public String toString(){
                return "The system has not been able to add the payment service due to invalid details";}
        },

        InvalidSupplyServiceParameters{
            public String toString(){
                return "The system has not been able to add the supply service due to invalid details";}
        },

        OnlySupplyService{
            public String toString(){
                return "There must remain at least one external supply service in the system";}
        },

        OnlyPaymentService{
            public String toString(){
                return "There must remain at least one external payment service in the system";}
        },

        InvalidCreditCardParameters{
            public String toString(){
                return "There is a problem with the credit card provided details.";}
        },

        noAvailableExternalPaymentService{
            public String toString(){
                return "There is no available external payment system";}
        },

        checkHandShake{
            public String toString(){
                return "HandShake failed, can't make the action.";}
        },

        noPayment{
            public String toString(){
                return "Payment was never done, cant abort.";}
        },

        cancelFailed{
            public String toString(){
                return "The cancellation has been failed.";}
        },

        CreditCardIssue{
            public String toString(){
                return "The external payment service detected a problem with your credit card";
            }
        },

        InvalidSupplyServiceDetails{
            public String toString(){
                return "The system has not been able to be launched since there is a problem with the supply service details";
            }
        },

        InvalidPaymentServiceDetails{
            public String toString(){
                return "The system has not been able to be launched since there is a problem with the payment service details";
            }
        },

        InvalidRuleNumber{
            public String toString(){
                return "The rule number you entered is invalid";
            }
        },

        InvalidRuleIndex{
            public String toString(){
                return "the index you entered is invalid";
            }
        },
        TimeExpired{
            public String toString(){
                return "The time has been expired";
            }
        },

        ExternalPaymentFailed{
            public String toString(){
                return "The payment with the external payment has failed";
            }
        },
        PaymentFailed{
            public String toString(){
                return "Payment failed";
            }
        },
        AcquisitionNotExist{
            public String toString(){
                return "Acquisition does not exist";
            }
        },

        InvalidRuleType{
            public String toString(){
                return "Invalid rule type";
            }
        },

        IllegalProductName{
            public String toString(){
                return "Illegal product name. Product name is empty.";
            }
        },

        NegativePrice{
            public String toString(){
                return "The price you entered is negative";
            }
        },
        UserCannotBeNull{
            public String toString(){
                return "User cannot be null";
            }
        },
        InvalidRangeType{
            public String toString(){
                return "Invalid range type";
            }
        },
        DatabaseIsNotConnected{
            public String toString(){
                return "Database is not connected";
            }
        };
    }



