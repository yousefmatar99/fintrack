package com.example.fintrack.controllers;

import com.example.fintrack.model.Budget;
import com.example.fintrack.model.Expense;
import com.example.fintrack.service.Mng;
import com.example.fintrack.utils.SceneManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisualizationController {
    private Mng manager;
    private Budget currentBudget;

    @FXML private PieChart expensePieChart;
    @FXML private BarChart<String, Number> expenseBarChart;
    @FXML private VBox chartsContainer;
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

    public void setManager(Mng manager) {
        this.manager = manager;
    }

    public void setBudget(Budget budget) {
        this.currentBudget = budget;
        this.updateBudgetInfo();
        generateCharts();
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

    /**
     * Generates the PieChart and BarChart dynamically.
     */
    private void generateCharts() {
        if (currentBudget == null) return;

        List<Expense> expenses = manager.getExpensesByBudget(currentBudget.getBudgetID());
        Map<String, Double> expenseData = aggregateExpensesByCategory(expenses);

        // Populate PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Double> entry : expenseData.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        expensePieChart.setData(pieChartData);
        expensePieChart.setTitle("Expense Breakdown");

        // Populate BarChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Double> entry : expenseData.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        expenseBarChart.getData().clear();
        expenseBarChart.getData().add(series);
    }

    /**
     * Aggregates expense amounts by category.
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

    @FXML
    private void handleBack(MouseEvent event) throws IOException {
      SceneManager.switchScene("/fxml/expencetrackingscreen.fxml", (BudgetDetailController ctrl) -> {
         ctrl.setManager(this.manager);
         ctrl.setBudget(this.currentBudget);
      });
    }

}