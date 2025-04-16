package com.example.application.View;

import com.example.application.Presenter.ShoppingCartPresenter;
import com.example.application.Util.CartDTO;
import com.example.application.Util.ProductDTO;
import com.example.application.Util.UserDTO;
import com.example.application.WebSocketUtil.WebSocketHandler;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;
import java.util.Map;

@Route("ShoppingCartView")
public class ShoppingCartView extends VerticalLayout {
    private ShoppingCartPresenter presenter;
    private String userID;
    private VerticalLayout cartLayout;
    private VerticalLayout paymentLayout;

    public ShoppingCartView(){
        userID = VaadinSession.getCurrent().getAttribute("userID").toString();
        Object memberIdObj = VaadinSession.getCurrent().getAttribute("memberId");
        if (memberIdObj!=null){
            String memberId = memberIdObj.toString();
            WebSocketHandler.getInstance().addUI(memberId, UI.getCurrent());
        }
        this.presenter = new ShoppingCartPresenter(this, userID);
        if (presenter.getCart() == null) {
            cartFailedToLoad();
        }
        else{
            buildView();
        }
    }

    public void buildView(){
        createTopLayout();
        H1 header = new H1("Shopping Cart");
        VerticalLayout layout = new VerticalLayout(header);
        layout.getStyle().set("background-color", "#ffc0cb"); // Set background color to dark pink
        // Set spacing and alignment if needed
        layout.setSpacing(false);
        layout.setAlignItems(Alignment.CENTER);

        // Add the layout to your UI or another container
        add(layout);
        cartLayout = new VerticalLayout();
        createCartProductsLayout();
        createSummaryLayout();
    }

    public void createTopLayout(){
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.getStyle().set("background-color", "#fff0f0"); // Set background color
        Text helloMessage = new Text("Hello, " + presenter.getUserName());
        Button homeButton = new Button("Home", new Icon(VaadinIcon.HOME), event -> getUI().ifPresent(ui -> ui.navigate("MarketView")));
        Button shoppingCartButton = new Button("Shopping Cart", new Icon(VaadinIcon.CART),
                event -> getUI().ifPresent(ui -> ui.navigate("ShoppingCartView")));

        topLayout.add(helloMessage, homeButton, shoppingCartButton);
        if(!presenter.isMember()){
            Button loginButton = new Button("Log In", event -> {
                getUI().ifPresent(ui -> ui.navigate("LoginView"));
            });
            Button signInButton = new Button("Sign In", event -> {
                getUI().ifPresent(ui -> ui.navigate("SignInView"));
            });
            topLayout.add(loginButton, signInButton);
        }
        else{
            Button openStoreButton = new Button("Open new Store", event -> {
                getUI().ifPresent(ui -> ui.navigate("OpenStoreView"));
            });
            Button purchaseHistoryButton = new Button("Purchase History", event -> {
                getUI().ifPresent(ui -> ui.navigate("PurchaseHistoryView"));
            });
            Button supplyHistoryButton = new Button("Supply History", event -> {
                getUI().ifPresent(ui -> ui.navigate("SupplyHistoryView"));
            });
            Button myProfileButton = new Button("My Profile", event -> {
                getUI().ifPresent(ui -> ui.navigate("MyProfileView"));
            });
            Button jobProposalsButton = new Button("Job Proposals", event -> {
                getUI().ifPresent(ui -> ui.navigate("JobProposalsView"));
            });
            Button notificationsButton = new Button("Notifications", event -> {
                getUI().ifPresent(ui -> ui.navigate("NotificationsView"));
            });
            Button logoutButton = new Button("Log Out", event -> {
                logoutConfirm();
            });
            topLayout.add(openStoreButton, purchaseHistoryButton, supplyHistoryButton, myProfileButton, jobProposalsButton, notificationsButton, logoutButton);

        }
        add(topLayout);
    }

    public void createCartProductsLayout() {
        Map<String, Map<String, List<Integer>>> storeToProductsCart = presenter.getCart().getStoreToProducts();
        for (String storeID : storeToProductsCart.keySet()) {
            VerticalLayout basketLayout = new VerticalLayout();
            // Set background color and border style using CSS
            basketLayout.getStyle()
                    .set("background-color", "#fff0f0") // Light pink background color
                    .set("border", "2px solid #e91e63") // Pink border with 1px width
                    .set("border-radius", "10px") // Rounded corners
                    .set("padding", "20px") // Padding inside the basket
                    .set("margin", "20px") // Margin between baskets
                    .set("box-shadow", "2px 2px 12px rgba(0, 0, 0, 0.1)"); // Subtle shadow for depth

            basketLayout.setAlignItems(Alignment.CENTER);
            basketLayout.setJustifyContentMode(JustifyContentMode.CENTER);

            H1 storeNameHeader = new H1(presenter.getStoreName(storeID));
            storeNameHeader.getStyle()
                    .set("color", "#e91e63") // Pink color for store name
                    .set("margin-bottom", "20px"); // Margin below the store name
            basketLayout.add(storeNameHeader);

            Map<String, List<Integer>> basket = storeToProductsCart.get(storeID);
            HorizontalLayout productRow = new HorizontalLayout();
            productRow.setWidthFull();
            productRow.setSpacing(true);
            basketLayout.add(productRow);

            int productsPerRow = 3;
            int currentColumn = 0;
            HorizontalLayout currentRow = new HorizontalLayout();
            currentRow.setSpacing(true);

            for (String productName : basket.keySet()) {
                if (currentColumn >= productsPerRow) {
                    productRow.add(currentRow);
                    currentRow = new HorizontalLayout();
                    currentRow.setSpacing(true);
                    currentColumn = 0;
                }

                ProductDTO product = presenter.getProduct(productName, storeID);
                if(product == null){
                    productFailedToLoad();
                }
                else {
                    VerticalLayout productLayout = new VerticalLayout();
                    productLayout.getStyle()
                            .set("border", "1px solid #ccc") // Light gray border around each product block
                            .set("padding", "10px") // Padding inside the product block
                            .set("border-radius", "10px") // Rounded corners
                            .set("box-shadow", "1px 1px 6px rgba(0, 0, 0, 0.1)"); // Subtle shadow for depth

                    Span productNameSpan = new Span("Name: " + productName);
                    productNameSpan.getStyle()
                            .set("font-weight", "bold") // Bold font for product name
                            .set("color", "#333"); // Darker color for readability

                    Span productDescriptionSpan = new Span("Description: " + product.getDescription());
                    productDescriptionSpan.getStyle()
                            .set("color", "#666"); // Medium color for description

                    Span productPriceSpan = new Span("Price: " + basket.get(productName).get(1));
                    productPriceSpan.getStyle()
                            .set("color", "#e91e63") // Pink color for price
                            .set("font-weight", "bold"); // Bold font for price

                    IntegerField quantityField = new IntegerField();
                    quantityField.setLabel("Quantity");
                    quantityField.setMin(0);
                    quantityField.setMax(Math.min(10, product.getQuantity()));
                    quantityField.setValue(basket.get(productName).get(0));
                    quantityField.setStepButtonsVisible(true);

                    Button modifyButton = new Button("Modify Quantity", event -> presenter.modifyProductCart(productName, quantityField.getValue(), storeID, userID));
                    modifyButton.getStyle().set("background-color", "#e91e63").set("color", "white");

                    Button removeButton = new Button("Remove from Cart", event -> presenter.removeProductCart(productName, storeID, userID));
                    removeButton.getStyle().set("background-color", "#e91e63").set("color", "white");

                    productLayout.add(productNameSpan, productDescriptionSpan, productPriceSpan, quantityField, modifyButton, removeButton);
                    currentRow.add(productLayout);
                    currentColumn++;
                }
            }
            // Add the last row if it contains any products
            if (currentColumn > 0) {
                productRow.add(currentRow);
            }
            cartLayout.add(basketLayout);
        }
        add(cartLayout);
    }



    public void createSummaryLayout(){
        VerticalLayout summaryLayout = new VerticalLayout();
        summaryLayout.getStyle().set("background-color", "#fff0f0"); // Set background color
        summaryLayout.add(new H1("Total price: " + presenter.getTotalPrice()));
        paymentLayout = new VerticalLayout();
        paymentLayout = new VerticalLayout();
        Button continueToPaymentButton = new Button("Continue to Payment", event -> {
            showUserDetailsDialog();
        });
        paymentLayout.add(continueToPaymentButton);
        summaryLayout.add(paymentLayout);
        add(summaryLayout);
    }

    public void paymentFailure(String message) {
        Notification.show(message, 3000, Notification.Position.MIDDLE);
    }

    public void paymentSuccess(String message) {
        Notification.show(message, 3000, Notification.Position.MIDDLE);
        getUI().ifPresent(ui -> ui.navigate("MarketView"));
    }

    public void removeProductCartResult(String message){
        Notification.show(message, 3000, Notification.Position.MIDDLE);
        this.removeAll();
        buildView();
    }

    public void modifyProductCartResult(String message){
        Notification.show(message, 3000, Notification.Position.MIDDLE);
        this.removeAll();
        buildView();
    }

    private void showUserDetailsDialog() {
        ConfirmDialog dialog = new ConfirmDialog();
        FormLayout formLayout = new FormLayout();
        UserDTO userDTO = presenter.getUser();


        TextField countryField = new TextField("Country");
        countryField.setValue(userDTO.getCountry() != null ? userDTO.getCountry() : "");
        TextField cityField = new TextField("City");
        cityField.setValue(userDTO.getCity() != null ? userDTO.getCity() : "");
        TextField addressField = new TextField("Address");
        addressField.setValue(userDTO.getAddress() != null ? userDTO.getAddress() : "");
        TextField dateOfBirthField = new TextField("Date of Birth (YYYY-MM-DD)");
        dateOfBirthField.setValue(userDTO.getBirthday() != null ? userDTO.getBirthday() : "");
        TextField nameField = new TextField("Name");
        nameField.setValue(userDTO.getName() != null ? userDTO.getName() : "");


//        if (presenter.isMember()) {
//            if (userDTO != null) {
//                countryField.setValue(userDTO.getCountry());
//                cityField.setValue(userDTO.getCity());
//                addressField.setValue(userDTO.getAddress());
//                dateOfBirthField.setValue(userDTO.getBirthday());
//                nameField.setValue(userDTO.getName());
//            }
//        }

        formLayout.add(countryField, cityField, addressField, dateOfBirthField, nameField);

        dialog.setCancelable(true);
        dialog.addCancelListener(event -> dialog.close());
        dialog.setConfirmText("Confirm");
        dialog.addConfirmListener(event -> {
            String country = countryField.getValue();
            String city = cityField.getValue();
            String address = addressField.getValue();
            String dateOfBirth = dateOfBirthField.getValue();
            String name = nameField.getValue();

            UserDTO userDTO1 = new UserDTO(userID, presenter.getUserName(), dateOfBirth, country, city, address, name);

            presenter.validationCart(userID,userDTO1);

            dialog.close();
        });
        dialog.add(formLayout);
        dialog.open();
    }

    public void succssesCartValidation(CartDTO cartDTO){
        VaadinSession.getCurrent().setAttribute("cartDTO", cartDTO);
        getUI().ifPresent(ui -> ui.navigate("FinalShoppingCartView"));
    }

    public void failCartValidation(String message){
        Notification.show(message, 3000, Notification.Position.MIDDLE);
    }

    public void cartFailedToLoad(){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Cart failed to load");
        dialog.setText("Database is not connected");
        dialog.setConfirmText("OK");
        dialog.addConfirmListener(event -> getUI().ifPresent(ui -> ui.navigate("MarketView")));
        dialog.open();
    }

    public void productFailedToLoad(){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Product failed to load");
        dialog.setText("Database is not connected");
        dialog.setConfirmText("OK");
        dialog.addConfirmListener(event -> dialog.close());
        dialog.open();
    }

    public void logoutConfirm(){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Logout");
        dialog.setText("Are you sure you want to log out?");
        dialog.setCancelable(true);
        dialog.addCancelListener(event -> dialog.close());
        dialog.setConfirmText("Yes");
        dialog.addConfirmListener(event -> presenter.logOut());
        dialog.open();
    }

    public void logout(){
        getUI().ifPresent(ui -> ui.navigate("MarketView"));
    }
}
