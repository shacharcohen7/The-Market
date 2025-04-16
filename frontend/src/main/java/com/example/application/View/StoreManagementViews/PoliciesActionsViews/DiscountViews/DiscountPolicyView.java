package com.example.application.View.StoreManagementViews.PoliciesActionsViews.DiscountViews;

import com.example.application.Presenter.StoreManagementPresenters.PoliciesActionsPresenters.DiscountPresenters.DiscountPolicyPresenter;
import com.example.application.WebSocketUtil.WebSocketHandler;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route("DiscountPolicyView")
public class DiscountPolicyView extends VerticalLayout implements HasUrlParameter<String> {
    private DiscountPolicyPresenter presenter;
    private QueryParameters storeQuery;
    private String userID;
    private String storeID;
    private Button BackButton;
    private Button AddSimpleRuleButton;
    private Button AddCondRuleButton;
    private Button ComposeSimpleRuleButton;
    private Button ComposeCondRuleButton;
    private Button RemoveRuleButton;

    public DiscountPolicyView(){}

    public void buildView(){
        userID = VaadinSession.getCurrent().getAttribute("userID").toString();
        Object memberIdObj = VaadinSession.getCurrent().getAttribute("memberId");
        if (memberIdObj!=null){
            String memberId = memberIdObj.toString();
            WebSocketHandler.getInstance().addUI(memberId, UI.getCurrent());
        }
        presenter = new DiscountPolicyPresenter(this, userID, storeID);
        makeStoreQuery();
        createTopLayout();
        H1 header = new H1("Store Discount Policy");
        VerticalLayout layout = new VerticalLayout(header);
        layout.getStyle().set("background-color", "#ffc0cb"); // Set background color to dark pink
        layout.setSpacing(false);
        layout.setAlignItems(Alignment.CENTER);
        add(layout);
        AddSimpleRuleButton = new Button("Add Simple Discount", event -> {
            getUI().ifPresent(ui -> ui.navigate("AddSimpleDiscountView", storeQuery));
        });
        AddCondRuleButton = new Button("Add Conditional Discount", event -> {
            getUI().ifPresent(ui -> ui.navigate("AddCondDiscountView", storeQuery));
        });
        ComposeSimpleRuleButton = new Button("Compose Simple Discount", event -> {
            getUI().ifPresent(ui -> ui.navigate("ComposeSimpleDiscountView", storeQuery));
        });
        ComposeCondRuleButton = new Button("Compose Conditional Discount", event -> {
            getUI().ifPresent(ui -> ui.navigate("ComposeCondDiscountView", storeQuery));
        });
        RemoveRuleButton = new Button("Remove Discount", event -> {
            getUI().ifPresent(ui -> ui.navigate("RemoveDiscountView", storeQuery));
        });
        BackButton = new Button("Back To Store", event -> {
            getUI().ifPresent(ui -> ui.navigate("StoreView", storeQuery));
        });
        createDiscountLayout();
        add(new HorizontalLayout(AddSimpleRuleButton, AddCondRuleButton, ComposeSimpleRuleButton,
                ComposeCondRuleButton, RemoveRuleButton, BackButton));
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

    public void createDiscountLayout(){
        List<String> discountRules = presenter.getStoreCurrentDiscountRules();
        if (discountRules == null) {
            rulesFailedToLoad();
        }
        else {
            if (discountRules.size() == 0) {
                add(new HorizontalLayout(new Text("No discount rules")));
            }
            for (int i = 0; i < discountRules.size(); i++) {
                add(new HorizontalLayout(new Text(i + 1 + ". " + discountRules.get(i))));
            }
        }
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

    public void rulesFailedToLoad(){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Rules failed to load");
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
