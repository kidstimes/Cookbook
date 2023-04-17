package cookbook;


import cookbook.controller.ControllerManager;
import javafx.application.Application;
import javafx.stage.Stage;


public class Cookbook extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
      // Init root controller
      new ControllerManager(primaryStage);
    }

    public static void main(String args[]) {
        // Init UI
        launch(args);
    }
}