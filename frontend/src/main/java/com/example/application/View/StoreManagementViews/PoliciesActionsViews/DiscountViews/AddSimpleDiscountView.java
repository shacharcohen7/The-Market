package com.example.application.View.StoreManagementViews.PoliciesActionsViews.DiscountViews;

import com.example.application.Presenter.StoreManagementPresenters.PoliciesActionsPresenters.DiscountPresenters.AddSimpleDiscountPresenter;
import com.example.application.Util.DiscountValueDTO;
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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;

import java.util.*;

@Route("AddSimpleDiscountView")
public class AddSimpleDiscountView extends VerticalLayout implements HasUrlParameter<String> {
    private AddSimpleDiscountPresenter presenter;
    private QueryParameters storeQuery;
    private String userID;
    private String storeID;
    private VerticalLayout discountsLayout;
    private List<DiscountValueDTO> discs;
    private List<String> numericOperators;
    private Button addButton;
    private Button cancelButton;

    public AddSimpleDiscountView(){}

    public void buildView(){
        userID = VaadinSession.getCurrent().getAttribute("userID").toString();
        Object memberIdObj = VaadinSession.getCurrent().getAttribute("memberId");
        if (memberIdObj!=null){
            String memberId = memberIdObj.toString();
            WebSocketHandler.getInstance().addUI(memberId, UI.getCurrent());
        }
        presenter = new AddSimpleDiscountPresenter(this, userID, storeID);
        makeStoreQuery();
        createTopLayout();
        H1 header = new H1("Add Simple Discount");
        VerticalLayout layout = new VerticalLayout(header);
        layout.getStyle().set("background-color", "#ffc0cb"); // Set background color to dark pink
        layout.setSpacing(false);
        layout.setAlignItems(Alignment.CENTER);
        add(layout);
        discs = new LinkedList<DiscountValueDTO>();
        numericOperators = new LinkedList<String>();
        discountsLayout = new VerticalLayout();
        HorizontalLayout discountLayout = new HorizontalLayout();
        HorizontalLayout relevantFieldsLayout = new HorizontalLayout();
        HorizontalLayout buttons = new HorizontalLayout();
        ComboBox<String> discountTypes = new ComboBox<String>("discount type *");
        discountTypes.setItems("Store Discount", "Products Discount", "Category Discount");
        discountTypes.addValueChangeListener(event -> {
            createRelevantFields(discountTypes, buttons, relevantFieldsLayout);
        });
        discountLayout.add(discountTypes, relevantFieldsLayout);
        discountsLayout.add(discountLayout);
        addButton = new Button("Add", event -> {
            presenter.onAddButtonClicked(discs, numericOperators);
        });
        cancelButton = new Button("Cancel", event -> {
            getUI().ifPresent(ui -> ui.navigate("DiscountPolicyView", storeQuery));
        });
        add(
                discountsLayout,
                new HorizontalLayout(addButton, cancelButton)
        );
    }

    public void createTopLayout(){
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.getStyle().set("background-color", "#fff0f0"); // Set background color
        Text helloMessage = new Text("Hello, " + presenter.getUserName());
        Button homeButton = new Button("Home", new Icon(VaadinIcon.HOME), event -> getUI().ifPresent(ui -> ui.navigate("MarketView")));
        Button shoppingCartButton = new Button("Shopping Cart", new Icon(VaadinIcon.CART),
                event -> getUI().ifPresent(ui -> ui.navigate("ShoppingCartView")));

        topLayout.add(helloMessage, homeButton, shoppingCartButton);
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

        add(topLayout);
    }

    public void createRelevantFields(ComboBox<String> discountTypes, HorizontalLayout buttons, HorizontalLayout relevantFieldsLayout){
        relevantFieldsLayout.removeAll();
        buttons.removeAll();
        IntegerField percentageField = new IntegerField("percentage *");
        ComboBox<String> categoryField = new ComboBox<String>("category *");
        List<String> categories = presenter.getCategories();
        if (categories == null) {
            categoriesFailedToLoad();
        }
        else {
            categoryField.setItems(categories);
        }
        MultiSelectComboBox<String> productsField = new MultiSelectComboBox<String>("product *");
        List<String> productNames = presenter.getAllProductNames();
        if (productNames == null) {
            productsFailedToLoad();
        }
        else {
            productsField.setItems(productNames);
        }
        relevantFieldsLayout.add(percentageField);
        if(discountTypes.getValue().equals("Products Discount")){
            relevantFieldsLayout.add(productsField);
        }
        if(discountTypes.getValue().equals("Category Discount")){
            relevantFieldsLayout.add(categoryField);
        }
        Button applyButton = new Button("apply", event -> {
            if(discountTypes.isEmpty()){
                Notification.show("Please fill discountType field",3000, Notification.Position.MIDDLE);
            }
            else if(percentageField.isEmpty()){
                Notification.show("Please fill percentage field",3000, Notification.Position.MIDDLE);
            }
            else if(discountTypes.getValue() == "Products Discount" && productsField.isEmpty()){
                Notification.show("Please fill product field",3000, Notification.Position.MIDDLE);
            }
            else if(discountTypes.getValue() == "Category Discount" && categoryField.isEmpty()){
                Notification.show("Please fill category field",3000, Notification.Position.MIDDLE);
            }
            else {
                boolean isStoreDiscount = discountTypes.getValue().equals("Store Discount");
                List<String> productsList = null;
                if(discountTypes.getValue().equals("Products Discount")){
                    productsList = new ArrayList<>(productsField.getValue());
                }
                DiscountValueDTO newDiscount = new DiscountValueDTO(percentageField.getValue(), categoryField.getValue(),
                        isStoreDiscount, productsList);
                discs.add(newDiscount);
                discountTypes.setEnabled(false);
                percentageField.setEnabled(false);
                categoryField.setEnabled(false);
                productsField.setEnabled(false);
                Button extendButton = new Button("extend", buttonClickEvent -> extend());
                extendButton.setDisableOnClick(true);
                buttons.add(extendButton);
                event.getSource().setEnabled(false);
            }
        });
        buttons.add(applyButton);
        discountsLayout.add(buttons);
    }

    public void extend(){
        HorizontalLayout discountLayout = new HorizontalLayout();
        HorizontalLayout relevantFieldsLayout = new HorizontalLayout();
        HorizontalLayout buttons = new HorizontalLayout();
        ComboBox<String> operator = new ComboBox<String>("operator");
        operator.setItems("MAX", "ADDITION");
        operator.addValueChangeListener(event -> {
            numericOperators.add(event.getValue());
            operator.setEnabled(false);
        });
        discountsLayout.add(operator);
        ComboBox<String> discountTypes = new ComboBox<String>("discount type");
        discountTypes.setItems("Store Discount", "Products Discount", "Category Discount");
        discountTypes.addValueChangeListener(event -> {
            createRelevantFields(discountTypes, buttons, relevantFieldsLayout);
        });
        discountLayout.add(discountTypes, relevantFieldsLayout);
        discountsLayout.add(discountLayout);
    }

    public void addSuccess(String message) {
        Notification.show(message, 3000, Notification.Position.MIDDLE);
        getUI().ifPresent(ui -> ui.navigate("DiscountPolicyView", storeQuery));
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

    public void addFailure(String message) {
        Notification.show(message, 3000, Notification.Position.MIDDLE);
    }

    public void categoriesFailedToLoad(){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Categories failed to load");
        dialog.setText("Database is not connected");
        dialog.setConfirmText("OK");
        dialog.addConfirmListener(event -> dialog.close());
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

    public void makeStoreQuery(){
        Map<String, List<String>> parameters = new HashMap<>();
        parameters.put("storeID", List.of(storeID));
        storeQuery = new QueryParameters(parameters);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        Map<String, List<String>> parameters = beforeEvent.getLocation().getQueryParameters().getParameters();
        storeID = parameters.getOrDefault("storeID", List.of("Unknown")).get(0);
        buildView();
    }
}
