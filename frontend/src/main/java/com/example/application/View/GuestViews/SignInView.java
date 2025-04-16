package com.example.application.View.GuestViews;

import com.example.application.Presenter.GuestPresenters.SignInPresenter;
import com.example.application.WebSocketUtil.WebSocketHandler;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;

@Route("SignInView")
public class SignInView extends VerticalLayout {
    private SignInPresenter presenter;
    private String userID;
    private TextField userNameField;
    private DatePicker birthdateField;
    private TextField countryField;
    private TextField cityField;
    private TextField addressField;
    private TextField nameField;
    private PasswordField passwordField;
    private Button signInButton;
    private Button cancelButton;

    public SignInView(){
        userID = VaadinSession.getCurrent().getAttribute("userID").toString();
        Object memberIdObj = VaadinSession.getCurrent().getAttribute("memberId");
        if (memberIdObj!=null){
            String memberId = memberIdObj.toString();
            WebSocketHandler.getInstance().addUI(memberId, UI.getCurrent());
        }
        buildView();
    }

    public void buildView(){
        presenter = new SignInPresenter(this, userID);
        createTopLayout();
        H1 header = new H1("Sign In");
        VerticalLayout layout = new VerticalLayout(header);
        layout.getStyle().set("background-color", "#ffc0cb"); // Set background color to dark pink
        layout.setSpacing(false);
        layout.setAlignItems(Alignment.CENTER);
        add(layout);
        userNameField = new TextField("username");
        userNameField.setWidth("300px");
        birthdateField = new DatePicker("birthdate");
        birthdateField.setWidth("300px");
        countryField = new TextField("country");
        countryField.setWidth("300px");
        cityField = new TextField("city");
        cityField.setWidth("300px");
        addressField = new TextField("address");
        addressField.setWidth("300px");
        nameField = new TextField("name");
        nameField.setWidth("300px");
        passwordField = new PasswordField("password");
        passwordField.setWidth("300px");
        signInButton = new Button("Sign In", event -> {
            presenter.onSignInButtonClicked(userNameField.getValue(), birthdateField.getValue(),
                    countryField.getValue(), cityField.getValue(),
                    addressField.getValue(), nameField.getValue(),
                    passwordField.getValue());
        });
        signInButton.getStyle().set("background-color", "#e91e63").set("color", "white");

        cancelButton = new Button("Cancel", event -> {
            getUI().ifPresent(ui -> ui.navigate("MarketView"));
        });
        cancelButton.getStyle().set("background-color", "#cccccc").set("color", "white");
        HorizontalLayout buttonsLayout = new HorizontalLayout(signInButton, cancelButton);
        buttonsLayout.setSpacing(true);
        buttonsLayout.setPadding(true);

        VerticalLayout signInFormLayout = new VerticalLayout();
        signInFormLayout.setAlignItems(Alignment.CENTER);
        signInFormLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        signInFormLayout.getStyle().set("background-color", "#fff0f0").set("border-radius", "10px")
                .set("padding", "40px").set("box-shadow", "2px 2px 12px rgba(0, 0, 0, 0.1)");
        signInFormLayout.add(
                new HorizontalLayout(nameField, birthdateField),
                new HorizontalLayout(countryField, cityField),
                new HorizontalLayout(addressField, userNameField),
                new HorizontalLayout(passwordField),
                buttonsLayout
        );
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setAlignItems(Alignment.CENTER);
        mainLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        mainLayout.add(signInFormLayout);

        // Add the main layout to the view
        add(mainLayout);
    }

    public void createTopLayout(){
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.getStyle().set("background-color", "#fff0f0"); // Set background color
        Text helloMessage = new Text("Hello, Guest");
        Button homeButton = new Button("Home", new Icon(VaadinIcon.HOME), event -> getUI().ifPresent(ui -> ui.navigate("MarketView")));
        Button shoppingCartButton = new Button("Shopping Cart", new Icon(VaadinIcon.CART),
                event -> getUI().ifPresent(ui -> ui.navigate("ShoppingCartView")));
        Button loginButton = new Button("Log In", event -> {
            getUI().ifPresent(ui -> ui.navigate("LoginView"));
        });
        Button signInButton = new Button("Sign In", event -> {
            getUI().ifPresent(ui -> ui.navigate("SignInView"));
        });
        topLayout.add(helloMessage, homeButton, shoppingCartButton, loginButton, signInButton);

        add(topLayout);
    }

    public void SignInFailure(String message) {
        Notification.show(message, 3000, Notification.Position.MIDDLE);
    }

    public void SignInSuccess(String message) {
        Notification.show(message, 3000, Notification.Position.MIDDLE);
        getUI().ifPresent(ui -> ui.navigate("MarketView"));
    }
}
