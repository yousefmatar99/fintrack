package com.example.fintrack.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;


public class SceneManager {

    private static Stage primaryStage;

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Loads an FXML, optionally sets a controller function, then swaps the scene.
     * 
     * @param fxmlPath path to your FXML (e.g. "/fxml/Dashboard.fxml")
     * @param controllerConsumer a lambda that lets you pass data to the new controller
     * @throws IOException if FXML loading fails
     */
    public static <T> void switchScene(String fxmlFile, ControllerConsumer<T> controllerConsumer) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlFile));
        Parent root = loader.load();

        // Get the controller
        @SuppressWarnings("unchecked")
        T controller = (T) loader.getController();

        // Let the caller pass data or do more initialization
        if (controllerConsumer != null && controller != null) {
            controllerConsumer.accept(controller);
        }

        // Create new scene and set on the same stage
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void showAlert(String title, String message) {
        Alert alert = null;
        if (title.equals("ERROR")) {
            alert = new Alert(Alert.AlertType.ERROR);
        }
        // else if (title.equals("WARNING")) {
        //     alert = new Alert(Alert.AlertType.WARNING);
        // }
        else {
            alert = new Alert(Alert.AlertType.INFORMATION);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Functional interface for passing the new controller to a lambda.
     */
    @FunctionalInterface
    public interface ControllerConsumer<T> {
        void accept(T controller);
    }
}
