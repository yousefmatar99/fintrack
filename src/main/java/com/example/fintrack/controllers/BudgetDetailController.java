package com.example.fintrack.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.fxml.FXML;
//import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import com.example.fintrack.model.Budget;
import com.example.fintrack.model.Expense;
import com.example.fintrack.service.Mng;
import com.example.fintrack.utils.SceneManager;

public class BudgetDetailController {
   private Mng manager;
   private Budget currentBudget;
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
   private ScrollPane expenseScrollPane;
   @FXML
   private VBox expensesContainer;

   public void setManager(Mng manager) {
      this.manager = manager;
   }

   public void setBudget(Budget budget) {
      this.currentBudget = budget;
      this.updateBudgetInfo();
      this.loadExpenses();
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

   private void loadExpenses() {
      if (this.expensesContainer != null)
         this.expensesContainer.getChildren().clear();
      if (this.currentBudget != null && this.manager != null) {
         ArrayList<Expense> budgetExpenses = this.manager.getExpensesByBudget(this.currentBudget.getBudgetID());
         Iterator<Expense> var2 = budgetExpenses.iterator();

         while(var2.hasNext()) {
            Expense e = (Expense)var2.next();
            this.expensesContainer.getChildren().add(this.createExpenseRow(e));
         }

      }
   }

   private VBox createExpenseRow(Expense expense) {
      VBox expenseRow = new VBox(5);
      expenseRow.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-border-color: #999; -fx-padding: 8; -fx-border-radius: 5; -fx-background-radius: 5;");

      Label catLabel = new Label("Category: " + expense.getCategory());
      catLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #1A73E8;");

      Label amtLabel = new Label("Amount: $" + String.format("%.2f", expense.getAmount()));
      amtLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #1A73E8;");

      Label dateLabel = new Label("Date: " + String.valueOf(expense.getCreatedAt()));
      dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");

      // Load trash bin icon
      ImageView trashIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/trash.png")));
      trashIcon.setFitWidth(16);  // Adjust icon size
      trashIcon.setFitHeight(16);

      // Create delete button with icon
      Button deleteButton = new Button();
      deleteButton.setGraphic(trashIcon);
      deleteButton.setOnMouseClicked(event -> deleteExpense(expense));
      
      expenseRow.getChildren().addAll(catLabel, amtLabel, dateLabel, deleteButton);
      return expenseRow;
   }

   /**
    * Deletes an expense and refreshes the UI
    */
    private void deleteExpense(Expense expense) {
      if (this.manager != null && this.currentBudget != null) {
         this.manager.deleteExpense(expense.getExpenseID());  // Assume this method exists in Mng service
         this.manager.expenseDao.addSpent(-expense.getAmount(), this.currentBudget.getBudgetID());
         this.loadExpenses();  // Refresh the expense list after deletion
         this.updateBudgetInfo();  // Update the budget details
      }
   }

   @FXML
   private void handleAddExpense(MouseEvent event) throws IOException {
      SceneManager.switchScene("/fxml/addexpensescreen.fxml", (NewExpenseController ctrl) -> {
         ctrl.setManager(this.manager);
         ctrl.setBudget(this.currentBudget);
      });
   }

   @FXML
   private void handleBack(MouseEvent event) throws IOException {
      SceneManager.switchScene("/fxml/mainscreen.fxml", (DashboardController ctrl) -> {
         ctrl.setManager(this.manager);
      });
   }

   @FXML
    private void handleviewvisu(MouseEvent event) throws IOException {
        SceneManager.switchScene("/fxml/visualizationscreen.fxml", (VisualizationController ctrl) -> {
            ctrl.setManager(this.manager);
            ctrl.setBudget(this.currentBudget);
        });
    }
}
