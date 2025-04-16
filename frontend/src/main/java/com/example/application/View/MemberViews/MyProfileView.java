package com.example.application.View.MemberViews;

import com.example.application.Presenter.MemberPresenters.MyProfilePresenter;
import com.example.application.Util.UserDTO;
import com.example.application.WebSocketUtil.WebSocketHandler;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Route("MyProfileView")
public class MyProfileView extends VerticalLayout {
    private MyProfilePresenter presenter;
    private String userID;

    public MyProfileView(){
        userID = VaadinSession.getCurrent().getAttribute("userID").toString();
        Object memberIdObj = VaadinSession.getCurrent().getAttribute("memberId");
        if (memberIdObj!=null){
            String memberId = memberIdObj.toString();
            WebSocketHandler.getInstance().addUI(memberId, UI.getCurrent());
        }
        buildView();
    }

    public void buildView(){
        try {
            presenter = new MyProfilePresenter(this, userID);
            createTopLayout();
            H1 header = new H1("My Profile");
            VerticalLayout layout = new VerticalLayout(header);
            layout.getStyle().set("background-color", "#ffc0cb"); // Set background color to dark pink
            layout.setSpacing(false);
            layout.setAlignItems(Alignment.CENTER);
            add(layout);
            UserDTO userDto = presenter.getUser();
            if (userDto == null) {
                userFailedToLoad();
            }
            else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                Button OkButton = new Button("OK", event -> {
                    getUI().ifPresent(ui -> ui.navigate("MarketView"));
                });
                add(
                        new HorizontalLayout(new Text("Name: " + userDto.getName())),
                        new HorizontalLayout(new Text("Birth Date: " + LocalDate.parse(userDto.getBirthday(), formatter))),
                        new HorizontalLayout(new Text("Country: " + userDto.getCountry())),
                        new HorizontalLayout(new Text("City: " + userDto.getCity())),
                        new HorizontalLayout(new Text("Address: " + userDto.getAddress())),
                        new HorizontalLayout(OkButton)
                );
            }
        } catch (Exception e){
            System.out.println(e);
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

    public void userFailedToLoad(){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("User Profile failed to load");
        dialog.setText("Database is not connected");
        dialog.setConfirmText("OK");
        dialog.addConfirmListener(event -> dialog.close());
        dialog.open();
    }

    public void logout(){
        getUI().ifPresent(ui -> ui.navigate("MarketView"));
    }

    public void updateFailure(String message) {
        Notification.show(message, 3000, Notification.Position.MIDDLE);
    }

    public void updateSuccess(String message) {
        Notification.show(message, 3000, Notification.Position.MIDDLE);
        getUI().ifPresent(ui -> ui.navigate("MarketView"));
    }
}
