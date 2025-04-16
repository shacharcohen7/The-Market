package com.example.application.View.MemberViews;

import com.example.application.Presenter.MemberPresenters.JobProposalsPresenter;
import com.example.application.Util.StoreManagerDTO;
import com.example.application.Util.StoreOwnerDTO;
import com.example.application.WebSocketUtil.WebSocketHandler;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;

@Route("JobProposalsView")
public class JobProposalsView extends VerticalLayout {
    private JobProposalsPresenter presenter;
    private String userID;

    public JobProposalsView(){
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
            presenter = new JobProposalsPresenter(this, userID);
            createTopLayout();
            H1 header = new H1("Job Proposals");
            VerticalLayout layout = new VerticalLayout(header);
            layout.getStyle().set("background-color", "#ffc0cb"); // Set background color to dark pink
            layout.setSpacing(false);
            layout.setAlignItems(Alignment.CENTER);
            add(layout);
            createOwnerProposalsLayout();
            createManagerProposalsLayout();
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

    public void createOwnerProposalsLayout(){
        VerticalLayout ownerProposalsLayout = new VerticalLayout();
        ownerProposalsLayout.add(new H1("Store Owner Proposals"));
        List<StoreOwnerDTO> ownerProposals = presenter.getStoreOwnerProposals();
        if (ownerProposals == null) {
            ownerProposalsFailedToLoad();
        }
        else{
            if (ownerProposals.size() == 0) {
                ownerProposalsLayout.add(new HorizontalLayout(new Text("No store owner proposals")));
            }
            for(int i=0 ; i<ownerProposals.size() ; i++){
                VerticalLayout ownerProposalLayout = new VerticalLayout();
                StoreOwnerDTO ownerProposal = ownerProposals.get(i);
                if(presenter.getStore(ownerProposal.getStoreId()) == null){
                    storeFailedToLoad();
                }
                else {
                    String storeID = ownerProposal.getStoreId();
                    ownerProposalLayout.add(
                            new HorizontalLayout(new Text("Store ID - " + storeID)),
                            new HorizontalLayout(new Text("Store Name - " + presenter.getStoreName(storeID))),
                            new HorizontalLayout(
                                new Button("Approve", event -> {
                                    presenter.answerProposal(storeID, false, true);
                                }),
                                new Button("Decline", event -> {
                                    presenter.answerProposal(storeID, false, false);
                                })
                            )
                    );
                    ownerProposalsLayout.add(ownerProposalLayout);
                }
            }
            add(ownerProposalsLayout);
        }
    }

    public void createManagerProposalsLayout(){
        VerticalLayout managerProposalsLayout = new VerticalLayout();
        managerProposalsLayout.add(new H1("Store Manager Proposals"));
        List<StoreManagerDTO> managerProposals = presenter.getStoreManagerProposals();
        if (managerProposals == null) {
            managerProposalsFailedToLoad();
        }
        else{
            if (managerProposals.size() == 0) {
                managerProposalsLayout.add(new HorizontalLayout(new Text("No store manager proposals")));
            }
            for(int i = 0; i< managerProposals.size() ; i++){
                VerticalLayout managerProposalLayout = new VerticalLayout();
                StoreManagerDTO managerProposal = managerProposals.get(i);
                if(presenter.getStore(managerProposal.getStoreId()) == null){
                    storeFailedToLoad();
                }
                else {
                    String storeID = managerProposal.getStoreId();
                    managerProposalLayout.add(
                            new HorizontalLayout(
                                    new Text("Store ID - " + storeID),
                                    new Text("Store Name - " + presenter.getStoreName(storeID))
                            ),
                            new HorizontalLayout(
                                    new Button("Approve", event -> {
                                        presenter.answerProposal(storeID, true, true);
                                    }),
                                    new Button("Decline", event -> {
                                        presenter.answerProposal(storeID, true, false);
                                    })
                            )
                    );
                    managerProposalsLayout.add(managerProposalLayout);
                }
            }
            add(managerProposalsLayout);
        }
    }

    public void answerSuccess(String message) {
        Notification.show(message, 3000, Notification.Position.MIDDLE);
        getUI().ifPresent(ui -> ui.navigate("MarketView"));
    }

    public void answerFailure(String message) {
        Notification.show(message, 3000, Notification.Position.MIDDLE);
    }

    public void storeFailedToLoad(){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Store failed to load");
        dialog.setText("Database is not connected");
        dialog.setConfirmText("OK");
        dialog.addConfirmListener(event -> dialog.close());
        dialog.open();
    }

    public void ownerProposalsFailedToLoad(){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Store owner proposals failed to load");
        dialog.setText("Database is not connected");
        dialog.setConfirmText("OK");
        dialog.addConfirmListener(event -> dialog.close());
        dialog.open();
    }

    public void managerProposalsFailedToLoad(){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Store manager proposals failed to load");
        dialog.setText("Database is not connected");
        dialog.setConfirmText("OK");
        dialog.addConfirmListener(event -> dialog.close());
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

    public void updateFailure(String message) {
        Notification.show(message, 3000, Notification.Position.MIDDLE);
    }

    public void updateSuccess(String message) {
        Notification.show(message, 3000, Notification.Position.MIDDLE);
        getUI().ifPresent(ui -> ui.navigate("MarketView"));
    }
}
