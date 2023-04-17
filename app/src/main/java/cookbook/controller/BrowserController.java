package cookbook.controller;

import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Arrays;

import cookbook.model.Recipe;
import cookbook.view.BrowserView;
import cookbook.view.BrowserViewObserver;

public class BrowserController implements BrowserViewObserver, BaseController {
  private ControllerManager controllerManager;
  private BrowserView browserView;
  private ArrayList<Recipe> recipes;

  public BrowserController(ControllerManager controllerManager) {
    this.controllerManager = controllerManager;
    recipes = this.controllerManager.getCookbook().getRecipes();
    this.browserView = new BrowserView(recipes);
    this.browserView.setObserver(this);
  }

  public void setControllerManager(ControllerManager manager) {
    this.controllerManager = manager;
}

  public String getTitle() {
    return "Recipe Browser";
  }

  public Node getView() {
    return this.browserView.getView();
  }

  public void handleBrowseRecipesClicked() {
    this.controllerManager.showBrowserView();
  }

  public void handleBackToHomeClicked() {
    this.controllerManager.showHomePageView();
  }

  public void handleGoToRecipeClicked(Recipe recipe) {
    this.controllerManager.showRecipeView();
  }

  public void handleSearch(String searchType, String searchText, ObservableList<String> selectedTags) {
    ArrayList<Recipe> searchResults = new ArrayList<>();

    if (!searchText.isEmpty()) {
        if (searchType.equals("Search by Name")) {
            String[] names = searchText.split(" ");
            ArrayList<String> nameList = new ArrayList<>(Arrays.asList(names));
            searchResults.addAll(this.controllerManager.getCookbook().getRecipesWithName(nameList));
        } else if (searchType.equals("Search by Ingredient")) {
            String[] ingredients = searchText.split(" ");
            ArrayList<String> ingredientList = new ArrayList<>(Arrays.asList(ingredients));
            searchResults.addAll(this.controllerManager.getCookbook().getRecipesWithIngredients(ingredientList));
        }
    } else {
        searchResults.addAll(this.controllerManager.getCookbook().getRecipes());
    }

    ArrayList<Recipe> filteredResults = new ArrayList<>();

    if (!selectedTags.isEmpty()) {
        filteredResults.addAll(this.controllerManager.getCookbook().getRecipesWithTags(new ArrayList<>(selectedTags)));
        filteredResults.retainAll(searchResults);
    } else {
        filteredResults.addAll(searchResults);
    }

    updateDisplayedRecipes(filteredResults);
}


  public void updateDisplayedRecipes(ArrayList<Recipe> filteredResults) {
    this.browserView.displayRecipes(filteredResults);
  }

}
