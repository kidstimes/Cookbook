package cookbook.controller;

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

}
