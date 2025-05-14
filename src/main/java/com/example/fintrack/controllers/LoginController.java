package com.example.fintrack.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import com.example.fintrack.model.User;
import com.example.fintrack.service.Mng;
import com.example.fintrack.utils.SceneManager;

public class LoginController {
   private Mng manager;
   @FXML
   private TextField useremail;
   @FXML
   private PasswordField userpass;

   public void setManager(Mng manager) {
      this.manager = manager;
   }

   @FXML
   private void handleLogin(MouseEvent event) throws IOException {
      String inputUserEmail = this.useremail.getText();
      String inputPassword = this.userpass.getText();
      User user = this.manager.getUserByEmail(inputUserEmail);
      if (user != null && user.getPassword().equals(inputPassword)) {
         this.goToDashborad(user);
      } else {
         SceneManager.showAlert("Error", "Wrong Credentials!");
      }

   }

   @FXML
   private void handleRegister(MouseEvent event) throws IOException {
      this.goToRegister();
   }

   private void goToDashborad(User user) throws IOException {
      SceneManager.switchScene("/fxml/mainscreen.fxml", (DashboardController ctrl) -> {
         this.manager.setLoggedInUser(user);
         ctrl.setManager(this.manager);
      });
   }

   private void goToRegister() throws IOException {
      SceneManager.switchScene("/fxml/registrationscreen.fxml", (RegisterController ctrl) -> {
         ctrl.setManager(this.manager);
      });
   }

   @FXML
   private void handlechangepass(MouseEvent event) throws IOException {
      SceneManager.switchScene("/fxml/changepassword.fxml", (ChangePasswordController ctrl) -> {
      });
   }
}
