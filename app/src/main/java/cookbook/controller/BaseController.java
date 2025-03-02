package cookbook.controller;

<<<<<<< HEAD
import cookbook.model.CookbookFacade;

/**
 * Base controller for all controllers.
 */
public abstract class BaseController {
  protected MainController mainController;
  protected CookbookFacade model;


  /**
   * Constructor for the base controller.
   */
  public BaseController(CookbookFacade model, MainController mainController) {
    this.model = model;
    this.mainController = mainController;

  }

  public void goToHomePage() {
    mainController.goToHomePage();
  }

  public void goToBrowser() {
    mainController.goToBrowser();
  }

  public void goToAddRecipe() {
    mainController.goToAddRecipe();
  }


  public void userLogout() {
    mainController.userLogout();
  }

  public void goToWeeklyDinner() {
    mainController.goToWeeklyDinner();
  }

  public void goToShoppingList() {
    mainController.goToShoppingList();
  }

  public void goToMyFavorite() {
    mainController.goToMyFavorite();
  } 

  public void goToMessages() {
    mainController.goToMessages();
  }

  public void goToHelp() {
    mainController.goToHelp();
  }

  public void goToAccount() {
    mainController.goToAccount();
  }
=======
import javafx.scene.Node;

/**
 * The interface for the controllers that handle various functionalities.
 */
public interface BaseController {

  /**
   * Get the view to display.
   *
   * @return the central Node of the view
   */
  Node getView();
>>>>>>> 650f248cb461825fbe4c63afa2464483f95a54ae
}
