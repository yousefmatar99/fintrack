package com.example.fintrack;

import com.example.fintrack.service.Mng;
import com.example.fintrack.utils.SceneManager;

import javafx.application.Application;
import javafx.stage.Stage;

import com.example.fintrack.dao.UserDao;
import com.example.fintrack.controllers.LoginController;
import com.example.fintrack.dao.BudgetDao;
import com.example.fintrack.dao.ExpenseDao;
import com.example.fintrack.dao.NotificationDao;
import com.example.fintrack.dao.VisualizationDao;

public class Main extends Application {

    private Mng manager;

    @Override
    public void start(Stage primaryStage) throws Exception {

        UserDao userDao = new UserDao();
        BudgetDao budgetDao = new BudgetDao();
        ExpenseDao expenseDao = new ExpenseDao();
        NotificationDao notificationDao = new NotificationDao();
        VisualizationDao visualizationDao = new VisualizationDao();

        manager = new Mng(userDao, budgetDao, expenseDao, notificationDao, visualizationDao);

        SceneManager.setStage(primaryStage);
        SceneManager.switchScene("/fxml/loginscreen.fxml", (LoginController ctrl) -> {
            ctrl.setManager(manager);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
