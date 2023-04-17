package cookbook;


import cookbook.controller.ControllerManager;
import javafx.application.Application;
import javafx.stage.Stage;


//Main class and entry point for the JavaFX application
public class Cookbook extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
      // Init controller manager to manage the user interface
      new ControllerManager(primaryStage);
    }

    public static void main(String args[]) {
        // Init UI
        launch(args);
    }
}