package cookbook.controller;

import cookbook.model.CookbookFacade;
import cookbook.view.ShoppingListView;
import cookbook.view.ShoppingListViewObserver;
import javafx.scene.Node;

/**
 * Controller for the shopping list view.
 */
public class ShoppingListController extends BaseController implements ShoppingListViewObserver {
  private ShoppingListView shoppingListView;

  /** Constructor for the shopping list controller.
   *
   * @param model the cookbook facade
   * @param mainController the main controller
   */
  public ShoppingListController(CookbookFacade model, MainController mainController) {
    super(model, mainController);
    this.shoppingListView = new ShoppingListView(model.getUserDisplayName(),
         model.loadShoppingListsFromDatabase());
    this.shoppingListView.setObserver(this);
  }

  public Node getView() {
    return this.shoppingListView.getView();
  }


  public void editIngredientInShoppingList(String ingredientName,
       float newQuantity, int weekNumber) {
    model.editIngredientInShoppingList(ingredientName, newQuantity, weekNumber);
  }

  public void deleteIngredientInShoppingList(String ingredientName, int weekNumber) {
    model.deleteIngredientInShoppingList(ingredientName, weekNumber);
  }


  @Override
  public void refreshShoppingListWithWeeklyDinnerList() {
    model.refreshShoppingListWithWeeklyDinnerList();
  }

  
}
