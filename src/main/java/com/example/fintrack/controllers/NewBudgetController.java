package com.example.fintrack.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import com.example.fintrack.model.Budget;
import com.example.fintrack.model.User;
import com.example.fintrack.service.Mng;
import com.example.fintrack.utils.SceneManager;

public class NewBudgetController {
   private Mng manager;
   private User loggedInUser;
   @FXML
   private TextField budgetNameField;
   @FXML
   private TextField budgetLimitField;

   public void setManager(Mng manager) {
      this.manager = manager;
      this.loggedInUser = manager.getLoggedInUser();
   }

   @FXML
   private void handleSetBudget(MouseEvent event) {
      String budgetName = this.budgetNameField.getText().trim();
      String limitText = this.budgetLimitField.getText().trim();
      if (!budgetName.isEmpty() && !limitText.isEmpty()) {
         double limit;
         try {
            limit = Double.parseDouble(limitText);
         } catch (NumberFormatException var9) {
            SceneManager.showAlert("Error", "Invalid budget limit. Please enter a numeric value.");
            return;
         }

         Budget newBudget = new Budget();
         newBudget.setBudgetName(budgetName);
         newBudget.setLimit(limit);
         newBudget.setCreatedAt(LocalDateTime.now());
         this.manager.createBudgetForUser(this.loggedInUser.getUserID(), newBudget);
         SceneManager.showAlert("Success", "Budget created successfully!");
      } else {
         SceneManager.showAlert("Error", "Budget name and limit are required.");
      }
   }

   @FXML
   private void handleBack(MouseEvent event) throws IOException {
      goToHome();
   }

   private void goToHome() throws IOException {
      SceneManager.switchScene("/fxml/mainscreen.fxml", (DashboardController ctrl) -> {
         ctrl.setManager(this.manager);
      });
   }

}

