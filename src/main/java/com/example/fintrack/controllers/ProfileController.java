package com.example.fintrack.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import com.example.fintrack.model.User;
import com.example.fintrack.service.Mng;
import com.example.fintrack.utils.SceneManager;

public class ProfileController {
   private com.example.fintrack.service.Mng manager;
   private User currentUser;
   @FXML
   private Label nameLabel;
   @FXML
   private TextField passwordField;
   @FXML
   private TextField confirmPasswordField;


   public void setManager(Mng manager) {
      this.manager = manager;
      this.currentUser = manager.getLoggedInUser();
      this.loadProfileInfo();
   }

   private void loadProfileInfo() {
      if (this.currentUser != null) {
         this.nameLabel.setText("Name: " + this.currentUser.getName());
      }
   }

   @FXML
   private void handleBack(MouseEvent event) throws IOException {
      SceneManager.switchScene("/fxml/mainscreen.fxml", (DashboardController ctrl) -> {
         ctrl.setManager(this.manager);
      });
   }

   @FXML
   private void handleChangePassword(MouseEvent event) {
      String newPassword = passwordField.getText();
      String confirmPassword = confirmPasswordField.getText();

      if (!newPassword.equals(confirmPassword)) {
         SceneManager.showAlert("Error", "Password Mismatch - The passwords do not match.");
         return;
      }

      this.manager.updateUserPassword(this.currentUser, newPassword);
      SceneManager.showAlert("Infromation", "Password Changed - Your password has been successfully updated.");
   }

}
