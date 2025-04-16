package com.example.application.View;

import com.example.application.Presenter.PaymentPresenter;
import com.example.application.Util.CartDTO;
import com.example.application.WebSocketUtil.WebSocketHandler;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("PaymentView")
public class PaymentView extends VerticalLayout {

    private PaymentPresenter presenter;
    private String userID;
    private CartDTO cartDTO;
    private TextField holderIDField;
    private TextField holderNameField;
    private TextField creditCardField;
    private TextField currencyField;
    private ComboBox<Integer> yearComboBox;
    private ComboBox<Integer> monthComboBox;
    private IntegerField cvvField;
    private Button submitButton;
    private Button cancelButton;

    public PaymentView() {
        userID = VaadinSession.getCurrent().getAttribute("userID").toString();
        Object memberIdObj = VaadinSession.getCurrent().getAttribute("memberId");
        if (memberIdObj!=null){
            String memberId = memberIdObj.toString();
            WebSocketHandler.getInstance().addUI(memberId, UI.getCurrent());
        }
        cartDTO = (CartDTO) VaadinSession.getCurrent().getAttribute("cartDTO");
        buildView();
    }

    private void buildView() {
        presenter = new PaymentPresenter(this, userID, cartDTO);

        createTopLayout();
        H1 header = new H1("Payment Details");
        VerticalLayout layout = new VerticalLayout(header);
        layout.getStyle().set("background-color", "#ffc0cb"); // Set background color to dark pink
        layout.setSpacing(false);
        layout.setAlignItems(Alignment.CENTER);
        add(layout);

        holderIDField = new TextField("Holder ID");
        holderIDField.setWidth("300px");

        holderNameField = new TextField("Holder Name");
        holderNameField.setWidth("300px");

        currencyField = new TextField("Currency");
        currencyField.setWidth("300px");

        creditCardField = new TextField("Credit Card");
        creditCardField.setWidth("300px");

        yearComboBox = new ComboBox<>("Year");
        yearComboBox.setItems(2024, 2025, 2026, 2027, 2028, 2029, 2030, 2031, 2032);
        yearComboBox.setWidth("300px");

        monthComboBox = new ComboBox<>("Month");
        monthComboBox.setItems(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        monthComboBox.setWidth("300px");

        cvvField = new IntegerField("CVV");
        cvvField.setWidth("300px");

        submitButton = new Button("Submit", event -> {
            presenter.onSubmitButtonClicked(
                    holderIDField.getValue(),
                    holderNameField.getValue(),
                    currencyField.getValue(),
                    creditCardField.getValue(),
                    cvvField.getValue(),
                    monthComboBox.getValue(),
                    yearComboBox.getValue()
            );
        });
        submitButton.getStyle().set("background-color", "#e91e63").set("color", "white");

        cancelButton = new Button("Cancel", event -> {
            getUI().ifPresent(ui -> ui.navigate("MarketView"));
        });
        cancelButton.getStyle().set("background-color", "#cccccc").set("color", "white");

        HorizontalLayout buttonsLayout = new HorizontalLayout(submitButton, cancelButton);
        buttonsLayout.setSpacing(true);
        buttonsLayout.setPadding(true);

        VerticalLayout paymentFormLayout = new VerticalLayout();
        paymentFormLayout.setAlignItems(Alignment.CENTER);
        paymentFormLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        paymentFormLayout.getStyle().set("background-color", "#fff0f0").set("border-radius", "10px")
                .set("padding", "40px").set("box-shadow", "2px 2px 12px rgba(0, 0, 0, 0.1)");
        paymentFormLayout.add(
                new HorizontalLayout(holderIDField, holderNameField, currencyField),
                new HorizontalLayout(creditCardField, monthComboBox, yearComboBox, cvvField),
                buttonsLayout
        );

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setAlignItems(Alignment.CENTER);
        mainLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        mainLayout.add(paymentFormLayout);

        // Add the main layout to the view
        add(mainLayout);
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

    public void paymentResult(String message){
        Notification.show(message, 3000, Notification.Position.MIDDLE);
        this.removeAll();
        getUI().ifPresent(ui -> ui.navigate("MarketView"));
    }
}

