package com.example.application.View;

import com.example.application.Presenter.MarketPresenter;
import com.example.application.Util.ProductDTO;
import com.example.application.Util.StoreDTO;
import com.example.application.WebSocketUtil.WebSocketHandler;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
//import org.springframework.messaging.simp.annotation.SubscribeMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route("MarketView")
public class MarketView extends VerticalLayout {
    private MarketPresenter presenter;
    private String userID;
    private VerticalLayout productsFoundLayout;

    public MarketView() {
        userID = VaadinSession.getCurrent().getAttribute("userID").toString();
        //String memberId;
        Object memberIdObj = VaadinSession.getCurrent().getAttribute("memberId");
        if (memberIdObj!=null){
            String memberId = memberIdObj.toString();
            WebSocketHandler.getInstance().addUI(memberId, UI.getCurrent());
        }
        buildView();
    }

    public void buildView(){
        presenter = new MarketPresenter(this, userID);
        createTopLayout();
        H1 header = new H1("Welcome to The Market Place!");
        VerticalLayout layout = new VerticalLayout(header);
        layout.getStyle().set("background-color", "#ffc0cb"); // Set background color to dark pink
        // Set spacing and alignment if needed
        layout.setSpacing(false);
        layout.setAlignItems(Alignment.CENTER);

        // Add the layout to your UI or another container
        add(layout);
        createSearchLayout();
        createAllStoresLayout();
        if(presenter.isAdmin()){
            createAdminLayout();
        }
    }

    public void createTopLayout(){
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.getStyle().set("background-color", "#fff0f0"); // Set background color
        Text helloMessage = new Text("Hello, " + presenter.getUserName());
        Button homeButton = new Button("Home", new Icon(VaadinIcon.HOME));
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

    public void createSearchLayout(){
        // Create the main layout
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setWidthFull();
        mainLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        mainLayout.getStyle().set("background-color", "#fff0f0"); // Set background color


        // Create the logo
        Image logo = new Image("/logo.png", "Logo");
        logo.setWidth("350px"); // Set appropriate size for the logo
        logo.setHeight("200px");

        // Add the logo to a centered horizontal layout
        HorizontalLayout logoLayout = new HorizontalLayout(logo);
        logoLayout.setWidthFull();
        logoLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // Add the search text to the main layout
        Text searchText = new Text("Search for product:");
        HorizontalLayout searchTextLayout = new HorizontalLayout(searchText);
        searchTextLayout.setWidthFull();
        searchTextLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // Create the search fields
        TextField productNameField = new TextField("Product name");
        ComboBox<String> categoryField = new ComboBox<>("Category");
        List<String> categories = presenter.getCategories();
        if (categories == null) {
            categoriesFailedToLoad();
        }
        else{
            categoryField.setItems(categories);
        }
        MultiSelectComboBox<String> keywordsField = new MultiSelectComboBox<>("Keywords");
        keywordsField.setItems("clothes", "shoes", "food", "optic", "electricity", "toys", "health", "sport",
                "women", "men", "children", "beauty", "travel", "gifts", "office", "coffee", "home");
        IntegerField minPriceField = new IntegerField("Minimum price");
        IntegerField maxPriceField = new IntegerField("Maximum price");
        IntegerField minStoreRatingField = new IntegerField("Minimum store rating");

        // Create the search and clear buttons
        Button searchButton = new Button("Search", event -> {
            presenter.onSearchButtonClicked(productNameField.getValue(), categoryField.getValue(),
                    keywordsField.getValue(), minPriceField.getValue(),
                    maxPriceField.getValue(), minStoreRatingField.getValue());
        });

        Button clearButton = new Button("Clear", event -> {
            this.removeAll();
            this.buildView();
        });

        productsFoundLayout = new VerticalLayout();

        // Create a horizontal layout for the search fields and buttons
        HorizontalLayout searchFieldsLayout = new HorizontalLayout(
                productNameField,
                categoryField,
                keywordsField,
                minPriceField,
                maxPriceField,
                minStoreRatingField
        );
        searchFieldsLayout.setWidthFull();
        searchFieldsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        HorizontalLayout buttonsLayout = new HorizontalLayout(searchButton, clearButton);
        buttonsLayout.setWidthFull();
        buttonsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // Add all components to the main layout
        mainLayout.add(logoLayout, searchTextLayout, searchFieldsLayout, buttonsLayout, productsFoundLayout);

        // Add the main layout to the root layout
        add(mainLayout);
    }


    public void createAllStoresLayout(){
        VerticalLayout storesLayout = new VerticalLayout();
        storesLayout.getStyle().set("background-color", "#fff0f0"); // Set background color
        storesLayout.add(new H1("All Stores:"));
        List<StoreDTO> allStoresDtos = presenter.getAllStores();
        if (allStoresDtos == null) {
            storesFailedToLoad();
        }
        else{
            HorizontalLayout storeNamesLayout = new HorizontalLayout();
            for(int i=0 ; i<allStoresDtos.size(); i++){
                StoreDTO storeDto = allStoresDtos.get(i);
                storeNamesLayout.add(new Button(storeDto.getStoreName(), event -> {
                    UI.getCurrent().access(() -> goToStore(storeDto.getStore_ID()));}));
            }
            storesLayout.add(storeNamesLayout);
            add(storesLayout);
        }
    }

    public void createAdminLayout(){
        VerticalLayout adminLayout = new VerticalLayout();
        adminLayout.getStyle().set("background-color", "#fff0f0"); // Set background color
        adminLayout.add(new H1("Administration:"));
        Button closeStoreButton = new Button("Close Store", event -> {
            getUI().ifPresent(ui -> ui.navigate("AdminCloseStoreView"));
        });
        Button externalServicesButton = new Button("External Services", event -> {
            getUI().ifPresent(ui -> ui.navigate("ExternalServicesView"));
        });
        Button marketPurchaseHistoryButton = new Button("Market Purchase History", event -> {
            getUI().ifPresent(ui -> ui.navigate("MarketPurchaseHistoryView"));
        });
        Button marketSupplyHistoryButton = new Button("Market Supply History", event -> {
            getUI().ifPresent(ui -> ui.navigate("MarketSupplyHistoryView"));
        });
        adminLayout.add(new HorizontalLayout(closeStoreButton, externalServicesButton,
                                                        marketPurchaseHistoryButton, marketSupplyHistoryButton));
        add(adminLayout);
    }

    public void goToStore(String storeID){
        if(!presenter.isStoreOpen(storeID) && !presenter.isAdmin() && !presenter.isStoreOwner(storeID)){
            Notification.show("Store is closed",3000, Notification.Position.MIDDLE);
        }
        else {
            Map<String, List<String>> parameters = new HashMap<>();
            parameters.put("storeID", List.of(storeID));
            parameters.put("userID", List.of(userID));
            QueryParameters userStoreQuery = new QueryParameters(parameters);
            getUI().ifPresent(ui -> ui.navigate("StoreView", userStoreQuery));
        }
    }

    public void showGeneralSearchResult(Map<String,List<ProductDTO>> productsFound){
        productsFoundLayout.removeAll();
        if (productsFound == null || productsFound.isEmpty()) {
            productsFoundLayout.add(new H1("No products found."));
        } else {
            productsFoundLayout.add(new H1("Search results:"));

            for (Map.Entry<String, List<ProductDTO>> entry : productsFound.entrySet()) {
                String storeId = entry.getKey(); // Assuming the key is the store ID
                String storeName = presenter.getStoreName(storeId);
                for (int i=0; i<entry.getValue().size(); i++) {
                    ProductDTO product = entry.getValue().get(i);

                    IntegerField quantityField = new IntegerField();
                    quantityField.setLabel("quantity");
                    quantityField.setMin(0);
                    quantityField.setMax(Math.min(10,product.getQuantity()));
                    quantityField.setValue(1);
                    quantityField.setStepButtonsVisible(true);

                    productsFoundLayout.add(
                            new HorizontalLayout(new Text("store: " + storeName)),
                            new HorizontalLayout(new Text("name: " + product.getName())),
                            new HorizontalLayout(new Text("description: " + product.getDescription())),
                            new HorizontalLayout(new Text("price: " + product.getPrice())),
                            quantityField,
                            new Button("Add to Cart", event -> {
                                presenter.onAddToCartButtonClicked(product, quantityField.getValue(), storeId);
                            })
                    );
                }
            }
        }
    }

    public void addProductCartResult(String message){
        Notification.show(message, 3000, Notification.Position.MIDDLE);
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

    public void categoriesFailedToLoad(){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Categories failed to load");
        dialog.setText("Database is not connected");
        dialog.setConfirmText("OK");
        dialog.addConfirmListener(event -> dialog.close());
        dialog.open();
    }

    public void storesFailedToLoad(){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Stores failed to load");
        dialog.setText("Database is not connected");
        dialog.setConfirmText("OK");
        dialog.addConfirmListener(event -> dialog.close());
        dialog.open();
    }

    //@SubscribeMapping("/topic/notifications")
    public void handleNotification(String notificationMessage) {
        UI.getCurrent().access(() -> {
            Notification.show(notificationMessage);
        });
    }

    public void logout(){
        this.removeAll();
        buildView();
    }
}