package com.example.application.View.StoreManagementViews.PoliciesActionsViews.PurchaseViews;

import com.example.application.Presenter.StoreManagementPresenters.PoliciesActionsPresenters.PurchasePresenters.AddPurchaseRulePresenter;
import com.example.application.Util.TestRuleDTO;
import com.example.application.WebSocketUtil.WebSocketHandler;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;

import java.util.*;

@Route("AddPurchaseRuleView")
public class AddPurchaseRuleView extends VerticalLayout implements HasUrlParameter<String> {
    private AddPurchaseRulePresenter presenter;
    private QueryParameters storeQuery;
    private String userID;
    private String storeID;
    private VerticalLayout rulesLayout;
    private List<TestRuleDTO> Rules;
    private List<String> logicOperators;
    private Button addButton;
    private Button cancelButton;

    public AddPurchaseRuleView(){}

    public void buildView(){
        userID = VaadinSession.getCurrent().getAttribute("userID").toString();
        Object memberIdObj = VaadinSession.getCurrent().getAttribute("memberId");
        if (memberIdObj!=null){
            String memberId = memberIdObj.toString();
            WebSocketHandler.getInstance().addUI(memberId, UI.getCurrent());
        }
        presenter = new AddPurchaseRulePresenter(this, userID, storeID);
        makeStoreQuery();
        createTopLayout();
        H1 header = new H1("Add Purchase Rule");
        VerticalLayout layout = new VerticalLayout(header);
        layout.getStyle().set("background-color", "#ffc0cb"); // Set background color to dark pink
        layout.setSpacing(false);
        layout.setAlignItems(Alignment.CENTER);
        add(layout);
        Rules = new LinkedList<TestRuleDTO>();
        logicOperators = new LinkedList<String>();
        rulesLayout = new VerticalLayout();
        HorizontalLayout ruleLayout = new HorizontalLayout();
        HorizontalLayout relevantFieldsLayout = new HorizontalLayout();
        HorizontalLayout buttons = new HorizontalLayout();
        ComboBox<String> ruleTypes = new ComboBox<String>("rule type *");
        ruleTypes.setItems("Age", "Amount", "Date", "Price", "Time");
        ruleTypes.addValueChangeListener(event -> {
            createRelevantFields(ruleTypes, buttons, relevantFieldsLayout);
        });
        ruleLayout.add(ruleTypes, relevantFieldsLayout);
        rulesLayout.add(ruleLayout);
        addButton = new Button("Add", event -> {
            presenter.onAddButtonClicked(Rules, logicOperators);
        });
        cancelButton = new Button("Cancel", event -> {
            getUI().ifPresent(ui -> ui.navigate("PurchasePolicyView", storeQuery));
        });
        add(
                rulesLayout,
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

    public void createRelevantFields(ComboBox<String> ruleTypes, HorizontalLayout buttons, HorizontalLayout relevantFieldsLayout){
        relevantFieldsLayout.removeAll();
        buttons.removeAll();
        ComboBox<String> rangeField = new ComboBox<String>("range *");
        rangeField.setItems("Exact", "Below", "Above");
        ComboBox<String> categoryField = new ComboBox<String>("category");
        List<String> categories = presenter.getCategories();
        if (categories == null) {
            categoriesFailedToLoad();
        }
        else {
            categoryField.setItems(categories);
        }
        ComboBox<String> productNameField = new ComboBox<String>("product name");
        List<String> productNames = presenter.getAllProductNames();
        if (productNames == null) {
            productsFailedToLoad();
        }
        else {
            productNameField.setItems(productNames);
        }
        categoryField.addValueChangeListener(event -> {
            if(!categoryField.isEmpty() & !productNameField.isEmpty()) {
                Notification.show("Impossible to fill both category and product name fields",3000, Notification.Position.MIDDLE);
                categoryField.clear();
            }
        });
        productNameField.addValueChangeListener(event -> {
            if(!categoryField.isEmpty() & !productNameField.isEmpty()) {
                Notification.show("Impossible to fill both category and product name fields", 3000, Notification.Position.MIDDLE);
                productNameField.clear();
            }
        });
        TextField descriptionField = new TextField("description *");
        ComboBox<String> containsField = new ComboBox<String>("contains");
        containsField.setItems("basket must contain", "basket must not contain");
        relevantFieldsLayout.add(rangeField, categoryField, productNameField, descriptionField, containsField);
        IntegerField ageField = new IntegerField("age *");
        IntegerField quantityField = new IntegerField("quantity *");
        DatePicker dateField = new DatePicker("date *");
        IntegerField priceField = new IntegerField("price *");
        TimePicker timeField = new TimePicker("time *");
        if(ruleTypes.getValue().equals("Age")){
            relevantFieldsLayout.add(ageField);
        }
        if(ruleTypes.getValue().equals("Amount")){
            relevantFieldsLayout.add(quantityField);
        }
        if(ruleTypes.getValue().equals("Date")){
            relevantFieldsLayout.add(dateField);
        }
        if(ruleTypes.getValue().equals("Price")){
            relevantFieldsLayout.add(priceField);
        }
        if(ruleTypes.getValue().equals("Time")){
            relevantFieldsLayout.add(timeField);
        }
        Button applyButton = new Button("apply", event -> {
//            String ruleTypeVal = ruleTypes==null ? null: ruleTypes.getValue();
//            String rangeFieldVal = rangeField ==null ? null : rangeField.getValue();
//            String categoryFieldVal = categoryField ==null ? null :  categoryField.getValue();
//            String productNameFieldVal = productNameField ==null ? null :  productNameField.getValue();
//            String descriptionFieldVal = descriptionField==null ? null : descriptionField.getValue();
//            Boolean containsFieldVal = containsField==null ? null : containsField.getValue();
//            apply(ruleTypes, rangeField, categoryField,
//                    productNameField, descriptionField, containsField, ageField,
//                    quantityField, dateField, priceField, timeField, buttons, event.getSource());
            if(ruleTypes.isEmpty()){
                Notification.show("Please fill ruleType field",3000, Notification.Position.MIDDLE);
            }
            else if(rangeField.isEmpty()){
                Notification.show("Please fill range field",3000, Notification.Position.MIDDLE);
            }
            else if(descriptionField.isEmpty()){
                Notification.show("Please fill description field",3000, Notification.Position.MIDDLE);
            }
            else if(categoryField.isEmpty() && productNameField.isEmpty() && !containsField.isEmpty()){
                Notification.show("Please fill category or productName field",3000, Notification.Position.MIDDLE);
            }
            else if((!categoryField.isEmpty() || !productNameField.isEmpty()) && containsField.isEmpty()){
                Notification.show("Please fill contains field",3000, Notification.Position.MIDDLE);
            }
            else if(ruleTypes.getValue() == "Age" && ageField.isEmpty()){
                Notification.show("Please fill age field",3000, Notification.Position.MIDDLE);
            }
            else if(ruleTypes.getValue() == "Amount" && quantityField.isEmpty()){
                Notification.show("Please fill quantity field",3000, Notification.Position.MIDDLE);
            }
            else if(ruleTypes.getValue() == "Amount" && categoryField.isEmpty() && productNameField.isEmpty()){
                Notification.show("Please fill category or productName field",3000, Notification.Position.MIDDLE);
            }
            else if(ruleTypes.getValue() == "Date" && dateField.isEmpty()){
                Notification.show("Please fill date field",3000, Notification.Position.MIDDLE);
            }
            else if(ruleTypes.getValue() == "Price" && priceField.isEmpty()){
                Notification.show("Please fill price field",3000, Notification.Position.MIDDLE);
            }
            else if(ruleTypes.getValue() == "Time" && timeField.isEmpty()){
                Notification.show("Please fill time field",3000, Notification.Position.MIDDLE);
            }
            else {
                Boolean contain = null;
                if(containsField.getValue() != null) {
                    if (containsField.getValue().equals("basket must contain")) {
                        contain = true;
                    } else if (containsField.getValue().equals("basket must not contain")) {
                        contain = false;
                    }
                }
                TestRuleDTO newRule = new TestRuleDTO(ruleTypes.getValue(), rangeField.getValue(), categoryField.getValue(),
                        productNameField.getValue(), descriptionField.getValue(), contain, ageField.getValue(),
                        quantityField.getValue(), dateField.getValue(), priceField.getValue(), timeField.getValue());
                Rules.add(newRule);
                ruleTypes.setEnabled(false);
                rangeField.setEnabled(false);
                categoryField.setEnabled(false);
                productNameField.setEnabled(false);
                descriptionField.setEnabled(false);
                containsField.setEnabled(false);
                ageField.setEnabled(false);
                quantityField.setEnabled(false);
                dateField.setEnabled(false);
                priceField.setEnabled(false);
                timeField.setEnabled(false);
                Button extendButton = new Button("extend", buttonClickEvent -> extend());
                extendButton.setDisableOnClick(true);
                buttons.add(extendButton);
                event.getSource().setEnabled(false);
            }
        });
        buttons.add(applyButton);
        rulesLayout.add(buttons);
    }

    public void extend(){
        HorizontalLayout ruleLayout = new HorizontalLayout();
        HorizontalLayout relevantFieldsLayout = new HorizontalLayout();
        HorizontalLayout buttons = new HorizontalLayout();
        ComboBox<String> operator = new ComboBox<String>("operator");
        operator.setItems("ONLY IF", "OR", "AND");
        operator.addValueChangeListener(event -> {
            logicOperators.add(event.getValue());
            operator.setEnabled(false);
        });
        rulesLayout.add(operator);
        ComboBox<String> ruleTypes = new ComboBox<String>("rule type");
        ruleTypes.setItems("Age", "Amount", "Date", "Price", "Time");
        ruleTypes.addValueChangeListener(event -> {
            createRelevantFields(ruleTypes, buttons, relevantFieldsLayout);
        });
        ruleLayout.add(ruleTypes, relevantFieldsLayout);
        rulesLayout.add(ruleLayout);
    }

    public void addSuccess(String message) {
        Notification.show(message, 3000, Notification.Position.MIDDLE);
        getUI().ifPresent(ui -> ui.navigate("PurchasePolicyView", storeQuery));
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

    public void addFailure(String message) {
        Notification.show(message, 3000, Notification.Position.MIDDLE);
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
