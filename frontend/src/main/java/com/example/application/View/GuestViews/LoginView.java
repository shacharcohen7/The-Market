package com.example.application.View.GuestViews;

import com.example.application.Presenter.GuestPresenters.LoginPresenter;
import com.example.application.WebSocketUtil.WebSocketHandler;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
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

@Route("LoginView")
public class LoginView extends VerticalLayout  {
    private LoginPresenter presenter;
    private String userID;
    private TextField userNameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button cancelButton;
    WebSocketHandler webSocketHandler;

    public LoginView() {
        userID = VaadinSession.getCurrent().getAttribute("userID").toString();
        buildView();
    }

    public void buildView(){
        presenter = new LoginPresenter(this, userID);

        createTopLayout();
        H1 header = new H1("Log In");
        VerticalLayout layout = new VerticalLayout(header);
        layout.getStyle().set("background-color", "#ffc0cb"); // Set background color to dark pink
        layout.setSpacing(false);
        layout.setAlignItems(Alignment.CENTER);
        add(layout);

        userNameField = new TextField();
        userNameField.setPlaceholder("Username");
        userNameField.setWidth("300px");
        userNameField.getStyle().set("margin-bottom", "10px");

        passwordField = new PasswordField();
        passwordField.setPlaceholder("Password");
        passwordField.setWidth("300px");
        passwordField.getStyle().set("margin-bottom", "20px");
        //userNameField = new TextField("", "username");
        //passwordField = new PasswordField("", "password");
        loginButton = new Button("Login", event -> {
            presenter.onLoginButtonClicked(userNameField.getValue(), passwordField.getValue());
        });
        loginButton.getStyle().set("background-color", "#e91e63").set("color", "white");
        loginButton.setWidth("140px");
        cancelButton = new Button("Cancel", event -> {
            getUI().ifPresent(ui -> ui.navigate("MarketView"));
        });
        cancelButton.getStyle().set("background-color", "#cccccc").set("color", "white");
        cancelButton.setWidth("140px");

        HorizontalLayout buttonsLayout = new HorizontalLayout(loginButton, cancelButton);
        buttonsLayout.setSpacing(true);
        buttonsLayout.setPadding(true);
        VerticalLayout loginFormLayout = new VerticalLayout();
        loginFormLayout.setAlignItems(Alignment.CENTER);
        loginFormLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        loginFormLayout.getStyle().set("background-color", "#fff0f0").set("border-radius", "10px")
                .set("padding", "40px").set("box-shadow", "2px 2px 12px rgba(0, 0, 0, 0.1)");
        loginFormLayout.add(
                userNameField,
                passwordField,
                buttonsLayout
        );

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setAlignItems(Alignment.CENTER);
        mainLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        mainLayout.add(loginFormLayout);

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

    public void loginFailure(String message) {
        Notification.show(message, 3000, Notification.Position.MIDDLE);
    }

    public void loginSuccess() {
        webSocketHandler = WebSocketHandler.getInstance();
        webSocketHandler.openConnection(VaadinSession.getCurrent().getAttribute("memberID").toString(), UI.getCurrent());
        getUI().ifPresent(ui -> ui.navigate("NotificationsView"));
    }

}
