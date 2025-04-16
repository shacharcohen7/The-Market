package com.example.application.View.AdminViews.ExternalServicesViews;

import com.example.application.Presenter.AdminPresenters.ExternalServicesPresenters.ExternalServicesPresenter;
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

import java.util.List;

@Route("ExternalServicesView")
public class ExternalServicesView extends VerticalLayout {
    private ExternalServicesPresenter presenter;
    private String userID;

    public ExternalServicesView(){
        userID = VaadinSession.getCurrent().getAttribute("userID").toString();
        Object memberIdObj = VaadinSession.getCurrent().getAttribute("memberId");
        if (memberIdObj!=null){
            String memberId = memberIdObj.toString();
            WebSocketHandler.getInstance().addUI(memberId, UI.getCurrent());
        }
        presenter = new ExternalServicesPresenter(this, userID);
        buildView();
    }

    public void buildView(){
        createTopLayout();
        H1 header = new H1("Store Discount Policy");
        VerticalLayout layout = new VerticalLayout(header);
        layout.getStyle().set("background-color", "#ffc0cb"); // Set background color to dark pink
        layout.setSpacing(false);
        layout.setAlignItems(Alignment.CENTER);
        add(layout);
        Button addSupplyButton = new Button("Add Supply Service", event -> {
            getUI().ifPresent(ui -> ui.navigate("AddSupplyView"));
        });
        Button addPaymentButton = new Button("Add Payment Service", event -> {
            getUI().ifPresent(ui -> ui.navigate("AddPaymentView"));
        });
        Button removeSupplyButton = new Button("Remove Supply Service", event -> {
            getUI().ifPresent(ui -> ui.navigate("RemoveSupplyView"));
        });
        Button removePaymentButton = new Button("Remove Payment Service", event -> {
            getUI().ifPresent(ui -> ui.navigate("RemovePaymentView"));
        });
        createSupplyLayout();
        createPaymentLayout();
        add(new HorizontalLayout(addSupplyButton, addPaymentButton, removeSupplyButton, removePaymentButton));
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

    public void createSupplyLayout(){
        VerticalLayout supplyLayout = new VerticalLayout();
        supplyLayout.add(new H1("Supply Services:"));
        List<String> supplyServices = presenter.getSupplyServices();
        if (supplyServices == null) {
            servicesFailedToLoad();
        }
        else {
            if (supplyServices.size() == 0) {
                supplyLayout.add(new HorizontalLayout(new Text("No supply services")));
            }
            for (int i = 0; i < supplyServices.size(); i++) {
                supplyLayout.add(new HorizontalLayout(new Text(i + 1 + ". " + supplyServices.get(i))));
            }
            add(supplyLayout);
        }
    }

    public void createPaymentLayout(){
        VerticalLayout paymentLayout = new VerticalLayout();
        paymentLayout.add(new H1("Payment Services:"));
        List<String> paymentServices = presenter.getPaymentServices();
        if (paymentServices == null) {
            servicesFailedToLoad();
        }
        else {
            if (paymentServices.size() == 0) {
                paymentLayout.add(new HorizontalLayout(new Text("No payment services")));
            }
            for (int i = 0; i < paymentServices.size(); i++) {
                paymentLayout.add(new HorizontalLayout(new Text(i + 1 + ". " + paymentServices.get(i))));
            }
            add(paymentLayout);
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

    public void servicesFailedToLoad(){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Services failed to load");
        dialog.setText("Database is not connected");
        dialog.setConfirmText("OK");
        dialog.addConfirmListener(event -> dialog.close());
        dialog.open();
    }
}
