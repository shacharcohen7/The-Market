package com.example.application.Presenter.GuestPresenters;

import com.example.application.Model.APIcalls;
import com.example.application.View.GuestViews.LoginView;
import com.vaadin.flow.server.VaadinSession;

public class LoginPresenter {
    private LoginView view;
    private String userID;

    public LoginPresenter(LoginView view, String userID) {
        this.view = view;
        this.userID = userID;
    }

    public void onLoginButtonClicked(String username, String password) {
        String result = APIcalls.login(userID, username, password);
        if (result != null && result.startsWith("member-")) {
            VaadinSession.getCurrent().setAttribute("memberID", result);
            view.loginSuccess();
        } else {
            view.loginFailure(result);
        }
    }
}
