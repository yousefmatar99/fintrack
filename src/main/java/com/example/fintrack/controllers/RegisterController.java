package com.example.fintrack.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import com.example.fintrack.model.User;
import com.example.fintrack.service.Mng;
import com.example.fintrack.utils.SceneManager;

public class RegisterController {
   private Mng manager;
   @FXML
   private TextField usernameField;
   @FXML
   private TextField emailField;
   @FXML
   private PasswordField passwordField;
   @FXML
   private PasswordField confirmPasswordField;
   @FXML
   private DatePicker birthdatePicker;
   @FXML
   private Button handleRegisterbtn;

   public void setManager(Mng manager) {
      this.manager = manager;
   }

   @FXML
   private void handleRegister(ActionEvent event) {
      try {
         String username = this.usernameField.getText().trim();
         String email = this.emailField.getText().trim();
         String password = this.passwordField.getText();
         String confirmPass = this.confirmPasswordField.getText();
         LocalDate birthDate = (LocalDate)this.birthdatePicker.getValue();
         if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
            SceneManager.showAlert("Error", "All fields are required!");
            return;
         }

         if (!password.equals(confirmPass)) {
            SceneManager.showAlert("Error", "Passwords do not match!");
            return;
         }

         User existingUser = this.manager.getUserByUsername(username);
         if (existingUser != null) {
            SceneManager.showAlert("Error", "Username already taken!");
            return;
         }

         User newUser = new User();
         newUser.setName(username);
         newUser.setEmail(email);
         newUser.setPassword(password);
         if (birthDate != null) {
            int age = Period.between(birthDate, LocalDate.now()).getYears();
            newUser.setAge(age);
         }

         newUser.setCreatedAt(LocalDateTime.now());
         this.manager.addUser(newUser);
         this.goToDashborad(newUser);
      } catch (IOException var10) {
         var10.printStackTrace();
         SceneManager.showAlert("Error", "An error occurred during registration.");
      }

   }

   @FXML
   private void goBackToLogin(MouseEvent event) throws IOException {
      SceneManager.switchScene("/fxml/loginscreen.fxml", (LoginController ctrl) -> {
         ctrl.setManager(this.manager);
      });
   }

   private void goToDashborad(User user) throws IOException {
      SceneManager.switchScene("/fxml/mainscreen.fxml", (DashboardController ctrl) -> {
         this.manager.setLoggedInUser(user);
         ctrl.setManager(this.manager);
      });
   }
}

