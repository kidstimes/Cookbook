package cookbook.controller;

public class ConcreteControllerFactory implements ControllerFactory {
  public HomePageController createHomePageController(ControllerManager controllerManager) {
    return new HomePageController(controllerManager);
  }
  
  public BrowserController createBrowserController(ControllerManager controllerManager) {
    return new BrowserController(controllerManager);
  }
  
  public RecipeController createRecipeController(ControllerManager controllerManager) {
    return new RecipeController(controllerManager);
  }
}
  

