package cookbook.controller;

public interface ControllerFactory {
  public HomePageController createHomePageController(ControllerManager controllerManager);

  public BrowserController createBrowserController(ControllerManager controllerManager);

  public RecipeController createRecipeController(ControllerManager controllerManager);
}
