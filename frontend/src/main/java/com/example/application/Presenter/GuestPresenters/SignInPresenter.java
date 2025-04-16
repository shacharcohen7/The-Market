package com.example.application.Presenter.GuestPresenters;

import com.example.application.Model.APIcalls;
import com.example.application.Util.UserDTO;
import com.example.application.View.GuestViews.SignInView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SignInPresenter {
    private SignInView view;
    private String userID;

    public SignInPresenter(SignInView view, String userID){
        this.view = view;
        this.userID = userID;
    }

    public void onSignInButtonClicked(String username, LocalDate birthdate, String country, String city,
                                      String address, String name, String password) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String result = APIcalls.register(new UserDTO(userID, username, birthdate.format(formatter), country, city, address, name), password);
        if (result != null && result.startsWith("member-")) {
            view.SignInSuccess("Registration performed.");
        } else {
            view.SignInFailure(result);
        }
    }
}
