package com.example.fintrack.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.AnchorPane;
import com.example.fintrack.model.Budget;
import com.example.fintrack.model.Expense;
import com.example.fintrack.model.User;
import com.example.fintrack.service.Mng;
import com.example.fintrack.utils.SceneManager;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardController {

    private Mng manager;
    private User loggedInUser;

    @FXML
    private VBox budgetsContainer;

    public void setManager(Mng manager) {
        this.manager = manager;
        this.loggedInUser = manager.getLoggedInUser();
        loadBudgets();
    }

    @FXML
    private void handleAddBudget(MouseEvent event) throws IOException {
        SceneManager.switchScene("/fxml/setbudgetscreen.fxml", (NewBudgetController ctrl) -> {
            ctrl.setManager(manager);
        });
    }

    @FXML
    private void handleGoToProfile(MouseEvent event) throws IOException {
        SceneManager.switchScene("/fxml/profilemngscreen.fxml", (ProfileController ctrl) -> {
            ctrl.setManager(manager);
        });
    }

    private void loadBudgets() {
      if (this.budgetsContainer != null) {
         this.budgetsContainer.getChildren().clear();
      }

      List<Budget> userBudgets = manager.getBudgetsByUser(loggedInUser.getUserID());
      for (Budget budget : userBudgets) {
         budgetsContainer.getChildren().add(createBudgetCard(budget));
      }
    }

    /**
     * Creates a styled VBox for each budget as a card with all necessary details and pie chart.
     */
    private VBox createBudgetCard(Budget budget) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 15; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 5);");

        // Hover effects

        // Budget Info
        Label nameLabel = new Label("Budget Name: " + budget.getBudgetName());
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1A73E8;");

        double limit = budget.getLimit();
        double spent = budget.getSpent();
        double remaining = limit - spent;

        Label limitLabel = new Label("Limit: $" + String.format("%.2f", limit));
        limitLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #1A73E8;");

        Label remainingLabel = new Label("Remaining Budget: $" + String.format("%.2f", remaining));
        remainingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #1A73E8;");

        ProgressBar progressBar = new ProgressBar(spent / (limit > 0 ? limit : 1));
        progressBar.setPrefWidth(250);
        progressBar.setStyle("-fx-accent: #4CAF50;");

        // Pie Chart for Expenses Breakdown
        PieChart expensePieChart = new PieChart();
        expensePieChart.setPrefHeight(200);
        expensePieChart.setPrefWidth(250);

        Map<String, Double> expenseCategories = aggregateExpensesByCategory(this.manager.getExpensesByBudget(budget.getBudgetID()));
        populatePieChart(expensePieChart, expenseCategories);

        // Add elements to the card
        card.getChildren().addAll(nameLabel, limitLabel, remainingLabel, progressBar, expensePieChart);

        // Add click action to view budget details
        card.setOnMouseClicked(e -> {
            try {
                openBudgetDetail(budget);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        return card;
    }

    /**
     * Aggregates expense amounts by category from a list of Expense objects.
     */
    private Map<String, Double> aggregateExpensesByCategory(List<Expense> expenses) {
        Map<String, Double> categoryTotals = new HashMap<>();

        for (Expense expense : expenses) {
            String category = expense.getCategory();
            double amount = expense.getAmount();

            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + amount);
        }

        return categoryTotals;
    }

    /**
     * Populates a PieChart with expense data.
     */
    private void populatePieChart(PieChart pieChart, Map<String, Double> expenses) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        double total = expenses.values().stream().mapToDouble(Double::doubleValue).sum();

        for (Map.Entry<String, Double> entry : expenses.entrySet()) {
            String category = entry.getKey();
            double amount = entry.getValue();
            double percentage = (amount / total) * 100;
            pieChartData.add(new PieChart.Data(category + " (" + String.format("%.1f", percentage) + "%)", amount));
        }

        pieChart.setData(pieChartData);
        pieChart.setTitle("Expense Breakdown");
    }

    private void openBudgetDetail(Budget budget) throws IOException {
        SceneManager.switchScene("/fxml/expencetrackingscreen.fxml", (BudgetDetailController ctrl) -> {
            ctrl.setManager(manager);
            ctrl.setBudget(budget);
        });
    }

    @FXML
    private void handlelogout(MouseEvent event) throws IOException {
        SceneManager.switchScene("/fxml/loginscreen.fxml", (LoginController ctrl) -> {
            ctrl.setManager(manager);
        });
    }
}