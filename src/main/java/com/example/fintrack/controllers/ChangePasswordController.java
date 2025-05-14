package com.example.fintrack.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

import com.example.fintrack.service.Mng;
import com.example.fintrack.utils.SceneManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChangePasswordController {

    private Mng manager;
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button changePasswordButton;

    private Map<String, String> userDatabase = new HashMap<>(); // Simulating user database

    public void setManager(Mng manager) {
        this.manager = manager;
    }


    @FXML
    private void handleChangePassword(ActionEvent event) {
        String email = emailField.getText().trim();
        String newPassword = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!userDatabase.containsKey(email)) {
            showAlert(Alert.AlertType.ERROR, "User Not Found", "The email does not exist in our system.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Password Mismatch", "The passwords do not match.");
            return;
        }

        // Update the password
        userDatabase.put(email, newPassword);
        showAlert(Alert.AlertType.INFORMATION, "Password Changed", "Your password has been successfully updated.");
        redirectToLoginPage();
    }


    private void redirectToLoginPage() {
        try {
            SceneManager.switchScene("/fxml/loginscreen.fxml", (LoginController ctrl) -> {
                ctrl.setManager(manager);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        SceneManager.switchScene("/fxml/loginscreen.fxml", (LoginController ctrl) -> {
            ctrl.setManager(manager);
        });
    }

}