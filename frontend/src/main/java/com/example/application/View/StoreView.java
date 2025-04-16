package com.example.application.View;

import com.example.application.Presenter.StorePresenter;
import com.example.application.Util.ProductDTO;
import com.example.application.WebSocketUtil.WebSocketHandler;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.H1;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route("StoreView")
public class StoreView extends VerticalLayout implements HasUrlParameter<String> {
    private StorePresenter presenter;
    private QueryParameters storeQuery;
    private String userID;
    private String storeID;
    private VerticalLayout productsFoundLayout;

    public StoreView() {
        Object memberIdObj = VaadinSession.getCurrent().getAttribute("memberId");
        if (memberIdObj!=null){
            String memberId = memberIdObj.toString();
            WebSocketHandler.getInstance().addUI(memberId, UI.getCurrent());
        }
    }

    public void buildView(){
        createTopLayout();
        String welcomeText = "Welcome to " + presenter.getStoreName();
        if(!presenter.isStoreOpen()){
            welcomeText += " (Closed)";
        }
        H1 header = new H1(welcomeText);
        VerticalLayout layout = new VerticalLayout(header);
        layout.getStyle().set("background-color", "#ffc0cb"); // Set background color to dark pink
        layout.setSpacing(false);
        layout.setAlignItems(Alignment.CENTER);
        add(layout);
        createSearchLayout();
        createAllProductsLayout();
        if(presenter.isMember()){
            if(presenter.isStoreOwner()){
                createInventoryLayout();
                createPurchaseLayout();
                createHRLayout();
                createOtherActionsLayout();
            }
            else if(presenter.isStoreManager()){
                if(presenter.hasInventoryPermissions()){
                    createInventoryLayout();
                }
                if(presenter.hasPurchasePermissions()){
                    createPurchaseLayout();
                }
            }
        }
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

    public void createSearchLayout() {
        // Create the main layout
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setWidthFull();
        mainLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        mainLayout.getStyle().set("background-color", "#fff0f0"); // Set background color

        // Add the search text to the main layout
        Text searchText = new Text("Search for product:");
        HorizontalLayout searchTextLayout = new HorizontalLayout(searchText);
        searchTextLayout.setWidthFull();
        searchTextLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // Create the search fields
        TextField productNameField = new TextField("Product name");
        ComboBox<String> categoryField = new ComboBox<>("Category");
        List<String> categories = presenter.getCategories();
        if(categories == null){
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

        // Create the search and clear buttons
        Button searchButton = new Button("Search", event -> {
            presenter.onSearchButtonClicked(productNameField.getValue(), categoryField.getValue(),
                    keywordsField.getValue(), minPriceField.getValue(),
                    maxPriceField.getValue());
        });
        searchButton.getStyle().set("background-color", "#e91e63").set("color", "white");

        Button clearButton = new Button("Clear", event -> {
            this.removeAll();
            this.buildView();
        });
        clearButton.getStyle().set("background-color", "#cccccc").set("color", "white");

        productsFoundLayout = new VerticalLayout();

        // Create a horizontal layout for the search fields and buttons
        HorizontalLayout searchFieldsLayout = new HorizontalLayout(
                productNameField,
                categoryField,
                keywordsField,
                minPriceField,
                maxPriceField
        );
        searchFieldsLayout.setWidthFull();
        searchFieldsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        searchFieldsLayout.setSpacing(true);
        searchFieldsLayout.setPadding(true);

        HorizontalLayout buttonsLayout = new HorizontalLayout(searchButton, clearButton);
        buttonsLayout.setWidthFull();
        buttonsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        buttonsLayout.setSpacing(true);
        buttonsLayout.setPadding(true);

        // Add all components to the main layout
        mainLayout.add(searchTextLayout, searchFieldsLayout, buttonsLayout, productsFoundLayout);

        // Add the main layout to the root layout
        add(mainLayout);
    }



    public void createAllProductsLayout(){
        VerticalLayout allProductsLayout = new VerticalLayout();
        allProductsLayout.add(new H1("All store products:"));
        List<ProductDTO> allProducts = presenter.getAllProducts();
        if(allProducts == null){
            productsFailedToLoad();
        }
        else{
            for (int i=0 ; i<allProducts.size(); i++) {
                ProductDTO product = allProducts.get(i);
                IntegerField quantityField = new IntegerField();
                quantityField.setLabel("quantity");
                quantityField.setMin(0);
                quantityField.setMax(Math.min(10,product.getQuantity()));
                quantityField.setValue(1);
                quantityField.setStepButtonsVisible(true);
                allProductsLayout.add(
                        new HorizontalLayout(new Text("name: " + product.getName())),
                        new HorizontalLayout(new Text("description: " + product.getDescription())),
                        new HorizontalLayout(new Text("price: " + product.getPrice())),
                        quantityField,
                        new Button("Add to Cart", event -> {
                            presenter.onAddToCartButtonClicked(product.getName(), quantityField.getValue());
                        })
                );
            }
            add(allProductsLayout);
        }
    }

    public void createInventoryLayout(){
        VerticalLayout inventoryLayout = new VerticalLayout();
        inventoryLayout.add(new H1("Inventory Actions:"),
                new HorizontalLayout(
                    new Button("Add Product to Store", event -> {
                        getUI().ifPresent(ui -> ui.navigate("AddProductToStoreView", storeQuery));
                    }),
                    new Button("Remove Product from Store", event -> {
                        getUI().ifPresent(ui -> ui.navigate("RemoveProductFromStoreView", storeQuery));
                    }),
                    new Button("Update Product in Store", event -> {
                        getUI().ifPresent(ui -> ui.navigate("UpdateProductInStoreView", storeQuery));
                })
        ));
        add(inventoryLayout);
    }

    public void createPurchaseLayout(){
        VerticalLayout purchaseLayout = new VerticalLayout();
        purchaseLayout.add(new H1("Policies Actions:"),
                new HorizontalLayout(
                        new Button("Purchase Policy", event -> {
                            getUI().ifPresent(ui -> ui.navigate("PurchasePolicyView", storeQuery));
                        }),
                        new Button("Discount Policy", event -> {
                            getUI().ifPresent(ui -> ui.navigate("DiscountPolicyView", storeQuery));
                        })
                )
        );
        add(purchaseLayout);
    }

    public void createHRLayout(){
        VerticalLayout HRLayout = new VerticalLayout();
        HRLayout.add(new H1("HR Actions:"),
                new HorizontalLayout(
                        new Button("Get all Employees", event -> {
                            getUI().ifPresent(ui -> ui.navigate("GetAllEmployeesView", storeQuery));
                        }),
                        new Button("Appoint Store Owner", event -> {
                            getUI().ifPresent(ui -> ui.navigate("AppointStoreOwnerView", storeQuery));
                        }),
                        new Button("Fire Store Owner", event -> {
                            getUI().ifPresent(ui -> ui.navigate("FireStoreOwnerView", storeQuery));
                        })
                ),
                new HorizontalLayout(
                        new Button("Appoint Store Manager", event -> {
                            getUI().ifPresent(ui -> ui.navigate("AppointStoreManagerView", storeQuery));
                        }),
                        new Button("Fire Store Manager", event -> {
                            getUI().ifPresent(ui -> ui.navigate("FireStoreManagerView", storeQuery));
                        }),
                        new Button("Update store manager permissions", event -> {
                            getUI().ifPresent(ui -> ui.navigate("UpdateManagerPermissionsView", storeQuery));
                        })
                )
        );
        add(HRLayout);
    }

    public void createOtherActionsLayout(){
        VerticalLayout otherActionsLayout = new VerticalLayout();
        otherActionsLayout.add(new H1("Other Actions:"));
        HorizontalLayout actions = new HorizontalLayout();
        if(presenter.isStoreOpen()){
            actions.add(new Button("Close Store", event -> {
                closeStoreConfirm();
            }));
        }
        else{
            actions.add(new Button("Reopen Store", event -> {
                if(presenter.onReopenButtonClicked()){
                    this.removeAll();
                    buildView();
                }
            }));
        }
        otherActionsLayout.add(actions);
        add(otherActionsLayout);
    }

    public void showInStoreSearchResult(List<ProductDTO> productsFound){
        productsFoundLayout.removeAll();
        if (productsFound == null || productsFound.isEmpty()) {
            productsFoundLayout.add(new H1("No products found."));
        } else {
            productsFoundLayout.add(new H1("Search results:"));
            for (int i=0 ; i < productsFound.size() ; i++) {
                ProductDTO productDto = productsFound.get(i);
                IntegerField quantityField = new IntegerField();
                quantityField.setLabel("quantity");
                quantityField.setMin(0);
                quantityField.setMax(Math.min(10,productDto.getQuantity()));
                quantityField.setValue(1);
                quantityField.setStepButtonsVisible(true);
                productsFoundLayout.add(
                        new HorizontalLayout(new Text("name: " + productDto.getName())),
                        new HorizontalLayout(new Text("description: " + productDto.getDescription())),
                        new HorizontalLayout(new Text("price: " + productDto.getPrice())),
                        quantityField,
                        new Button("Add to Cart", event -> {
                            presenter.onAddToCartButtonClicked(productDto.getName(), quantityField.getValue());
                        })
                );
            }
        }
    }

    public void addProductCartResult(String message){
        Notification.show(message, 3000, Notification.Position.MIDDLE);
    }

    public void closeStoreConfirm(){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Close Store");
        dialog.setText("Are you sure you want to close this store?");
        dialog.setCancelable(true);
        dialog.addCancelListener(event -> dialog.close());
        dialog.setConfirmText("Yes");
        dialog.addConfirmListener(event -> {
            if(presenter.onCloseButtonClicked()){
                this.removeAll();
                buildView();
            }
        });
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

    public void storeFailedToLoad(){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Store failed to load");
        dialog.setText("Database is not connected");
        dialog.setConfirmText("OK");
        dialog.addConfirmListener(event -> getUI().ifPresent(ui -> ui.navigate("MarketView")));
        dialog.open();
    }

    public void productsFailedToLoad(){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Products failed to load");
        dialog.setText("Database is not connected");
        dialog.setConfirmText("OK");
        dialog.addConfirmListener(event -> dialog.close());
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

    public void makeStoreQuery(){
        Map<String, List<String>> parameters = new HashMap<>();
        parameters.put("storeID", List.of(storeID));
        storeQuery = new QueryParameters(parameters);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        Map<String, List<String>> parameters = beforeEvent.getLocation().getQueryParameters().getParameters();
        storeID = parameters.getOrDefault("storeID", List.of("Unknown")).get(0);
        userID = VaadinSession.getCurrent().getAttribute("userID").toString();
        presenter = new StorePresenter(this, userID, storeID);
        makeStoreQuery();
        if(presenter.getStore() != null){
            buildView();
        }
        else{
            storeFailedToLoad();
        }
    }
}
