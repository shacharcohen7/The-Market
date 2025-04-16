package com.example.application.View.StoreManagementViews.HRActionsViews;

import com.example.application.Presenter.StoreManagementPresenters.HRActionsPresenters.FireStoreManagerPresenter;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route("FireStoreManagerView")
public class FireStoreManagerView  extends VerticalLayout implements HasUrlParameter<String> {
    private FireStoreManagerPresenter presenter;
    private QueryParameters storeQuery;
    private String userID;
    private String storeID;
    private ComboBox<String> storeManagerField;
    private Button fireButton;
    private Button cancelButton;

    public FireStoreManagerView(){}

    public void buildView(){
        userID = VaadinSession.getCurrent().getAttribute("userID").toString();
        presenter = new FireStoreManagerPresenter(this, userID, storeID);
        makeStoreQuery();
        createTopLayout();
        H1 header = new H1("Fire Store Manager");
        VerticalLayout layout = new VerticalLayout(header);
        layout.getStyle().set("background-color", "#ffc0cb"); // Set background color to dark pink
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        add(layout);
        storeManagerField = new ComboBox<String>("store manager");
        List<String> storeManagers = presenter.getStoreManagers();
        if (storeManagers == null) {
            storeManagersFailedToLoad();
        }
        else {
            storeManagerField.setItems(storeManagers);
        }
        fireButton = new Button("Fire", event -> {
            fireConfirm();
        });
        cancelButton = new Button("Cancel", event -> {
            getUI().ifPresent(ui -> ui.navigate("StoreView", storeQuery));
        });
        add(
                storeManagerField,
                new HorizontalLayout(fireButton, cancelButton)
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

    public void fireConfirm(){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Fire Store Manager");
        dialog.setText("Are you sure you want to fire this store manager?");
        dialog.setCancelable(true);
        dialog.addCancelListener(event -> dialog.close());
        dialog.setConfirmText("Yes");
        dialog.addConfirmListener(event -> presenter.onFireButtonClicked(storeManagerField.getValue()));
        dialog.open();
    }

    public void storeManagersFailedToLoad(){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Store managers failed to load");
        dialog.setText("Database is not connected");
        dialog.setConfirmText("OK");
        dialog.addConfirmListener(event -> dialog.close());
        dialog.open();
    }

    public void fireSuccess(String message) {
        Notification.show(message, 3000, Notification.Position.MIDDLE);
        getUI().ifPresent(ui -> ui.navigate("StoreView", storeQuery));
    }

    public void fireFailure(String message) {
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
