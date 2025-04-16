package com.example.application.View.MemberViews;

import com.example.application.Presenter.MemberPresenters.NotificationsPresenter;
import com.example.application.Util.AcquisitionDTO;
import com.example.application.Util.ReceiptDTO;
import com.example.application.WebSocketUtil.WebSocketHandler;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
//import org.springframework.messaging.simp.annotation.SubscribeMapping;

import java.util.List;

@Route("NotificationsView")

public class NotificationsView extends VerticalLayout {
    private NotificationsPresenter presenter;
    private String userID;
    private Grid<AcquisitionDTO> acquisitionGrid;
    private Grid<ReceiptDTO> receiptGrid;
    private VerticalLayout receiptDetailsLayout;
    private Button backButton;

    public NotificationsView() {
        userID = VaadinSession.getCurrent().getAttribute("userID").toString();
        Object memberIdObj = VaadinSession.getCurrent().getAttribute("memberId");
        if (memberIdObj!=null){
            String memberId = memberIdObj.toString();
            WebSocketHandler.getInstance().addUI(memberId, UI.getCurrent());
        }
        buildView();
    }

    public void buildView() {
        presenter = new NotificationsPresenter(this, userID);
        createTopLayout();
        H1 header = new H1("Notifications");
        VerticalLayout layout = new VerticalLayout(header);
        layout.getStyle().set("background-color", "#ffc0cb"); // Set background color to dark pink
        layout.setSpacing(false);
        layout.setAlignItems(Alignment.CENTER);
        add(layout);
        createNotificationsLayout();
        add(new Button("OK", event -> getUI().ifPresent(ui -> ui.navigate("MarketView"))));
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

    public void createNotificationsLayout(){
        VerticalLayout notificationsLayout = new VerticalLayout();
        List<String> notifications = presenter.getNotifications();
        if (notifications == null) {
            notificationsFailedToLoad();
        }
        else {
            if (notifications.size() == 0) {
                notificationsLayout.add(new HorizontalLayout(new Text("No notifications")));
            }
            for (int i = 0; i < notifications.size(); i++) {
                notificationsLayout.add(new HorizontalLayout(new Text(notifications.get(i))));
            }
            add(notificationsLayout);
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

    public void notificationsFailedToLoad(){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Notifications failed to load");
        dialog.setText("Database is not connected");
        dialog.setConfirmText("OK");
        dialog.addConfirmListener(event -> dialog.close());
        dialog.open();
    }
//    @SubscribeMapping("/topic/notifications")
//    public void handleNotification(String notificationMessage) {
//        UI.getCurrent().access(() -> {
//            Notification.show(notificationMessage);
//        });
//    }

    public void logout(){
        getUI().ifPresent(ui -> ui.navigate("MarketView"));
    }
}
