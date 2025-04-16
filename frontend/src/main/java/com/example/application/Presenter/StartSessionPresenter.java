package com.example.application.Presenter;

import com.example.application.Model.APIcalls;
import com.example.application.Util.APIResponse;
import com.example.application.View.StartSessionView;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Wrapper;
import java.util.Collections;


public class StartSessionPresenter {
    private String userID;
    private StartSessionView view;
    RestTemplate restTemplate;

    public StartSessionPresenter(StartSessionView view) {
        this.view = view;
        this.restTemplate = new RestTemplate();
        startSession();
    }

    private void startSession() {
        userID = APIcalls.startSession();
        VaadinSession.getCurrent().setAttribute("userID", userID);
        view.startSession();
    }

}
