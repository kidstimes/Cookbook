package cookbook;

<<<<<<< HEAD
import cookbook.controller.MainController;
import cookbook.database.Database;
import cookbook.model.CookbookFacade;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/**
 * The Cookbook application.
 * This is the main class of the application.
 */
public class Cookbook extends Application {

  @Override
  public void start(Stage primaryStage) {


    
    // Init controller manager to manage the user interface
    Database database = new Database();
    CookbookFacade model = new CookbookFacade(database);
    MainController mainController = new MainController(primaryStage, model);
    mainController.runCookbook();
    Font.loadFont(getClass().getResourceAsStream(
        "/fonts/Roboto-Regular.ttf"), 18);
    Font.loadFont(getClass().getResourceAsStream(
        "/fonts/Roboto-Italic.ttf"), 18);
    Font.loadFont(getClass().getResourceAsStream(
        "/fonts/Roboto-Bold.ttf"), 18);
    Font.loadFont(getClass().getResourceAsStream(
        "/fonts/Roboto-Bolditalic.ttf"), 18);


    // Set the onCloseRequest event handler when user closes the program window
    primaryStage.setOnCloseRequest(event -> database.disconnect());
  }


  /**
   * Main method.
   */
  public static void main(String[] args) {
    // Init UI
    launch(args);
  }
=======

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
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae
}