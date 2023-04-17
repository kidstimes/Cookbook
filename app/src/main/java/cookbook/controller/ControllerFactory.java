package cookbook.controller;

public class ControllerFactory {
  public static HomePageController createHomePageController(ControllerManager controllerManager) {
      HomePageController controller = new HomePageController(controllerManager);
      return controller;
  }

  public static BrowserController createBrowserController(ControllerManager controllerManager) {
      BrowserController controller = new BrowserController(controllerManager);
      return controller;
  }

  public static BaseController createRecipeController(ControllerManager controllerManager) {
      RecipeController controller = new RecipeController(controllerManager);
      return controller;
  }
}
