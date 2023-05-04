package cookbook;

import cookbook.controller.MainController;
import cookbook.database.Database;
import cookbook.model.CookbookFacade;
import javafx.application.Application;
import javafx.stage.Stage;


/**
 * The Cookbook application.
 * This is the main class of the application.
 */
public class Cookbook extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    // Init controller manager to manage the user interface
    Database database = new Database();
    CookbookFacade model = new CookbookFacade(database);
    MainController mainController = new MainController(primaryStage, model);
    mainController.runCookbook();

    // Set the onCloseRequest event handler when user closes the program window
    primaryStage.setOnCloseRequest(event -> {
      database.disconnect();
    });

  }


  /**
   * Main method.
   */
  public static void main(String args[]) {
    // Init UI
    launch(args);
  }
}