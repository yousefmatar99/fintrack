package com.example.fintrack.controllers;

import java.io.IOException;
import java.time.LocalDateTime;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import com.example.fintrack.model.Budget;
import com.example.fintrack.model.Expense;
import com.example.fintrack.service.Mng;
import com.example.fintrack.utils.SceneManager;

public class NewExpenseController {
   private Mng manager;
   private Budget currentBudget;
   @FXML
   private TextField expenseName;
   @FXML
   private TextField amount;
    @FXML
   private Label lblBudgetName;
   @FXML
   private Label lblBudgetLimit;
   @FXML
   private Label lblSpent;
   @FXML
   private Label lblBudgetRemaining;
   @FXML
   private ProgressBar budgetProgressBar;
   @FXML
   private ChoiceBox<String> expenseCategoryChoiceBox;

   private ObservableList<String> categories = FXCollections.observableArrayList(
      "Food", "Utilities", "Fitness", "Transport", "Other"
   );

   @FXML
   public void initialize() {
      // Populate the ChoiceBox with categories
      expenseCategoryChoiceBox.setItems(categories);
      expenseCategoryChoiceBox.setValue("Other");
   }

   public void setManager(Mng manager) {
      this.manager = manager;
   }

   public void setBudget(Budget budget) {
      this.currentBudget = budget;
      this.updateBudgetInfo();
   }

   private void updateBudgetInfo() {
      this.currentBudget = this.manager.getBudgetById(this.currentBudget.getBudgetID());
      if (this.currentBudget != null) {
         this.lblBudgetName.setText("Budget: " + this.currentBudget.getBudgetName());
         double limit = this.currentBudget.getLimit();
         double spent = this.currentBudget.getSpent();
         double remaining = limit - spent;
         if (remaining < 0.0) {
            remaining = 0.0;
         }

         this.lblBudgetLimit.setText("Limit: " + limit);
         this.lblSpent.setText("Spent: " + spent);
         this.lblBudgetRemaining.setText("Remaining: " + remaining);
         double progress = (limit - remaining) / limit;
         this.budgetProgressBar.setProgress(progress);
         this.budgetProgressBar.setStyle("-fx-accent: #4CAF50;");
      }
   }

   @FXML
   private void handleCreateExpense(MouseEvent event) {
      String expenseNameVal = expenseName.getText();
      String amountTextVal = amount.getText();
      String selectedCategory = expenseCategoryChoiceBox.getValue();

      if (!selectedCategory.isEmpty() && !amountTextVal.isEmpty() && !expenseNameVal.isEmpty()) {
         double amt;
         try {
            amt = Double.parseDouble(amountTextVal);
         } catch (NumberFormatException var9) {
            SceneManager.showAlert("Error", "Invalid amount. Must be numeric.");
            return;
         }

         Expense newExpense = new Expense();
         newExpense.setName(expenseNameVal);
         newExpense.setCategory(selectedCategory);
         newExpense.setAmount(amt);
         newExpense.setCreatedAt(LocalDateTime.now());
         newExpense.setBudgetID(this.currentBudget.getBudgetID());
         this.manager.createExpense(newExpense);
         SceneManager.showAlert("Success", "Expense created successfully!");
         this.updateBudgetInfo();

      } else {
         SceneManager.showAlert("Error", "Category and amount cannot be empty.");
      }
   }

   @FXML
   private void handleBack(MouseEvent event) throws IOException {
      SceneManager.switchScene("/fxml/expencetrackingscreen.fxml", (BudgetDetailController ctrl) -> {
         ctrl.setManager(this.manager);
         ctrl.setBudget(this.currentBudget);
      });
   }
}
