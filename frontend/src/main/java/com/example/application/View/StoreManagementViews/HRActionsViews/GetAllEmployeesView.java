package com.example.application.View.StoreManagementViews.HRActionsViews;

import com.example.application.Presenter.StoreManagementPresenters.HRActionsPresenters.GetAllEmployeesPresenter;
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

@Route("GetAllEmployeesView")
public class GetAllEmployeesView extends VerticalLayout implements HasUrlParameter<String> {
    private GetAllEmployeesPresenter presenter;
    private QueryParameters storeQuery;
    private String userID;
    private String storeID;
    private Button OKButton;

    public GetAllEmployeesView(){}

    public void buildView(){
        userID = VaadinSession.getCurrent().getAttribute("userID").toString();
        Object memberIdObj = VaadinSession.getCurrent().getAttribute("memberId");
        if (memberIdObj!=null){
            String memberId = memberIdObj.toString();
            WebSocketHandler.getInstance().addUI(memberId, UI.getCurrent());
        }
        presenter = new GetAllEmployeesPresenter(this, userID, storeID);
        makeStoreQuery();
        createTopLayout();
        H1 header = new H1("All Employees");
        VerticalLayout layout = new VerticalLayout(header);
        layout.getStyle().set("background-color", "#ffc0cb"); // Set background color to dark pink
        layout.setSpacing(false);
        layout.setAlignItems(Alignment.CENTER);
        add(layout);
        OKButton = new Button("OK", event -> {
            getUI().ifPresent(ui -> ui.navigate("StoreView", storeQuery));
        });
        createOwnersLayout();
        createManagersLayout();
        add(OKButton);
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

    public void createOwnersLayout(){
        VerticalLayout ownersLayout = new VerticalLayout();
        ownersLayout.add(new H1("Store Owners:"));
        List<String> storeOwners = presenter.getStoreOwners();
        if (storeOwners == null) {
            storeOwnersFailedToLoad();
        }
        else{
            if (storeOwners.size() == 0) {
                ownersLayout.add(new HorizontalLayout(new Text("No store owners")));
            }
            for(int i=0 ; i<storeOwners.size() ; i++){
                VerticalLayout ownerLayout = new VerticalLayout();
                ownerLayout.add(new HorizontalLayout(new Text("Store Owner - " + presenter.getEmployeeUserName(storeOwners.get(i)))));
                ownersLayout.add(ownerLayout);
            }
            add(ownersLayout);
        }
    }

    public void createManagersLayout(){
        VerticalLayout managersLayout = new VerticalLayout();
        managersLayout.add(new H1("Store Managers:"));
        List<String> storeManagers = presenter.getStoreManagers();
        if (storeManagers == null) {
            storeManagersFailedToLoad();
        }
        else{
            if (storeManagers.size() == 0) {
                managersLayout.add(new HorizontalLayout(new Text("No store owners")));
            }
            for(int i=0 ; i<storeManagers.size() ; i++){
                VerticalLayout managerLayout = new VerticalLayout();
                managerLayout.add(
                        new HorizontalLayout(new Text("Store Manager - " + presenter.getEmployeeUserName(storeManagers.get(i)))),
                        new HorizontalLayout(new Text("Inventory permissions: " + presenter.hasInventoryPermissions(storeManagers.get(i)))),
                        new HorizontalLayout(new Text("Purchase permissions: " + presenter.hasPurchasePermissions(storeManagers.get(i))))
                );
                managersLayout.add(managerLayout);
            }
            add(managersLayout);
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

    public void storeOwnersFailedToLoad(){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Store owners failed to load");
        dialog.setText("Database is not connected");
        dialog.setConfirmText("OK");
        dialog.addConfirmListener(event -> dialog.close());
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
